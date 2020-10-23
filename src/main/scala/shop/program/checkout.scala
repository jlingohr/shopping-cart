package shop.program

import cats.effect.Timer
import cats.implicits._
import io.chrisdavenport.log4cats.Logger
import retry.RetryDetails.{GivingUp, WillDelayAndRetry}
import retry.{RetryDetails, RetryPolicy, retryingOnAllErrors}
import shop.algebra._
import shop.domain.auth.UserId
import shop.domain.cart._
import shop.domain.checkout._
import shop.domain.order._
import shop.domain.payment._
import shop.effects._
import squants.market.Money

import scala.concurrent.duration._






final class CheckoutProgram[F[_]: Background: Logger: MonadThrow: Timer]
(
  paymentClient: PaymentClient[F],
  shoppingCart: ShoppingCart[F],
  orders: Orders[F],
  retryPolicy: RetryPolicy[F]
) {

  private def logError(action: String)(e: Throwable, details: RetryDetails): F[Unit] =
    details match {
      case r: WillDelayAndRetry =>
        Logger[F].error(s"Failed on $action. We retried ${r.retriesSoFar} times.")
      case g: GivingUp =>
        Logger[F].error(s"Giving up on $action after ${g.totalRetries} retries.")
    }

  private def processPayment(payment: Payment): F[PaymentId] = {
    val action = retryingOnAllErrors[PaymentId](
      policy = retryPolicy,
      onError = logError("Payments")
    )(paymentClient.process(payment))

    action.adaptError {
      case e => PaymentError(Option(e.getMessage).getOrElse("Unknown"))
    }
  }

  private def createOrder(userId: UserId,
                         paymentId: PaymentId,
                         items: List[CartItem],
                         total: Money): F[OrderId] = {
    val action = retryingOnAllErrors[OrderId](
      policy = retryPolicy,
      onError = logError("Order")
    )(orders.create(userId, paymentId, items, total))

    def bgAction(fa: F[OrderId]): F[OrderId] =
      fa.adaptError {
        case e => OrderError(e.getMessage)
      }
        .onError {
          case _ =>
            Logger[F].error(s"Failed to create order for: ${paymentId}") *>
              Background[F].schedule(bgAction(fa), 1.hour)
        }
    bgAction(action)
  }

  def checkout(userId: UserId, card: Card): F[OrderId] = {
    shoppingCart.get(userId)
      .ensure(EmptyCartError)(_.items.nonEmpty)
      .flatMap {
        case CartTotal(items, total) =>
          for {
            pid <- processPayment(Payment(userId, total, card))
            order <- createOrder(userId, pid, items, total)
            _ <- shoppingCart.delete(userId).attempt.void
          } yield order
      }
  }
}
package shop.http.routes.secured

import cats.{Defer, Monad}
import org.http4s.{AuthedRoutes, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{AuthMiddleware, Router}
import shop.algebra.Orders
import shop.domain.order.OrderId
import shop.http.auth.users.CommonUser

final class OrderRoutes[F[_]: Defer: Monad](orders: Orders[F]) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/orders"

  private val httpRoutes: AuthedRoutes[CommonUser, F] = AuthedRoutes.of {
    case GET -> Root as user =>
      Ok(orders.findBy(user.value.id))

    case GET -> Root / UUIDVar(orderId) as user =>
      Ok(orders.get(user.value.id, OrderId(orderId)))
  }

  def routes(authMiddleware: AuthMiddleware[F, CommonUser]): HttpRoutes[F] =
    Router(prefixPath -> authMiddleware(httpRoutes))



}

package shop.http.routes.admin

import cats.Defer
import org.http4s.{AuthedRoutes, HttpRoutes}
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{AuthMiddleware, Router}
import shop.algebra.Items
import shop.domain.brand.BrandParam
import shop.domain.item.{CreateItemParam, UpdateItemParam}
import shop.effects.MonadThrow
import shop.http.auth.users.AdminUser
import shop.http.decoder.RefinedRequestDecoder

final class AdminItemRoutes[F[_]: Defer: JsonDecoder: MonadThrow](items: Items[F]) extends Http4sDsl[F] {

  private[admin] val prefixPath = "/items"

  private val httpRoutes: AuthedRoutes[AdminUser, F] =
    AuthedRoutes.of {
      case ar @ POST -> Root as _ =>
        ar.req.decodeR[CreateItemParam] { bp =>
          Created(items.create(bp.toDomain))
        }

      case ar @ PUT -> Root as _ =>
        ar.req.decodeR[UpdateItemParam] { item =>
          Ok(items.update(item.toDomain))
        }
    }

  def routes(authMiddleware: AuthMiddleware[F, AdminUser]): HttpRoutes[F] =
    Router(prefixPath -> authMiddleware(httpRoutes))

}

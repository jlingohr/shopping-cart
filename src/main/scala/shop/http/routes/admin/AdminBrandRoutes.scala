package shop.http.routes.admin

import cats.Defer
import org.http4s.{AuthedRoutes, HttpRoutes}
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{AuthMiddleware, Router}
import shop.algebra.Brands
import shop.domain.brand.BrandParam
import shop.effects.MonadThrow
import shop.http.auth.users.AdminUser
import shop.http.decoder.RefinedRequestDecoder

final class AdminBrandRoutes[F[_]: Defer: JsonDecoder: MonadThrow](brands: Brands[F]) extends Http4sDsl[F] {

  private[admin] val prefixPath = "/brands"

  private val httpRoutes: AuthedRoutes[AdminUser, F] =
    AuthedRoutes.of {
      case ar @ POST -> Root as _ =>
        ar.req.decodeR[BrandParam] { bp =>
          Created(brands.create(bp.toDomain))
        }
    }

  def routes(authMiddleware: AuthMiddleware[F, AdminUser]): HttpRoutes[F] =
    Router(prefixPath -> authMiddleware(httpRoutes))

}
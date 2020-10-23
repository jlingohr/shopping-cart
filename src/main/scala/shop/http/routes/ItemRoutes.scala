package shop.http.routes

import cats.{Defer, Monad}
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import shop.algebra.Items
import shop.domain.brand.BrandParam
import shop.http.json.deriveEntityEncoder

class ItemRoutes[F[_]: Defer: Monad](items: Items[F]) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/items"

  object BrandyQueryParam extends OptionalQueryParamDecoderMatcher[BrandParam]("brand")

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root :? BrandyQueryParam(brand) =>
      Ok(brand.fold(items.findAll)(b => items.findBy(b.toDomain)))
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )


}

package shop.domain

import java.util.UUID

import io.circe._
import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype


import scala.util.control.NoStackTrace

object auth {

  @newtype case class UserId(value: UUID)
  @newtype case class UserName(value: String)
  @newtype case class Password(value: String)
  @newtype case class JwtToken(value: String)

  @newtype case class UserNameParam(value: NonEmptyString) {
    def toDomain: UserName = UserName(value.value.toLowerCase)
  }

  @newtype case class PasswordParam(value: NonEmptyString) {
    def toDomain: Password = Password(value.value)
  }

  case class CreateUser(username: UserNameParam, password: PasswordParam)
  case class UserNameInUse(username: UserName) extends NoStackTrace

  case class User(id: UserId, name: UserName)

  case class InvalidUserOrPassword(username: UserName) extends NoStackTrace
  case object UnsupportedOperation extends NoStackTrace

  case class LoginUser(
                        username: UserNameParam,
                        password: PasswordParam
                      )

  @newtype case class ClaimContent(uuid: UUID)

  object ClaimContent {
    implicit val jsonDecoder: Decoder[ClaimContent] =
      Decoder.forProduct1("uuid")(ClaimContent.apply)
  }

}

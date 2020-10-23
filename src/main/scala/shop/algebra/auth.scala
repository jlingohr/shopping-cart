package shop.algebra

import shop.domain.auth.{JwtToken, Password, User, UserId, UserName}

trait Users[F[_]] {

  def find(username: UserName,
          password: Password): F[Option[User]]

  def create(username: UserName,
            password: Password): F[UserId]

}

trait Auth[F[_]] {
  def findUser(token: JwtToken): F[Option[User]]
  def newUser(username: UserName, password: Password): F[JwtToken]
  def login(username: UserName, password: Password): F[JwtToken]
  def logout(token: JwtToken, username: UserName): F[Unit]
}

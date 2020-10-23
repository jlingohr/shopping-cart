package shop.algebra

import shop.domain.auth.UserId
import shop.domain.cart.{Cart, CartTotal, Quantity}
import shop.domain.item.ItemId

trait ShoppingCart[F[_]] {

  def add(userId: UserId, itemId: ItemId, quantity: Quantity)
  def delete(userId: UserId): F[Unit]
  def get(userId: UserId): F[CartTotal]
  def removeItem(userId: UserId, itemId: ItemId): F[Unit]
  def update(userId: UserId, cart: Cart): F[Unit]

}

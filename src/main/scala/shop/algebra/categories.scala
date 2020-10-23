package shop.algebra

import shop.domain.category.{Category, CategoryName}

trait Categories[F[_]] {

  def findAll: F[List[Category]]
  def create(name: CategoryName): F[Unit]

}

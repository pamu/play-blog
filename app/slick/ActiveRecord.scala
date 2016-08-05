package slick

import slick.dbio.DBIO

import scala.concurrent.ExecutionContext


abstract class ActiveRecord[R <: CrudActions](val repo: R) {
  def model: repo.Model
  def save()(implicit ec: ExecutionContext): DBIO[repo.Model] = repo.save(model)
  def update()(implicit ec: ExecutionContext): DBIO[repo.Model] = repo.update(model)
  def delete()(implicit ec: ExecutionContext): DBIO[Int] = repo.delete(model)
}

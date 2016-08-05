package slick

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext

trait EntityActionsLike extends CrudActions {
  val dbConfig: DatabaseConfig[JdbcProfile]
  import dbConfig.driver.api._

  type Entity

  type Id

  type Model = Entity

  def insert(entity: Entity)(implicit ec: ExecutionContext): DBIO[Id]

  def deleteById(id: Id)(implicit ec: ExecutionContext): DBIO[Int]

  def findById(id: Id)(implicit ec: ExecutionContext): DBIO[Entity]

  def findOptionById(id: Id)(implicit ec: ExecutionContext): DBIO[Option[Entity]]
}

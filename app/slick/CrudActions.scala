package slick

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext


trait CrudActions {
  val dbConfig: DatabaseConfig[JdbcProfile]
  import dbConfig.driver.api._

  type Model

  def count: DBIO[Int]

  def save(model: Model)(implicit ec: ExecutionContext): DBIO[Model]

  def update(model: Model)(implicit ec: ExecutionContext): DBIO[Model]

  def delete(model: Model)(implicit ec: ExecutionContext): DBIO[Int]

  def fetchAll(fetchSize: Int = 100)(implicit ec: ExecutionContext): StreamingDBIO[Seq[Model], Model]
}

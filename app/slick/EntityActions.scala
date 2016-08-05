package slick

import slick.ast.BaseTypedType
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext

trait EntityActions extends EntityActionsLike {
  val dbConfig: DatabaseConfig[JdbcProfile]
  import dbConfig.driver.api._

  type EntityTable <: Table[Entity]

  def tableQuery: TableQuery[EntityTable]

  def $id(table: EntityTable): Rep[Id]

  def modelIdContract: ModelIdContract[Entity,Id]

  override def count: DBIO[Int] = tableQuery.size.result

  override def insert(entity: Entity)(implicit ec: ExecutionContext): DBIO[Id] = {
    tableQuery.returning(tableQuery.map($id(_))) += entity
  }

  override def deleteById(id: Id)(implicit ec: ExecutionContext): DBIO[Int] = {
    filterById(id).delete
  }

  override def findById(id: Id)(implicit ec: ExecutionContext): DBIO[Entity] = {
    filterById(id).result.head
  }

  override def findOptionById(id: Id)(implicit ec: ExecutionContext): DBIO[Option[Entity]] = {
    filterById(id).result.headOption
  }

  override def save(model: Entity)(implicit ec: ExecutionContext): DBIO[Entity] = {
    insert(model).flatMap { id =>
      filterById(id).result.head
    }.transactionally
  }

  override def update(model: Entity)(implicit ec: ExecutionContext): DBIO[Entity] = {
    filterById(modelIdContract.get(model)).update(model).map { _ => model }.transactionally
  }

  override def delete(model: Entity)(implicit ec: ExecutionContext): DBIO[Int] = {
    filterById(modelIdContract.get(model)).delete
  }

  override def fetchAll(fetchSize: Int)(implicit ec: ExecutionContext): StreamingDBIO[Seq[Entity], Entity] = {
    tableQuery.result.transactionally.withStatementParameters(fetchSize = fetchSize)
  }

  def filterById(id: Id) = tableQuery.filter($id(_) === id)

  def baseTypedType: BaseTypedType[Id]

  protected implicit lazy val btt: BaseTypedType[Id] = baseTypedType

}

package services.repos

import services.ids._
import com.google.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import services.models.TypeTagName
import slick.driver.JdbcProfile

case class TypeTag(name: TypeTagName, id: Option[TypeTagId] = None)

@Singleton
class TypeTagRepo @Inject() (dbaseConfigProvider: DatabaseConfigProvider) extends Mappings {
  val dbConfig = dbaseConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._
  val typeTags = TableQuery[TypeTags]

  def findByName(name: TypeTagName): DBIO[Option[TypeTag]] = {
    typeTags.filter(_.name === name).result.headOption
  }

  class TypeTags(tag: Tag) extends Table[TypeTag](tag, TypeTagTable.name) {
    def name = column[TypeTagName]("type_tag_name")
    def id = column[TypeTagId]("type_tag_id", O.PrimaryKey, O.AutoInc)
    def * = (name, id.?) <> (TypeTag.tupled, TypeTag.unapply)
    def nameIndex = index("type_tag_name_index", name, true)
  }
}

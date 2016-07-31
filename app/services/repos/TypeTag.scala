package services.repos

import services.ids._
import com.google.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import services.models.TypeTagName
import slick.driver.JdbcProfile

case class TypeTag(name: TypeTagName, creator: UserId, id: Option[TypeTagId] = None)

@Singleton
class TypeTagRepo @Inject() (dbaseConfigProvider: DatabaseConfigProvider,
                             val usersRepo: UsersRepo) extends Mappings {
  val dbConfig = dbaseConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._

  private[services] val typeTags = TableQuery[TypeTags]

  def findByName(name: TypeTagName): DBIO[Option[TypeTag]] = {
    typeTags.filter(_.name === name).result.headOption
  }

  private[services] class TypeTags(tag: Tag) extends Table[TypeTag](tag, TypeTagTable.name) {
    def name = column[TypeTagName]("type_tag_name")
    def creator = column[UserId]("creator")
    def id = column[TypeTagId]("type_tag_id", O.PrimaryKey, O.AutoInc)
    def * = (name, creator, id.?) <> (TypeTag.tupled, TypeTag.unapply)
    def nameIndex = index("type_tag_name_index", name, true)
    def creatorFK = foreignKey("type_tags_user_id_fk", creator, usersRepo.users)(_.id)
  }
}

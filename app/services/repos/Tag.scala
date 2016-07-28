package models.repos

import services.ids._
import com.google.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

case class TypeTag(name: String, id: Option[TypeTagId] = None)

@Singleton
class TypeTagRepo @Inject() (dbaseConfigProvider: DatabaseConfigProvider) extends Mappings {
  val dbConfig = dbaseConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._
}

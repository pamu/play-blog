package models.repos

import com.google.inject.{Inject, Singleton}
import models.ids.TypeTagId
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

case class TypeTag(name: String, id: Option[TypeTagId] = None)

@Singleton
class TypeTagRepo @Inject() (dbaseConfigProvider: DatabaseConfigProvider) extends Mappings {
  val dbConfig = dbaseConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._
}

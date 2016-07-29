package services.repos

import com.google.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import services.models.{GoogleId, UserInfo}
import slick.driver.JdbcProfile

@Singleton
class UserInfoRepo @Inject() (databaseConfigProvider: DatabaseConfigProvider) extends Mappings {
  val dbConfig = databaseConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._
  val db = dbConfig.db

  class UserInfos(tag: Tag) extends Table[UserInfo](tag, UserInfoTable.name) {
    def googleId = column[GoogleId]("google_id")
    def * = ???
  }
}

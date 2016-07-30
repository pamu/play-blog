package services.repos

import com.google.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import services.models._
import slick.driver.JdbcProfile

@Singleton
class UserInfoRepo @Inject() (databaseConfigProvider: DatabaseConfigProvider) extends Mappings {
  val dbConfig = databaseConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._

  val userInfos = TableQuery[UserInfos]

  class UserInfos(tag: Tag) extends Table[UserInfo](tag, UserInfoTable.name) {
    def googleId = column[GoogleId]("google_id")
    def email = column[Email]("email")
    def verifiedEmail = column[VerifiedEmail]("verified_email")
    def name = column[Name]("name")
    def givenName = column[GivenName]("given_name")
    def familyName = column[FamilyName]("family_name")
    def link = column[Link]("link")
    def picture = column[Picture]("picture")
    def gender = column[Gender]("gender")
    def * = (googleId, email, verifiedEmail, name, givenName, familyName, link, picture, gender) <> ((UserInfo.apply _).tupled , UserInfo.unapply)
  }
}

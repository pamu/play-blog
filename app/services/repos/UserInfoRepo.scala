package services.repos

import com.google.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import services.models._
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class UserInfoRepo @Inject()(databaseConfigProvider: DatabaseConfigProvider) extends Mappings {
  val dbConfig = databaseConfigProvider.get[JdbcProfile]

  import dbConfig.driver.api._

  val userInfos = TableQuery[UserInfos]

  def upsert(userInfo: UserInfo): DBIO[Int] = {
    val q = userInfos.filter(_.email === userInfo.email)
    val existsAction = q.exists.result
    val deleteAction = q.delete
    val insertAction = userInfos += userInfo
    for {
      exists <- existsAction
      result <- if (exists) {
        for {
          deleteStatus <- deleteAction
          insertStatus <- insertAction
        } yield insertStatus
      } else {
        insertAction
      }
    } yield result
  }

  class UserInfos(tag: Tag) extends Table[UserInfo](tag, UserInfoTable.name) {
    def googleId = column[GoogleId]("google_id")

    def email = column[Email]("email")

    def verifiedEmail = column[VerifiedEmail]("verified_email")

    def name = column[Name]("name")

    def givenName = column[GivenName]("given_name")

    def familyName = column[FamilyName]("family_name")

    def link = column[Option[Link]]("link")

    def picture = column[Picture]("picture")

    def gender = column[Option[Gender]]("gender")

    def hd = column[Option[Hd]]("hd")

    def * = (googleId, email, verifiedEmail, name, givenName, familyName, link, picture, gender, hd) <> ((UserInfo.apply _).tupled, UserInfo.unapply)

    def emailIndex = index("user_infos_email_index", email, true)
  }

}

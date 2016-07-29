package services.repos

import javax.inject.{Inject, Singleton}

import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import services.ids.UserId
import services.models.{Email, NickName, Source}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global

case class User(nickName: NickName,
                email: Email,
                source: Source.Value,
                createdAt: DateTime,
                id: UserId)

@Singleton
class UsersRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends Mappings {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._

  val users = TableQuery[Users]

  def exists(id: UserId): DBIO[Boolean] = {
    users.filter(_.id === id).exists.result
  }

  def findById(id: UserId): DBIO[Option[User]] = {
    for {
      optUser <- users.filter(_.id === id).result.headOption
    } yield optUser
  }

  class Users(tag: Tag) extends Table[User](tag, UsersTable.name) {
    def id = column[UserId]("USER_ID")
    def name = column[NickName]("NICK_NAME")
    def email = column[Email]("EMAIL")
    def source = column[Source.Value]("SOURCE")
    def createdAt = column[DateTime]("CREATED_AT")
    def * = (name, email, source, createdAt, id) <> (User.tupled, User.unapply)
    def emailIndex = index("email_index", email, true)
  }

}


package services.repos

import javax.inject.{Inject, Singleton}

import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import services.ids.UserId
import services.models.{Email, Source}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global

case class User(email: Email,
                source: Source.Value,
                createdAt: DateTime,
                id: UserId)

@Singleton
class UsersRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends Mappings {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig.driver.api._

  private[services] val users = TableQuery[Users]

  def exists(id: UserId): DBIO[Boolean] = {
    users.filter(_.id === id).exists.result
  }

  def findById(id: UserId): DBIO[Option[User]] = {
    for {
      optUser <- users.filter(_.id === id).result.headOption
    } yield optUser
  }

  def insert(user: User): DBIO[UserId]  = {
    for {
      exists <- users.filter(_.email === user.email).exists.result
      existingUser: User <- users.filter(_.email === user.email).result.head
      result <- if (exists) {
        DBIO.successful(existingUser.id)
      } else {
        for {
          _ <- users += user
          existingUser: User <- users.filter(_.email === user.email).result.head
          result <- DBIO.successful(existingUser.id)
        } yield result
      }
    } yield result
  }

  def getEmail(id: UserId): DBIO[Option[Email]] = {
    for {
      result <- users.filter(_.id === id).map(_.email).result.headOption
    } yield result
  }

  private[services] class Users(tag: Tag) extends Table[User](tag, UsersTable.name) {
    def id = column[UserId]("USER_ID", O.PrimaryKey)

    def email = column[Email]("EMAIL")

    def source = column[Source.Value]("SOURCE")

    def createdAt = column[DateTime]("CREATED_AT")

    def * = (email, source, createdAt, id) <> (User.tupled, User.unapply)

    def emailIndex = index("users_email_index", email, true)
  }

}


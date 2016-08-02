package services.repos

import javax.inject.{Inject, Singleton}

import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import services.ids.UserId
import services.models.{Email, ProfileName, Source}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global

case class User(profileName: ProfileName,
                email: Email,
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

  def insert(user: User): DBIO[UserId] = {
    for {
      optUser <- users.filter(_.email === user.email).result.headOption
      result <- optUser.map { user =>
        DBIO.successful(user.id)
      }.getOrElse {
        for {
          status <- users += user
          result <- DBIO.successful(user.id)
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
    def profileName = column[ProfileName]("profile_name")

    def id = column[UserId]("user_id", O.PrimaryKey)

    def email = column[Email]("email")

    def source = column[Source.Value]("source")

    def createdAt = column[DateTime]("created_at")

    def * = (profileName, email, source, createdAt, id) <> (User.tupled, User.unapply)

    def emailIndex = index("users_email_index", email, true)
  }

}


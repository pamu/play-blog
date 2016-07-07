package models

import java.sql.Timestamp
import javax.inject.{Inject, Singleton}

import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Created by pnagarjuna on 04/04/16.
  */
case class User(name: String, email: String, key: String, createdAt: DateTime, id: Option[Long] = None)

@Singleton
class UserRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends Mapping {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._
  val db = dbConfig.db
  private val users = TableQuery[UserTable]

  def findById(id: Long): Future[User] = {
    db.run(users.filter(_.id === id).result.head)
  }

  private class UserTable(tag: Tag) extends Table[User](tag, "USER") {
    def id = column[Long]("ID", O.AutoInc, O.PrimaryKey)
    def name = column[String]("NAME")
    def email = column[String]("EMAIL")
    def key = column[String]("KEY")
    def createdAt = column[DateTime]("CREATED_AT")
    def * = (name, email, key, createdAt, id.?) <> (User.tupled, User.unapply)
  }

}


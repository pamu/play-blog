package models.repos

import javax.inject.{Inject, Singleton}

import models.ids.Ids.{BlogPostId, UserId}
import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.Future

case class BlogPost(userId: UserId, title: String, summary: String, createdAt: DateTime,
                    id: Option[BlogPostId] = None)

@Singleton
class BlogPostRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                             val usersRepo: UsersRepo) extends Mapping {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._
  val db = dbConfig.db

  val blogPosts = TableQuery[BlogPostTable]

  def findById(id: Long): Future[Option[BlogPost]] = {
    db.run(blogPosts.filter(_.id === id).result.headOption)
  }

  class BlogPostTable(tag: Tag) extends Table[BlogPost](tag, BlogPostsTable.name) {
    def id = column[Long]("ID", O.AutoInc, O.PrimaryKey)
    def userId = column[Long]("USER_ID")
    def title = column[String]("TITLE")
    def summary = column[String]("SUMMARY")
    def createdAt = column[DateTime]("CREATED_AT")
    def * = (userId, title, summary, createdAt, id.?) <> (BlogPost.tupled, BlogPost.unapply)
    def userIdFk = foreignKey("BLOGPOST_USERID_FK", userId, usersRepo.users)(_.id)
  }
}

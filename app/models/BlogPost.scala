package models

import java.sql.Timestamp
import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Created by pnagarjuna on 05/04/16.
  */
case class BlogPost(userId: Long, title: String, summary: String, tags: String, createdAt: Timestamp, id: Option[Long] = None)

@Singleton
class BlogPostRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._
  val db = dbConfig.db
  private val blogPosts = TableQuery[BlogPostTable]

  def findById(id: Long): Future[BlogPost] = {
    db.run(blogPosts.filter(_.id === id).result.head)
  }

  private class BlogPostTable(tag: Tag) extends Table[BlogPost](tag, "BLOG_POSTS") {
    def id = column[Long]("ID", O.AutoInc, O.PrimaryKey)
    def userId = column[Long]("USER_ID")
    def title = column[String]("TITLE")
    def summary = column[String]("SUMMARY")
    def tags = column[String]("TAGS")
    def createdAt = column[Timestamp]("CREATED_AT")
    def * = (userId, title, summary, tags, createdAt, id.?) <> (BlogPost.tupled, BlogPost.unapply)
  }
}

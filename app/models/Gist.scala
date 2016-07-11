package models

import java.sql.Timestamp
import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

/**
  * Created by pnagarjuna on 11/04/16.
  */
case class Gist(blogPostId: Long, title: Option[String], summary: Option[String], githubId: String, createdAt: Timestamp, id: Option[Long] = None)

@Singleton
class GistRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                         val blogPostRepo: BlogPostRepo) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._
  val db = dbConfig.db
  val gists = TableQuery[GistTable]


  class GistTable(tag: Tag) extends Table[Gist](tag, GistsTable.name) {
    def blogPostId = column[Long]("BLOG_POST_ID")
    def title = column[Option[String]]("TITLE")
    def githubId = column[String]("GITHUB_ID")
    def summary = column[Option[String]]("Summary")
    def createdAt = column[Timestamp]("CREATED_AT")
    def id = column[Long]("ID")
    def * = (blogPostId, title, summary, githubId, createdAt, id.?) <> (Gist.tupled, Gist.unapply)
    def blogPostIdFk = foreignKey("blogpost_gist_fk", blogPostId, blogPostRepo.blogPosts)(_.id)
  }

}
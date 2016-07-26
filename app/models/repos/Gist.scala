package models.repos

import javax.inject.{Inject, Singleton}

import models.ids.Ids.{GistId, GithubId}
import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

case class Gist(blogPostId: Long, title: Option[String], summary: Option[String], githubId: GithubId,
                createdAt: DateTime, id: Option[GistId] = None)

@Singleton
class GistRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                         val blogPostRepo: BlogPostRepo) extends Mapping {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._
  val db = dbConfig.db
  val gists = TableQuery[GistTable]

  class GistTable(tag: Tag) extends Table[Gist](tag, GistsTable.name) {
    def blogPostId = column[Long]("BLOG_POST_ID")
    def title = column[Option[String]]("TITLE")
    def githubId = column[String]("GITHUB_ID")
    def summary = column[Option[String]]("Summary")
    def createdAt = column[DateTime]("CREATED_AT")
    def id = column[Long]("ID")
    def * = (blogPostId, title, summary, githubId, createdAt, id.?) <> (Gist.tupled, Gist.unapply)
    def blogPostIdFk = foreignKey("BLOGPOST_GIST_FK", blogPostId, blogPostRepo.blogPosts)(_.id)
  }

}
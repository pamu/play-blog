package models.repos

import javax.inject.{Inject, Singleton}

import models.ids.{BlogPostId, GistId, GithubId}
import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

case class Gist(blogPostId: BlogPostId, title: Option[String], summary: Option[String], githubId: GithubId,
                createdAt: DateTime, id: Option[GistId] = None)

@Singleton
class GistRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                         val blogPostRepo: BlogPostRepo) extends Mappings {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._
  val db = dbConfig.db
  val gists = TableQuery[GistTable]

  class GistTable(tag: Tag) extends Table[Gist](tag, GistsTable.name) {
    def blogPostId = column[BlogPostId]("BLOG_POST_ID")
    def title = column[Option[String]]("TITLE")
    def summary = column[Option[String]]("Summary")
    def githubId = column[GithubId]("GITHUB_ID")
    def createdAt = column[DateTime]("CREATED_AT")
    def id = column[GistId]("ID")
    def * = (blogPostId, title, summary, githubId, createdAt, id.?) <> (Gist.tupled, Gist.unapply)
    def blogPostIdFk = foreignKey("BLOGPOST_GIST_FK", blogPostId, blogPostRepo.blogPosts)(_.id)
  }

}
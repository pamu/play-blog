package models

import com.google.inject.{Inject, Singleton}
import models.exceptions.TableNameNotFoundException
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.jdbc.meta.MTable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration

abstract class TableName(val name: String)

case object BlogPostsTable extends TableName("blog_posts")

case object GistsTable extends TableName("gists")

case object UsersTable extends TableName("users")

@Singleton
class Tables @Inject()(val dbConfigProvider: DatabaseConfigProvider,
                       val gistRepo: GistRepo,
                       val blogPostRepo: BlogPostRepo,
                       val usersRepo: UsersRepo) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  val tables = List(
    UsersTable.name -> usersRepo.users,
    GistsTable.name -> gistRepo.gists,
    BlogPostsTable.name -> blogPostRepo.blogPosts
  )

  def generateSchema: Unit = {
    import dbConfig.driver.api._
    val db = dbConfig.db
    val actions =
      tables.map { case (name, table) =>
        MTable.getTables(name).headOption.flatMap {
          case Some(table) => DBIO.successful(table.name)
          case None => DBIO.failed(TableNameNotFoundException)
        }.flatMap { _ =>
          val action: DBIO[Unit] = table.schema.create
          action
        }.transactionally
      }
    val f = db.run(DBIO.sequence(actions).transactionally)
    Await.result(f, Duration.Inf)
  }

}

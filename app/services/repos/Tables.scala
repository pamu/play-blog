package services.repos

import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.jdbc.meta.MTable

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

abstract class TableName(val name: String)

case object BlogPostsTable extends TableName("blog_posts")

case object GistsTable extends TableName("gists")

case object UsersTable extends TableName("users")

case object UserInfoTable extends TableName("user_info_table")

@Singleton
class Tables @Inject()(dbConfigProvider: DatabaseConfigProvider,
                       gistRepo: GistRepo,
                       blogPostRepo: BlogPostRepo,
                       usersRepo: UsersRepo,
                       userInfoRepo: UserInfoRepo) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  private val tables = List(
    UserInfoTable.name -> userInfoRepo.userInfos,
    UsersTable.name -> usersRepo.users,
    BlogPostsTable.name -> blogPostRepo.blogPosts,
    GistsTable.name -> gistRepo.gists)

  def createTables(): Unit = {
    import dbConfig.driver.api._
    val db = dbConfig.db
    val actions =
      tables.map { case (name, table) =>
        MTable.getTables(name).headOption.map {
          case Some(table) =>
            Logger.info(s"sql table ${table.name} exists.")
            true
          case None =>
            Logger.info(s"""sql table ${name} doesn't exist.""")
            false
        }.flatMap { exists =>
          if (! exists) {
            val action: DBIO[Unit] = table.schema.create
            Logger.info(s"""creating table ${name}.""")
            action
          } else DBIO.successful(0)
        }.transactionally
      }
    val f = db.run(DBIO.sequence(actions).transactionally)
    Await.result(f, Duration.Inf)
  }

}

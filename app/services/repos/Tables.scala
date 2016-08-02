package services.repos

import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.jdbc.meta.MTable

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

abstract class TableName(val name: String)

case object BlogPostsTable extends TableName("blog_posts")

case object GistsTable extends TableName("gists")

case object UsersTable extends TableName("users")

case object UserInfoTable extends TableName("user_info_table")

case object TypeTagTable extends TableName("type_tag_table")

case object BlogPostTagsTable extends TableName("blog_post_tags_table")

@Singleton
class Tables @Inject()(dbConfigProvider: DatabaseConfigProvider,
                       gistRepo: GistRepo,
                       blogPostRepo: BlogPostRepo,
                       usersRepo: UsersRepo,
                       userInfoRepo: UserInfoRepo,
                       typeTagRepo: TypeTagRepo,
                       blogPostTagsRepo: BlogPostTagsRepo) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  private val tables = List(
    UserInfoTable.name -> userInfoRepo.userInfos,
    UsersTable.name -> usersRepo.users,
    BlogPostsTable.name -> blogPostRepo.blogPosts,
    TypeTagTable.name -> typeTagRepo.typeTags,
    BlogPostTagsTable.name -> blogPostTagsRepo.blogPostTags,
    GistsTable.name -> gistRepo.gists)

  def createTables(): Unit = {
    val f: Future[_] = createTablesFuture()
    Await.result(f, Duration.Inf)
  }

  def createTablesFuture(): Future[_] = {
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
          if (!exists) {
            val action: DBIO[Unit] = table.schema.create
            Logger.info(s"""creating table ${name}.""")
            action
          } else DBIO.successful(Unit)
        }.transactionally
      }
    val f: Future[_] = db.run(DBIO.sequence(actions).transactionally)
    f
  }

  def dropTablesFuture(): Future[_] = {
    import dbConfig.driver.api._
    val db = dbConfig.db

    val actions =
      tables.reverse.map { case (name, table) =>
        MTable.getTables(name).headOption.map {
          case Some(table) => true
          case None => false
        }.flatMap { exists =>
          if (exists) {
            val dBIO: DBIO[Unit] = table.schema.drop
            dBIO
          } else DBIO.successful(Unit)
        }
      }

    val f = db.run(DBIO.sequence(actions).transactionally)
    f
  }

}

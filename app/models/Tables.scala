package models

import com.google.inject.{Inject, Singleton}
import slick.lifted.TableQuery

/**
  * Created by pnagarjuna on 08/07/16.
  */
abstract class TableName(val name: String)

case object BlogPostsTable extends TableName("blog_posts")

case object GistsTable extends TableName("gists")

case object UsersTable extends TableName("users")

@Singleton
class Tables @Inject() (val gistRepo: GistRepo,
                        val blogPostRepo: BlogPostRepo,
                        val usersRepo: UsersRepo) {

//  val dbConfig = dbConfigProvider.get[JdbcProfile]
//  import dbConfig.driver.api._

  val tables: Map[String, TableQuery[_]] = Map(
    UsersTable.name -> usersRepo.users,
    GistsTable.name -> gistRepo.gists,
    BlogPostsTable.name -> blogPostRepo.blogPosts
  )


}

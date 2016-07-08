package models

import com.google.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

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
  val tables = Map(
    UsersTable.name -> usersRepo.users,
    GistsTable.name -> gistRepo.gists,
    BlogPostsTable.name -> blogPostRepo.blogPosts
  )


}

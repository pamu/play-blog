package services.repos

import com.google.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import services.ids.{BlogPostId, BlogPostTagId, TypeTagId}
import slick.driver.JdbcProfile

case class BlogPostTag(blogPostId: BlogPostId, typeTagId: TypeTagId, id: Option[BlogPostTagId] = None)

@Singleton
class BlogPostTagsRepo @Inject() (dbConfigProvider: DatabaseConfigProvider,
                                  val blogPostRepo: BlogPostRepo,
                                  val typeTagRepo: TypeTagRepo) extends Mappings {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._

  private[services] val blogPostTags = TableQuery[BlogPostTags]

  private[services] class BlogPostTags(tag: Tag) extends Table[BlogPostTag](tag, BlogPostTagsTable.name) {
    def blogPostId = column[BlogPostId]("blog_post_id")
    def typeTagId = column[TypeTagId]("type_tag_id")
    def id = column[BlogPostTagId]("id", O.PrimaryKey, O.AutoInc)
    def * = (blogPostId, typeTagId, id.?) <> (BlogPostTag.tupled, BlogPostTag.unapply)
    def blogPostIdFK = foreignKey("blog_post_tags_blog_post_id", blogPostId, blogPostRepo.blogPosts)(_.id)
    def typeTagIdFK = foreignKey("type_tag_id_fk", typeTagId, typeTagRepo.typeTags)(_.id)
  }
}

package models.repos

import java.sql.Timestamp

import models.ids._
import org.joda.time.DateTime
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

trait Mappings {
  val dbConfig: DatabaseConfig[JdbcProfile]
  import dbConfig.driver.api._

  implicit def dateTimeMapping = MappedColumnType.base[DateTime, Timestamp](
    {t: DateTime => new Timestamp(t.getMillis)}, {d: Timestamp => new DateTime(d.getTime)})

  implicit def userIdMapping = MappedColumnType.base[UserId, Long](_.id, UserId(_))

  implicit def tagIdMapping = MappedColumnType.base[TypeTagId, Long](_.id, TypeTagId(_))

  implicit def blogPostIdMapping = MappedColumnType.base[BlogPostId, Long](_.id, BlogPostId(_))

  implicit def gistIdMapping = MappedColumnType.base[GistId, Long](_.id, GistId(_))

  implicit def githubIdMapping = MappedColumnType.base[GithubId, String](_.id, GithubId(_))

}

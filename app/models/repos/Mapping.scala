package models.repos

import java.sql.Timestamp

import org.joda.time.DateTime
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

trait Mapping {
  val dbConfig: DatabaseConfig[JdbcProfile]

  import dbConfig.driver.api._

  implicit val dateTimeMapping = MappedColumnType.base[DateTime, Timestamp](
    {t: DateTime => new Timestamp(t.getMillis)}, {d: Timestamp => new DateTime(d.getTime)})
}

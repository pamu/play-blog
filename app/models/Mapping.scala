package models

import java.sql.Timestamp

import org.joda.time.DateTime
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

/**
  * Created by pnagarjuna on 07/07/16.
  */
trait Mapping {
  val dbConfig: DatabaseConfig[JdbcProfile]

  import dbConfig.driver.api._

  implicit val dateTimeMapping = MappedColumnType.base[DateTime, Timestamp](
    {t: DateTime => new Timestamp(t.getMillis)}, {d: Timestamp => new DateTime(d.getTime)})
}

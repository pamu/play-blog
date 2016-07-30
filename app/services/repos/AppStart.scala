package models.repos

import com.google.inject.{Inject, Singleton}
import play.api.Logger
import services.repos.Tables


@Singleton
class AppStart @Inject() (tables: Tables) {
  tables.createTables()
  Logger.info("tables created")
}

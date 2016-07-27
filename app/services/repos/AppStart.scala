package models.repos

import com.google.inject.{AbstractModule, Inject, Singleton}
import play.api.Logger


@Singleton
class AppStart @Inject() (tables: Tables) extends AbstractModule {
  override def configure(): Unit = {
    tables.createTables()
    Logger.info("tables created")
  }
}
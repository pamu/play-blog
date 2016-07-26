package models.repos

import com.google.inject.{AbstractModule, Inject, Singleton}


@Singleton
class AppStart @Inject() (tables: Tables) extends AbstractModule {
  override def configure(): Unit = {
    tables.createTables()
  }
}

package controllers

import com.google.inject.{Inject, Singleton}
import play.api.mvc.{Action, Controller}
import services.repos.Tables

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class AdminController @Inject() (tables: Tables) extends Controller {

  def dropTables = Action.async { req =>
    tables.dropTablesFuture().map { result =>
      Ok("tables dropped.")
    }.recover { case th =>
      Ok(s"dropping tables failed with message: ${th.getMessage}")
    }
  }

  def createTables = Action.async {req =>
    tables.dropTablesFuture().map { result =>
      Ok("tables created")
    }.recover { case th =>
      Ok(s"""table creation failed with message: ${th.getMessage}""")
    }
  }

}

package controllers

import com.google.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.OAuthServices
import services.models.State

import scala.concurrent.ExecutionContext.Implicits.global

class Auth @Inject() (oAuthServices: OAuthServices) extends Controller {

  def login = Action.async { implicit req =>
    oAuthServices.auth(State(Json.obj("msg" -> "hello"))).map { _ =>
      Ok("done")
    }
    //Future.successful(Ok(views.html.login()))
  }

}

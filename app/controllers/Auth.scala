package controllers

import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

class Auth extends Controller {

  def login = Action.async { implicit req =>
    Future.successful(Ok(views.html.login()))
  }

}

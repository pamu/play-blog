package controllers

import play.api.Logger
import services.ids._
import play.api.mvc._
import services.UserServices
import services.repos.User

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait UserServicesProvider {
  val userServices: UserServices
}

trait Secured {
  self: Controller with UserServicesProvider =>

  def id(requestHeader: RequestHeader): Option[String] = {
    val opt = requestHeader.session.get("id")
    Logger.info(s"""id: $opt""")
    opt
  }

  def onUnauthorized(requestHeader: RequestHeader) = {
    Logger.info("on unauthorised called")
    Results.Redirect(routes.AuthController.login).withNewSession
  }

  def withAuth[A](p: BodyParser[A])(f: => String => Request[A] => Future[Result]) =
    Security.Authenticated(id, onUnauthorized) { id =>
      Action.async(p) { request => f(id)(request) }
    }

  def withUser[A](p: BodyParser[A])(f: User => Request[A] => Future[Result]) = withAuth(p) { id => request =>
    userServices.findUserById(UserId(id)).flatMap { user => f(user)(request) }
      .recover { case th =>
        th.printStackTrace()
        Logger.error(s"finding the user byi id $id failed.")
        Results.Redirect(routes.AuthController.login).withNewSession
      }
  }

}

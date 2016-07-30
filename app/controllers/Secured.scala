package controllers

import services.ids._
import play.api.mvc._
import services.UserServices
import services.repos.User

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

trait UserServicesProvider {
  val userServices: UserServices
}

trait Secured {
  self: UserServicesProvider =>

  def id(requestHeader: RequestHeader): Option[String] = {
    Try(requestHeader.session.get("id")) match {
      case Success(optId) => optId
      case Failure(th) => None
    }
  }

  def onUnauthorized(requestHeader: RequestHeader) = Results.Redirect(routes.Auth.login)

  def withAuth[A](p: BodyParser[A])(f: => String => Request[A] => Future[Result]) =
    Security.Authenticated(id, onUnauthorized) { id =>
      Action.async(p) { request => f(id)(request) }
    }

  def withUser[A](p: BodyParser[A])(f: User => Request[A] => Future[Result]) = withAuth(p) { id => request =>
    userServices.findUserById(UserId(id)).flatMap { user => f(user)(request) }
      .recover { case th => Results.Redirect(routes.Auth.login).withNewSession }
  }

}

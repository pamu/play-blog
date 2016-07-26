package controllers

import models.ids.UserId
import play.api.mvc._
import models.repos.{User, UsersRepo}

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

trait UserRepoProvider {
  val userRepo: UsersRepo
}

trait Secured {
  self: UserRepoProvider =>

  def id(requestHeader: RequestHeader): Option[Long] = {
    Try(requestHeader.session.get("id").map(_.toLong)) match {
      case Success(optId) => optId
      case Failure(th) => None
    }
  }

  def onUnauthorized(requestHeader: RequestHeader) = Results.Redirect(routes.Auth.login).withNewSession

  def withAuth[A](p: BodyParser[A])(f: => Long => Request[A] => Future[Result]) =
    Security.Authenticated(id, onUnauthorized) { id =>
      Action.async(p) { request => f(id)(request) }
    }

  def withUser[A](p: BodyParser[A])(f: User => Request[A] => Future[Result]) = withAuth(p) { id => request =>
    userRepo.findById(UserId(id)).flatMap { user => f(user)(request) }
      .recover { case th => Results.Redirect(routes.Auth.login).withNewSession }
  }

}

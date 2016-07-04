package controllers

import models.{User, UserRepo}
import play.api.Logger
import play.api.mvc._

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
  * Created by pnagarjuna on 04/04/16.
  */
trait UserRepoProvider {
  val userRepo: UserRepo
}

trait Secured {
  self: UserRepoProvider =>

  def id(requestHeader: RequestHeader): Option[Long] = {
    Try (requestHeader.session.get("id").map(_.toLong)) match {
      case Success(optionId) => optionId
      case Failure(th) => {
        Logger.error(s"""${th.getMessage}""", th)
        None
      }
    }
  }

  def onUnauthorized(requestHeader: RequestHeader) = Results.Redirect(routes.Auth.login).withNewSession.flashing("success" -> "Please login.")

  def withAuth[A](p: BodyParser[A])(f: => Long => Request[A] => Future[Result]) = Security.Authenticated(id, onUnauthorized) { id => {
    Action.async(p){ request => f(id)(request) }
  }}

  def withUser[A](p: BodyParser[A])(f: User => Request[A] => Future[Result]) = withAuth(p) { id => request => {
    userRepo.findById(id).flatMap { user => f(user)(request)}.recover { case th => Results.Redirect(routes.Auth.login).withNewSession.flashing("success" -> "Please login.") }
  }}

}

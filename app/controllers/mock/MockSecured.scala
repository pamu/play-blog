package controllers.mock

import controllers.UserServicesProvider
import org.joda.time.{DateTime, DateTimeZone}
import play.api.mvc._
import services.ids.UserId
import services.models.{Email, ProfileName, Source}
import services.repos.User

import scala.concurrent.Future

trait MockSecured {
  self: Controller with UserServicesProvider =>
  def withUser[A](p: BodyParser[A])(f: User => Request[A] => Future[Result]) = Action.async(p) { req =>
    f(User(ProfileName("pamu"), Email("hello@gmail.com"), Source.Other, DateTime.now(DateTimeZone.UTC), UserId("pamu1234")))(req)
  }
}

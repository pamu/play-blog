package controllers

import com.google.inject.Inject
import play.api.mvc.Controller
import models._

import scala.concurrent.Future

class Application @Inject()(override val userRepo: UsersRepo) extends Controller
  with UserRepoProvider
  with Secured {

  def index = withUser(parse.anyContent) { user => req =>
    Future.successful(Ok(views.html.index()))
  }

}


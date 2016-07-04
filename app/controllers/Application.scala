package controllers

import com.google.inject.Inject
import models.UserRepo
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
  * Created by pnagarjuna on 24/03/16.
  */
class Application @Inject()(override val userRepo: UserRepo) extends Controller
  with UserRepoProvider
  with Secured {

  def index = Action.async { implicit req =>
    Future(Ok(views.html.index("Pure Code")(req.flash)))
  }

  def profile(name: String) = Action { implicit req =>
    Ok(views.html.me("Pure Code")(req.flash))
  }

  def publicProfile(name: String) = Action { implicit req =>
    Ok(views.html.publicProfile()(req.flash))
  }

  def profileId(id: String) = Action { implicit req =>
    Ok("")
  }

  def blogPost(heading: String) = Action { implicit req =>
    Ok("")
  }

  def createBlogPost() = Action.async(parse.json) { req =>
    Future(Ok(""))
  }

}


package controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

/**
  * Created by pnagarjuna on 04/04/16.
  */
class Auth extends Controller {

  def login = Action { implicit req =>
    Ok(views.html.login(loginForm)(req.flash))
  }

  val loginForm = Form(
    tuple(
      "email" -> email,
      "password" -> nonEmptyText(minLength = 6, maxLength = 20)
    )
  )

  def loginPost = Action.async { implicit req =>
    loginForm.bindFromRequest().fold(
      errors => Future(BadRequest(views.html.login(errors)(req.flash)).withNewSession.flashing("failure" -> "login failed")),
      data => Future(Ok("done"))
    )
  }

  def signUp = Action { implicit req =>
    Ok(views.html.signup("SignUp")(req.flash))
  }

  def forgotPassword = Action { implicit req =>
    Ok(views.html.forgotpassword("Forgot Password")(req.flash))
  }

}

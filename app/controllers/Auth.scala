package controllers

import com.google.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.OAuthServices
import services.models.RandomSecureString

import scala.concurrent.Future

class Auth @Inject()(oAuthServices: OAuthServices) extends Controller {

  def login = Action.async { implicit req =>
    Future.successful {
      Redirect(routes.Auth.oauth2(1))
    }
  }

  implicit class MapConverter(rMap: Map[String, String]) {
    def convert: List[String] = rMap.map(pair => s"${pair._1}=${pair._2}").toList
  }


  object GOAuthEndpoints {
    val goauthServiceURL = s"""https://accounts.google.com/o/oauth2/v2/auth"""
    val clientId = s"""995561758104-2civgrjum3kij0fhj1h836rppi4jqg04.apps.googleusercontent.com"""
    val clientSecret = s"""rA7Gu4LIuYC2dPYvTUQk0B9x"""
  }


  def oauth2(state: Long) = Action { req =>
    val random = RandomSecureString.getOne
    val params = Map[String, String](
      "response_type" -> "token",
      "client_id" -> s"${GOAuthEndpoints.clientId}",
      "nonce" -> s"${random.str}",
      "redirect_uri" -> "http://rxcode.herokuapp.com/oauth2callback",
      "scope" -> "email",
      "state" -> s"${state.toString()}"
    ).convert.mkString("?", "&", "").toString
    val requestURI = s"${GOAuthEndpoints.goauthServiceURL}${params}"

    Redirect(requestURI)
  }

}

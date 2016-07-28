package controllers

import com.google.inject.Inject
import play.api.mvc.{Action, Controller}
import services.OAuthServices
import services.endpoints.GOAuthEndpoints

import scala.concurrent.Future

class Auth @Inject()(oAuthServices: OAuthServices, gOAuthEndpoints: GOAuthEndpoints) extends Controller {

  def login = Action.async {
    Future.successful {
      Redirect(routes.Auth.oauth2(1))
    }
  }

  implicit class MapConverter(rMap: Map[String, String]) {
    def convert: List[String] = rMap.map(pair => s"${pair._1}=${pair._2}").toList
  }

  def oauth2(state: Long) = Action {
    val params = Map[String, String](
      "response_type" -> "token",
      "client_id" -> s"${gOAuthEndpoints.clientId}",
      "redirect_uri" -> "http://rxcode.herokuapp.com/oauth2callback",
      "scope" -> "email",
      "state" -> s"${state.toString()}"
    ).convert.mkString("?", "&", "").toString
    val requestURI = s"${gOAuthEndpoints.goauthServiceURL}${params}"

    Redirect(requestURI)
  }

  def oauth2callback() = Action {
    Ok(views.html.oauth2callback())
  }

  def oauth2callbackCleaned() = Action { req =>
    val qMap = req.queryString
    if (qMap.contains("access_token")) {
      //+ve flow
      Redirect(routes.Application.index).withSession("id" -> "some_id")
    } else {
      //-ve flow redirect to login screen
      Redirect(routes.Auth.login)
    }
  }

}

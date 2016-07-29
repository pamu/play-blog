package controllers

import com.google.inject.Inject
import play.api.mvc.{Action, Controller}
import services.{OAuthServices, Sha1Services, UserServices}
import services.endpoints.GOAuthEndpoints
import services.ids.UserId

import scala.concurrent.Future

class Auth @Inject()(oAuthServices: OAuthServices,
                     gOAuthEndpoints: GOAuthEndpoints,
                     sha1Services: Sha1Services,
                     userServices: UserServices) extends Controller {

  def login = Action.async { req =>
    val optId = req.headers.get("id")
    optId.map { id =>
      userServices.checkUserExists(UserId(id)).map { exists =>
        if (exists) Redirect(routes.Application.index)
        else {
          val key = sha1Services.sha1(System.nanoTime().toString)
          Redirect(routes.Auth.oauth2(key)).withNewSession
        }
      }
    }.getOrElse {
      val key = sha1Services.sha1(System.nanoTime().toString)
      Future.successful(Redirect(routes.Auth.oauth2(key)).withNewSession)
    }
  }

  implicit class MapConverter(rMap: Map[String, String]) {
    def convert: List[String] = rMap.map(pair => s"${pair._1}=${pair._2}").toList
  }

  def oauth2(state: String) = Action {
    val params = Map[String, String](
      "response_type" -> "token",
      "client_id" -> s"${gOAuthEndpoints.clientId}",
      "redirect_uri" -> "http://rxcode.herokuapp.com/oauth2callback",
      "scope" -> "email",
      "state" -> s"${state}"
    ).convert.mkString("?", "&", "").toString
    val requestURI = s"${gOAuthEndpoints.goauthServiceURL}${params}"

    Redirect(requestURI).withSession("state" -> state)
  }

  def oauth2callback() = Action {
    Ok(views.html.oauth2callback())
  }

  def oauth2callbackCleaned() = Action { req =>
    val qMap = req.queryString
    if (qMap.contains("access_token")) {
      Redirect(routes.Application.index)
    } else {
      Redirect(routes.Auth.login)
    }
  }

}

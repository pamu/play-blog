package controllers

import com.google.inject.Inject
import play.api.Logger
import play.api.mvc.{Action, Controller}
import services.{OAuthServices, Sha1Services, UserServices}
import services.endpoints.GOAuthEndpoints
import services.ids.UserId
import services.status.{AuthFailure, LoginSuccess, UnknownException}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

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

  def oauth2callbackCleaned() = Action.async { req =>
    val qMap = req.queryString
    val optReqState: Option[String] = qMap.get("state").flatMap {
      _.headOption
    }
    val opSessionSate: Option[String] = req.session.data.get("state")
    optReqState -> opSessionSate match {
      case (Some(rState), Some(sState)) =>
        if (rState == sState) {
          val accessToken = qMap.get("access_token").flatMap(_.headOption)
          oAuthServices.getUserInfo(accessToken.get).flatMap {
            case LoginSuccess(loginInfo) =>
              userServices.onBoardUser(loginInfo).map { userId: UserId =>
                Redirect(routes.Application.index).withSession("id" -> userId.id)
              }.recover {
                case th =>
                  th.printStackTrace
                  Logger.error(s"""routing to index page failed ${th.getMessage}""")
                  Redirect(routes.Auth.oops())
              }
            case AuthFailure => Future.successful(Redirect(routes.Auth.login()))
            case UnknownException(ex) => Future.successful(Redirect(routes.Auth.oops()))
          }
        } else {
          Future.successful(Redirect(routes.Auth.oops()))
        }
      case _ => Future.successful(Redirect(routes.Auth.oops()))
    }
  }

  def oops = Action { req =>
    Ok(views.html.oops())
  }
}

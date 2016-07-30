package services

import com.google.inject.{ImplementedBy, Inject, Singleton}
import org.apache.http.HttpStatus
import play.api.Logger
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.libs.ws.WSClient
import services.endpoints.GOAuthEndpoints
import services.exceptions.ParseException
import services.models.UserInfo
import services.status.{AuthFailure, LoginStatus, LoginSuccess}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@ImplementedBy(classOf[GOAuthServicesImpl])
trait OAuthServices {
  def getUserInfo(accessToken: String): Future[LoginStatus[UserInfo]]
}

@Singleton
class GOAuthServicesImpl @Inject()(gOAuthEndpoints: GOAuthEndpoints, wsClient: WSClient) extends OAuthServices {
  override def getUserInfo(accessToken: String): Future[LoginStatus[UserInfo]] = {
    wsClient.url(gOAuthEndpoints.userInfoURL(accessToken)).get().flatMap { wsResponse =>
      val status = wsResponse.status
      if (status == HttpStatus.SC_OK) {
        Logger.info(s"""response body as string: ${wsResponse.body}""")
        val payload = Json.parse(wsResponse.body)
        payload.validate[UserInfo] match {
          case JsSuccess(userInfo, _) => Future.successful(LoginSuccess(userInfo))
          case error@JsError(errors) => Future.failed(ParseException(s"Errors while parsing the json payload into user info object: ${errors.mkString(" ")}"))
        }
      } else if (status == HttpStatus.SC_UNAUTHORIZED) {
        Future.successful(AuthFailure)
      } else {
        Future.failed(new Exception("Unknown http status: " + status))
      }
    }
  }
}

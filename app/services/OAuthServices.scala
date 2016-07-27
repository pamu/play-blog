package services

import com.google.inject.{ImplementedBy, Inject, Singleton}
import play.api.libs.ws.WSClient
import services.endpoints.GOAuthEndpoints
import services.models.UserInfo

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@ImplementedBy(classOf[GOAuthServicesImpl])
trait OAuthServices {
  def getUserInfo(accessToken: String): Future[UserInfo]
}

@Singleton
class GOAuthServicesImpl @Inject()(gOAuthEndpoints: GOAuthEndpoints, wsClient: WSClient) extends OAuthServices {
  override def getUserInfo(accessToken: String): Future[UserInfo] = {
    wsClient.url(gOAuthEndpoints.userInfoURL(accessToken)).get().flatMap { wsResponse =>
      ???
    }
  }
}

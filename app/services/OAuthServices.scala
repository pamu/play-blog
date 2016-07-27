package services

import com.google.inject.{ImplementedBy, Inject, Singleton}
import play.api.libs.ws.WSClient
import services.models.UserInfo

import scala.concurrent.Future

@ImplementedBy(classOf[GOAuthServicesImpl])
trait OAuthServices {
  def getUserInfo(accessToken: String): Future[UserInfo]
}

@Singleton
class GOAuthServicesImpl @Inject()(wsClient: WSClient) extends OAuthServices {
  override def getUserInfo(accessToken: String): Future[UserInfo] = ???
}

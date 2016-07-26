package services

import com.google.inject.{ImplementedBy, Inject, Singleton}
import play.api.libs.ws.WSClient

import scala.concurrent.Future

@ImplementedBy(classOf[OAuthServicesImpl])
trait OAuthServices {
  def auth: Future[]
}

@Singleton
class OAuthServicesImpl @Inject() (wsClient: WSClient) extends OAuthServices {

}


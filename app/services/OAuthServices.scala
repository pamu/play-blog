package services

import com.google.inject.{ImplementedBy, Inject, Singleton}
import play.api.libs.ws.WSClient

@ImplementedBy(classOf[GOAuthServicesImpl])
trait OAuthServices {
}

@Singleton
class GOAuthServicesImpl @Inject()(wsClient: WSClient) extends OAuthServices {
}

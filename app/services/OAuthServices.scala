package services

import com.google.inject.{ImplementedBy, Inject, Singleton}
import org.apache.http.HttpStatus
import play.api.libs.ws.WSClient
import services.models.{RandomSecureString, State}
import services.statuses.OAuthProcessingDone

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@ImplementedBy(classOf[GOAuthServicesImpl])
trait OAuthServices {
  def auth(state: State): Future[_]
}

@Singleton
class GOAuthServicesImpl @Inject()(wsClient: WSClient) extends OAuthServices {
  override def auth(state: State): Future[_] = {
    val random = RandomSecureString.getOne
    wsClient.url(GOAuthEndpoints.goauthServiceURL).withQueryString(
      "response_type" -> "token",
      "client_id" -> GOAuthEndpoints.clientId,
      "nonce" -> random.str,
      "redirect_uri" -> "http://rxcode.herokuapp.com/oauth2callback",
      "scope" -> "email",
      "state" -> state.toString()
    ).get().flatMap { wsRes =>
      val status = wsRes.status
      if (wsRes.status == HttpStatus.SC_OK) {
        Future.successful {
          OAuthProcessingDone(Some(state))
        }
      } else Future.failed(new Exception(s"Bad status, status: $status"))
    }
  }
}

object GOAuthEndpoints {
  val goauthServiceURL = s"""https://accounts.google.com/o/oauth2/v2/auth"""
  val clientId = s"""995561758104-2civgrjum3kij0fhj1h836rppi4jqg04.apps.googleusercontent.com"""
  val clientSecret = s"""rA7Gu4LIuYC2dPYvTUQk0B9x"""
}

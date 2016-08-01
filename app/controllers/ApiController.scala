package controllers

import com.google.inject.{Inject, Singleton}
import controllers.mock.MockSecured
import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc.Controller
import services.UserServices
import services.models.UserInfo
import services.response.OKRes

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ApiController @Inject()(override val userServices: UserServices) extends Controller
  with UserServicesProvider
  with MockSecured {

  def profile = withUser(parse.anyContent) { user => req =>
    for {
      optUserInfo <- userServices.getUserInfo(user.email)
      writes: Writes[(UserInfo, String)] = new Writes[(UserInfo, String)] {
        override def writes(o: (UserInfo, String)): JsValue = {
          Json.obj("user_info" -> o._1, "user_id" -> o._2)
        }
      }
      result <- Future.successful(Ok(OKRes("success", optUserInfo.map((_, user.id.id))).toJsonOK(writes)))
    } yield result
  }

}

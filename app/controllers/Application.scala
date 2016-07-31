package controllers

import com.google.inject.Inject
import controllers.mock.MockSecured
import play.api.Logger
import play.api.mvc.Controller
import services.UserServices

import scala.concurrent.Future

class Application @Inject()(override val userServices: UserServices) extends Controller
  with UserServicesProvider
  with MockSecured {

  def index = withUser(parse.anyContent) { user => req =>
    Logger.info("going to index")
    Future.successful(Ok(views.html.index()))
  }

}


package controllers

import com.google.inject.Inject
import play.api.Logger
import play.api.mvc.Controller
import services.UserServices

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Application @Inject()(override val userServices: UserServices) extends Controller
  with UserServicesProvider
  with Secured {

  def index = withUser(parse.anyContent) { user => req =>
    Logger.info("going to index")
    //Future.successful(Ok(views.html.index()))
    Future.successful(Redirect(routes.Application.profile))
  }

  def profile = withUser(parse.anyContent) { user => req =>
    Logger.info("profile action")
    userServices.getUserInfo(user.email).flatMap {
      case Some(info) => Future.successful(Ok(views.html.profile(info.picture.link)))
      case None => Future.successful(Ok(views.html.profile("https://s-media-cache-ak0.pinimg.com/236x/54/bf/a9/54bfa975abbc85378a31fa36f6f7c806.jpg")))
    }
  }

}


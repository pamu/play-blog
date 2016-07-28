package services

import java.security.MessageDigest

import com.google.inject.{ImplementedBy, Inject, Singleton}
import services.ids.UserId
import services.models.Name

import scala.concurrent.Future


@ImplementedBy(classOf[UserServicesImpl])
trait UserServices {
  def generateUserId(name: Name): Future[UserId]
}

@Singleton
class UserServicesImpl @Inject()() extends UserServices {
  override def generateUserId(name: Name): Future[UserId] = {
    Future.successful {
      val crypt = MessageDigest.getInstance("SHA-1")
      crypt.reset()
      crypt.update(name.nameStr.getBytes("UTF-8"))
      UserId(s"""${new String(crypt.digest())}${System.nanoTime()}""")
    }
  }
}

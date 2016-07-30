package services

import java.security.MessageDigest

import com.google.inject.{ImplementedBy, Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import services.exceptions.NoEntityFoundException
import services.ids.UserId
import services.models.Name
import services.repos.{User, UsersRepo}
import slick.driver.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


@ImplementedBy(classOf[UserServicesImpl])
trait UserServices {
  def generateUserId(name: Name): Future[UserId]

  def checkUserExists(id: UserId): Future[Boolean]

  def findUserById(id: UserId): Future[User]
}

@Singleton
class UserServicesImpl @Inject()(databaseConfigProvider: DatabaseConfigProvider,
                                 usersRepo: UsersRepo) extends UserServices {
  val dbConfig = databaseConfigProvider.get[JdbcProfile]
  val db = dbConfig.db

  override def generateUserId(name: Name): Future[UserId] = {
    Future.successful {
      val crypt = MessageDigest.getInstance("SHA-1")
      crypt.reset()
      crypt.update(name.nameStr.getBytes("UTF-8"))
      UserId(s"""${new String(crypt.digest())}${System.nanoTime()}""")
    }
  }

  override def checkUserExists(id: UserId): Future[Boolean] = {
    db.run(usersRepo.exists(id))
  }

  override def findUserById(id: UserId): Future[User] = {
    db.run {
      for {
        optUser <- usersRepo.findById(id)
      } yield optUser
    }.flatMap {
      case Some(user) => Future.successful(user)
      case None => Future.failed(NoEntityFoundException("User with given id not found"))
    }
  }

}

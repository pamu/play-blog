package services

import java.security.MessageDigest

import com.google.inject.{ImplementedBy, Inject, Singleton}

@ImplementedBy(classOf[Sha1ServicesImpl])
trait Sha1Services {
  def sha1(msg: String): String
}

@Singleton
class Sha1ServicesImpl @Inject() () extends Sha1Services {
  override def sha1(msg: String): String = {
    val crypt = MessageDigest.getInstance("SHA-1")
    crypt.reset()
    crypt.update(msg.getBytes("UTF-8"))
    String.valueOf(crypt.digest())
  }
}

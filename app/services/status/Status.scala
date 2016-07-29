package services.status

sealed trait LoginStatus[+T]

case class LoginSuccess[+T](loginInfo: T) extends LoginStatus[T]

case object AuthFailure extends LoginStatus[Nothing]

case class UnknownException(ex: Throwable) extends LoginStatus[Nothing]
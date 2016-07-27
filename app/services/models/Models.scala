package services.models

case class Email(emailStr: String)

case class UserInfo(email: Email)

case class NickName(nameStr: String)

object Source extends Enumeration {
  val GOOGLE, FACEBOOK, Other = Value
}
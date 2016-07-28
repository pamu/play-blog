package services.models

case class Email(emailStr: String)

case class Name(nameStr: String)

case class UserInfo(email: Email)

object UserInfo {

}

case class NickName(nameStr: String)

object Source extends Enumeration {
  val GOOGLE, FACEBOOK, Other = Value
}
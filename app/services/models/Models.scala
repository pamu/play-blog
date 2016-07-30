package services.models

import play.api.libs.json._

case class Email(emailStr: String)

case class Name(nameStr: String)

case class GoogleId(id: String)

case class VerifiedEmail(verified: Boolean)

case class GivenName(givenNameStr: String)

case class FamilyName(nameStr: String)

case class Link(link: String)

case class Picture(link: String)

case class Gender(gender: String)

case class UserInfo(googleId: GoogleId,
                    email: Email,
                    verifiedEmail: VerifiedEmail,
                    name: Name,
                    givenName: GivenName,
                    familyName: FamilyName,
                    link: Link,
                    picture: Picture,
                    gender: Gender)


object UserInfo {

  import play.api.libs.functional.syntax._

  implicit val reads: Reads[UserInfo] = {
    ((JsPath \ "id").read[String].map(GoogleId(_)) and
      (JsPath \ "email").read[String].map(Email(_)) and
      (JsPath \ "verified_email").read[Boolean].map(VerifiedEmail(_)) and
      (JsPath \ "name").read[String].map(Name(_)) and
      (JsPath \ "given_name").read[String].map(GivenName(_)) and
      (JsPath \ "family_name").read[String].map(FamilyName(_)) and
      (JsPath \ "link").read[String].map(Link(_)) and
      (JsPath \ "picture").read[String].map(Picture(_)) and
      (JsPath \ "gender").read[String].map(Gender(_))) {
      (id, email, verified_email, name, given_name, family_name, link, picture, gender) =>
        UserInfo(
          id,
          email,
          verified_email,
          name,
          given_name,
          family_name,
          link,
          picture,
          gender
        )
    }
  }

}


object Source extends Enumeration {
  val GOOGLE, FACEBOOK, Other = Value
}

case class LoginAttemptKey(key: String)
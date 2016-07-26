package services.models

import java.security.SecureRandom

import play.api.libs.json.JsValue

case class RandomSecureString(str: String)

object RandomSecureString {
  val secureRandom = new SecureRandom()
  def getOne: RandomSecureString = RandomSecureString(BigInt(130, secureRandom).toString(32))
}

case class State(json: JsValue) {
  override def toString() = json.toString()
}
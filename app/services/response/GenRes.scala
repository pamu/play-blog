package services.response

import play.api.libs.json.{JsValue, Json, Writes}

trait GenRes[T] {
  val msg: String
  val optPayload: Option[T]
  def toJsonOK(implicit payloadWrites: Writes[T]): JsValue = Json.obj("msg" -> msg, "payload" -> Json.toJson(optPayload))
}

case class OKRes[T](msg: String, optPayload: Option[T]) extends GenRes[T]


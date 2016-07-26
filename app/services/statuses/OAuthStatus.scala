package services.statuses

import services.models.{RandomSecureString, State}


sealed trait OAuthProcessingStatus {
  val optState: Option[State] = None
}

case class OAuthProcessingDone(override val optState: Option[State]) extends OAuthProcessingStatus

case object OAuthProcessingFailed extends OAuthProcessingStatus

case class OAuthProcessingSuccessful() extends OAuthProcessingStatus

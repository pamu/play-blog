package models

import com.google.inject.{Inject, Singleton}
import models.ids.Ids.TagId

case class Tag(name: String, id: Option[TagId] = None)

@Singleton
class TagRepo @Inject() () {

}

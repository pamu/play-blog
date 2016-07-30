package services.exceptions

case class ParseException(msg: String) extends Exception(msg)

case class NoEntityFoundException(msg: String) extends Exception(msg)


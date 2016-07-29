package services.exceptions

case object TableNameNotFoundException extends Exception("Table name not found")

case class ParseException(msg: String) extends Exception(msg)

case class NoEntityFoundException(msg: String) extends Exception(msg)


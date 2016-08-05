package slick

case class ModelIdContract[A, B](get: A => B, set: (A, B) => A)
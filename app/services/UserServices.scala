package services

import com.google.inject.{ImplementedBy, Inject, Singleton}

@ImplementedBy(classOf[UserServicesImpl])
trait UserServices {

}

@Singleton
class UserServicesImpl @Inject() () extends UserServices {

}

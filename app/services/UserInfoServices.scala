package services

import com.google.inject.{ImplementedBy, Inject, Singleton}

@ImplementedBy(classOf[UserInfoServicesImpl])
trait UserInfoServices {

}

@Singleton
class UserInfoServicesImpl @Inject() () extends UserInfoServices {

}
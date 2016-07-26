import com.google.inject.AbstractModule
import models.repos.AppStart


class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[AppStart]).asEagerSingleton()
  }

}



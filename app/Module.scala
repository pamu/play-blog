import com.google.inject.AbstractModule
import models.repos.AppStart
import play.api.{Configuration, Environment}


class Module(environment: Environment, configuration: Configuration) extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[AppStart]).asEagerSingleton()
  }

}



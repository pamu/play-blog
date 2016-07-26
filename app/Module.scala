import com.google.inject.AbstractModule
import models.repos.{AppStart, Tables}
import play.api.{Configuration, Environment}


class Module(environment: Environment, configuration: Configuration) extends AbstractModule {

  override def configure(): Unit = {
    //bind(classOf[Tables]).asEagerSingleton()
    //bind(classOf[AppStart]).asEagerSingleton()
  }

}



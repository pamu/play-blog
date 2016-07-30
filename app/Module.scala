import com.google.inject.{AbstractModule, Inject, Singleton}
import models.repos.AppStart
import play.api.{Configuration, Environment}

@Singleton
class Module @Inject() (environment: Environment, configuration: Configuration) extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[AppStart]).asEagerSingleton()
  }

}



import com.google.inject.{AbstractModule, Singleton}


class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[AppStart]).asEagerSingleton()
  }

}

@Singleton
class AppStart extends AbstractModule {
  override def configure(): Unit = {

  }
}

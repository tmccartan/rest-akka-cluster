package module

import actors.{MainActor, AkkaConfig, Init}
import akka.actor.{ActorRef, Props, ActorSystem}
import akka.cluster.Cluster
import com.google.inject.{Provides, AbstractModule}
import service.{SessionService, AddressService, PaymentMethodService}

class RuntimeModule extends AbstractModule {



  override def configure(): Unit = {

    bind(classOf[Init]).asEagerSingleton()
    bind(classOf[SessionService]).asEagerSingleton()
  }

  @Provides
  def providesAddressService :AddressService = {
    new AddressService()
  }
  @Provides
  def providesPaymentService :PaymentMethodService = {
    new PaymentMethodService()
  }
}

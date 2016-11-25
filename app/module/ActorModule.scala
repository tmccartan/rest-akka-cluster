package module

import actors.{AkkaConfig, MainActor}
import akka.actor.{ActorRef, PoisonPill, Props, ActorSystem}
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings}
import scaldi.Module
import service.{SessionService, PaymentMethodService, AddressService}

class ActorModule extends Module {
  bind[AddressService] toProvider new AddressService()
  bind[PaymentMethodService] toProvider new PaymentMethodService()
  bind[MainActor] to new MainActor(inject[PaymentMethodService], inject[AddressService])



  bind[ActorSystem] to {
    val actorSystem = ActorSystem("rest-akka-cluster", new AkkaConfig(None).config)

    actorSystem.actorOf(
      ClusterSingletonManager.props(
        singletonProps = Props(inject[MainActor]),
        terminationMessage =  PoisonPill,
        settings = ClusterSingletonManagerSettings(actorSystem).withSingletonName("MainActor")
      ), name = "singleton")

    actorSystem
  } destroyWith(_.terminate)

  binding identifiedBy "MainActorProxy" to {
    val system = inject[ActorSystem]

    system.actorOf(
      props = ClusterSingletonProxy.props(singletonManagerPath = "/user/singleton",
      settings = ClusterSingletonProxySettings(system).withSingletonName("MainActor")
    ), name = "main-proxy")
  }

  bind[SessionService] toProvider new SessionService(inject[ActorRef]("MainActorProxy"))
}

package actors


import akka.actor.{Props, ActorSystem}
import akka.cluster.Cluster
import com.google.inject.{Inject, Singleton}
import com.typesafe.config.ConfigFactory
import play.api.Play
import service.{AddressService, PaymentMethodService}

@Singleton
class Init @Inject()(paymentMethodService: PaymentMethodService, addressService: AddressService) {

  val clusterHost = sys.props.get("akka.remote.netty.tcp.hostname")
  val system = ActorSystem("scheduler", new AkkaConfig(clusterHost).config)
  val localAddress = Cluster(system).selfAddress

  val mainActor = system.actorOf(Props(new MainActor(this, paymentMethodService, addressService)), "mainActor")

  def init() {
    println("Init.init()")
  }

  def shutdown() {
    system.shutdown()
  }
}

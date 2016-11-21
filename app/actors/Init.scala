package actors


import akka.actor.{Props, ActorSystem}
import akka.cluster.Cluster
import com.google.inject.{Inject, Singleton}
import com.typesafe.config.ConfigFactory
import play.api.Play
import service.{AddressService, PaymentMethodService}

@Singleton
class InitImpl @Inject()(paymentMethodService: PaymentMethodService, addressService: AddressService) {

  val clusterHost = sys.props.get("akka.remote.netty.tcp.hostname")
  val system = ActorSystem("scheduler", new AkkaConfig(clusterHost).config)
  val localAddress = Cluster(system).selfAddress

  val mainActor = system.actorOf(Props(new MainActor(paymentMethodService, addressService)), "mainActor")

  def init() {
    println("Init.init()")
  }

  def shutdown() {
    system.terminate()
  }
}


object Init {
  private val paymentMethodService = new PaymentMethodService
  private val addressService = new AddressService

  private val _init = new InitImpl(paymentMethodService, addressService)
  val instance = _init
}
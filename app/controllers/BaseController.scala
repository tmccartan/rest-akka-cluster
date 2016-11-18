package controllers

import actors.AkkaConfig
import akka.actor.ActorSystem
import play.api.mvc.Controller

trait BaseController extends Controller{

  val clusterHost = sys.props.get("akka.remote.netty.tcp.hostname")
  val config = new AkkaConfig(clusterHost)
  val system = ActorSystem("akka-cluster-test", config.config)
}

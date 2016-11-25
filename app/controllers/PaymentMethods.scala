package controllers


import java.util.UUID
import actors.{SessionActorMessages, MainActorMessages}
import akka.actor.ActorRef
import com.gilt.akk.cluster.api.test.v0.models.PaymentMethod
import scala.concurrent.Future
import play.api.libs.json._
import com.gilt.akk.cluster.api.test.v0.models.json._

import play.api.mvc._
import akka.pattern.ask

class PaymentMethods (mainActor: ActorRef) extends BaseController {

  def get(uuidStr: String) = Action.async { request =>
    val uuid = UUID.fromString(uuidStr)
    for{
      actorRef <- ask(mainActor, MainActorMessages.FindActor(uuid)).mapTo[ActorRef]
      response <- ask(actorRef, SessionActorMessages.PaymentMethods(uuid)).mapTo[Future[Seq[PaymentMethod]]]
      paymentMethods <- response
    } yield {
      Ok(Json.toJson(paymentMethods))
    }
  }
}

package controllers

import java.util.UUID
import javax.inject.Singleton

import actors.{PaymentMethodsMessage, PaymentMethodActor}
import akka.actor.Props
import akka.util.Timeout
import com.gilt.akk.cluster.api.test.v0.models.PaymentMethod

import scala.concurrent.{Promise, Future}
import scala.concurrent.duration._

import com.google.inject.Inject
import play.api.libs.json._
import com.gilt.akk.cluster.api.test.v0.models.json._

import play.api.mvc._
import akka.pattern.ask

import service.PaymentMethodService
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class PaymentMethods @Inject() (paymentMethodService: PaymentMethodService) extends BaseController {

  implicit val timeout = Timeout(10.seconds)
  val paymentMethodRef = system.actorOf(Props(new PaymentMethodActor(paymentMethodService)), "paymentMethodActor")

  def get(uuid: String) = Action.async { request =>
    val res = ask(paymentMethodRef, PaymentMethodsMessage).mapTo[Seq[PaymentMethod]]

    res.map(paymentMethods => {
      Results.Ok(Json.toJson(paymentMethods))
    })
  }
}

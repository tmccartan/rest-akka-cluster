package controllers

import akka.util.Timeout
import play.api.mvc.Controller

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

trait BaseController extends Controller{
  implicit val ec: ExecutionContext = play.api.libs.concurrent.Execution.Implicits.defaultContext
  implicit val timeout = Timeout(30.seconds)
}

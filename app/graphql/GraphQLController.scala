package graphql

import controllers.BaseController
import play.api.libs.json.{Json, JsObject}
import play.api.mvc._
import sangria.execution.{ErrorWithResolver, QueryAnalysisError, Executor}
import sangria.parser.{SyntaxError, QueryParser}
import sangria.marshalling.playJson._
import service.{AddressService, PaymentMethodService, SessionService}

import scala.concurrent.Future
import scala.util.{Failure, Success}

class GraphQLController (sessionService: SessionService, paymentMethodService: PaymentMethodService, addressService: AddressService) extends BaseController {

  def graphql(query: Option[String], variables: Option[String], operation: Option[String]) = Action.async {
    implicit request => {

      val resolvedQuery = request.method match {
        case "POST" => request.body.asText.get
        case "GET" => query.get
        case _ => "" //let it throw the syntax error
      }
      println(s"Recieved graph ql query = $resolvedQuery method ${request.method}" )
      executeQuery(resolvedQuery, variables map parseVariables, operation)
  }}

  private def parseVariables(variables: String) =
    if (variables.trim == "" || variables.trim == "null") Json.obj() else Json.parse(variables).as[JsObject]


  private def executeQuery(query: String, variables: Option[JsObject], operation: Option[String]) =

    QueryParser.parse(query) match {
      // query parsed successfully, time to execute it!
      case Success(queryAst) => Executor.execute (
        SchemaDefinition.CheckoutSchema, queryAst, SessionRepo(sessionService , paymentMethodService, addressService),
          operationName = operation
      ).map(Ok(_)).recover {
        case error: QueryAnalysisError ⇒ BadRequest(error.resolveError)
        case error: ErrorWithResolver ⇒ InternalServerError(error.resolveError)
      }


      // can't parse GraphQL query, return error
      case Failure(error: SyntaxError) ⇒
        Future.successful(BadRequest(Json.obj(
          "syntaxError" → error.getMessage,
          "locations" → Json.arr(Json.obj(
            "line" → error.originalError.position.line,
            "column" → error.originalError.position.column)))))

      case Failure(error) ⇒
        throw error
    }

}

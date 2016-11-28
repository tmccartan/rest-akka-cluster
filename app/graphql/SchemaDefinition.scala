package graphql

import com.gilt.akk.cluster.api.test.v0.models.{PaymentMethod, Address, Session}
import sangria.schema._
import service.{AddressService, PaymentMethodService, SessionService}

import scala.concurrent.ExecutionContext

object SchemaDefinition {

  implicit val ec: ExecutionContext = play.api.libs.concurrent.Execution.Implicits.defaultContext

  val SessionType = ObjectType(
    "Session", "A checkout session",
    fields[Session, Session](
      Field("guid", StringType,
        Some(""), resolve = _.value.guid.toString),
      Field("user", StringType,
        Some(""), resolve = _.value.user),
      Field("shipping_addresses", StringType,
        Some(""), resolve = _.value.shippingAddress),
      Field("payment_methods", StringType,
        Some(""), resolve = _.value.paymentMethods)

    )
  )
  val PaymentMethodType = ObjectType(
    "PaymentMethod", "A payment method",
    fields[PaymentMethod, PaymentMethod] (
      Field("guid", StringType,
        Some(""), resolve = _.value.guid.toString)
    )
  )
  val AddressType = ObjectType(
    "Address", "A address object",
    fields[Address, Address] (
      Field("guid", StringType,
        Some(""), resolve = _.value.guid.toString)
    )
  )

  val sessionArgs = Argument("uuid", StringType, description = "")

  val Query = ObjectType (
    "Query", fields[SessionRepo, Unit] (
      Field("session",
        SessionType,
        //arguments = sessionArgs :: Nil,
        resolve = ctx => ctx.ctx.sessionService.get()
      ),
      Field("paymentMethod",
        ListType(PaymentMethodType),
        resolve = ctx => ctx.ctx.paymentMethodService.getAll()
      ),
      Field("addresses",
        ListType(AddressType),
        resolve = ctx => ctx.ctx.addressService.getAll()
      )
    )
  )

  val CheckoutSchema = Schema(Query)
}

case class SessionRepo (sessionService: SessionService, paymentMethodService: PaymentMethodService, addressService: AddressService)

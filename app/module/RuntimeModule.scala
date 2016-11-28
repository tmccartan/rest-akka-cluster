package module

import controllers._
import graphql.{SchemaDefinition, GraphQLController}
import scaldi.Module

class RuntimeModule extends Module  {

  binding to injected[Session]
  binding to injected[Addresses]
  binding to injected[PaymentMethods]
  binding to injected[Orders]
  binding to injected[GraphQLController]
}

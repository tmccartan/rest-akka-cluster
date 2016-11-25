package module

import controllers._
import scaldi.Module

class RuntimeModule extends Module  {

  binding to injected[Session]
  binding to injected[Addresses]
  binding to injected[PaymentMethods]
  binding to injected[Orders]
}

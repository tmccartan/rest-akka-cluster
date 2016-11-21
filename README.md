
A sample project for leveraging akka clusters to resolve REST based links to resources

The basis of the idea is for rest objects that rely on remote calls from the service will return links to where that object resides. The call for the object is then passed onto a akka actor that is
within a cluster for resolving the object. The cluster allows for the actor to be shared across multiple nodes of the service

When the client requests the resource from the given link, the objects result should be already resolved by the actor in the cluster. If not the future of the result can be piped back to Play

More of a Proof of concept then a fully fledged project



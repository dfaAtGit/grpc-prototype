# grpc-prototype
To launch:
- Start GrpcClientApplication.class
- Start GrpcApplication.class

localhost:8080/

- GET /sync/{text} Synchronous Call, which retrieves a string.
- GET /async/{text} Asynchronous Call, implemented with ListenableFuture.
- GET /async2/{text} Asynchronous Call, implemented with StreamObserver Interface.
- GET /person/{name} Synchronous Call, retrieves an object with multiple attributes.

package com.example.grpc;


import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ServiceImpl extends HelloWorldServiceGrpc.HelloWorldServiceImplBase {
    public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        HelloResponse response = HelloResponse.newBuilder().
                setText(String.format("Hello %s!", request.getText()))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void person(ObjectRequest request, StreamObserver<ObjectResponse> responseObserver) {
        ObjectResponse response = ObjectResponse.newBuilder()
                .setAge(38)
                .setCity("Karlsruhe")
                .setName(request.getName())
                .setWeight(98.2)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}


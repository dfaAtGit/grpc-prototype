package com.example.grpcclient;

import com.example.grpc.HelloRequest;
import com.example.grpc.HelloResponse;
import com.example.grpc.HelloWorldServiceGrpc;
import com.example.grpc.ObjectRequest;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.Descriptors;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class HelloClientService extends HelloWorldServiceGrpc.HelloWorldServiceImplBase {

    @GrpcClient("hello-world")
    HelloWorldServiceGrpc.HelloWorldServiceBlockingStub synchronousClient;

    @GrpcClient("hello-world")
    HelloWorldServiceGrpc.HelloWorldServiceFutureStub asynchronousClient;

    public String sendMessage(String text) {
        HelloRequest request = HelloRequest.newBuilder().setText(text).build();
        HelloResponse response = synchronousClient.hello(request);
        return response.getText();
    }

    public Map<Descriptors.FieldDescriptor, Object> sendMessageAsync(String text) throws TimeoutException, ExecutionException, InterruptedException {
        HelloRequest request = HelloRequest.newBuilder().setText(text).build();
        ListenableFuture<HelloResponse> response = asynchronousClient.hello(request);
        response.addListener(() ->
                        log.info("call completed.")
                , Executors.newCachedThreadPool());
        HelloResponse helloResponse = response.get(5, TimeUnit.SECONDS);
        return helloResponse.getAllFields();
    }

    public void sendMessageAsyncOverride(String text) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        HelloWorldServiceGrpc.HelloWorldServiceStub asyncStub = HelloWorldServiceGrpc.newStub(channel);
        HelloRequest request = HelloRequest.newBuilder().setText(text).build();
        asyncStub.hello(request, new StreamObserver<>() {
            @Override
            public void onNext(HelloResponse value) {
                log.info("Received {}", value.getText());
            }

            @Override
            public void onError(Throwable t) {
                log.error("Error occurred, cause {}", t.getMessage());
            }

            @Override
            public void onCompleted() {
                log.info("Call completed.");
                channel.shutdown();
            }
        });
    }

    public Map<Descriptors.FieldDescriptor, Object> getPerson(String name) {
        ObjectRequest request = ObjectRequest.newBuilder().setName(name).build();
        return synchronousClient.person(request).getAllFields();
    }
}

package com.github.travis.grpc.greeting.client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {

    ManagedChannel channel;

    public static void main(String[] args) {
        System.out.println("Hello this is the gRPC Client");
        GreetingClient main = new GreetingClient();
        main.run();
    }

    public void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        //doUnaryCall(channel);
        //doServerStreamingCall(channel);
        System.out.println("Shutting down channel");
        channel.shutdown();
    }

    private void doUnaryCall(ManagedChannel channel) {
        // Created a protocol buffer greeting message
        Greeting greeting = Greeting.newBuilder()
                .setFirstName("T-rav")
                .setLastName("Martin")
                .build();

        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);
        // do the same for a greetingRequest
        GreetRequest greetRequest = GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        // Call the RPC and get back the GreetResponse (protocol bufferes)
        GreetResponse greetResponse = greetClient.greet(greetRequest);
        // do something here
        System.out.println(greetResponse.getResult());
    }

    private void doServerStreamingCall(ManagedChannel channel) {
        // Prepare the request
        GreetManyTimesRequest greetManyTimesRequest =
                GreetManyTimesRequest.newBuilder()
                        .setGreeting(Greeting.newBuilder().setFirstName("T-rav"))
                        .build();
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        // We stream the response (in a blocking manner)
        greetClient.greetManyTimes(greetManyTimesRequest)
                .forEachRemaining(greetManyTimesResponse -> {
                    System.out.println(greetManyTimesResponse.getResult());
                });
    }
}

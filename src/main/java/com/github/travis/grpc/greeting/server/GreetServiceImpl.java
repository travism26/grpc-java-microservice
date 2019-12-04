package com.github.travis.grpc.greeting.server;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {

    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {

        // extract the fields
        Greeting greeting = request.getGreeting();
        String firstName = greeting.getFirstName();

        // creating response
        String result = "Good Morning " + firstName;
        GreetResponse response = GreetResponse.newBuilder()
                .setResult(result)
                .build();


        // sending the response
        responseObserver.onNext(response);

        //complete the rpc call
        responseObserver.onCompleted();


        //super.greet(request, responseObserver);
    }
}

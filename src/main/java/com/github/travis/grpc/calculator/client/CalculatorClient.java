package com.github.travis.grpc.calculator.client;

import com.proto.calculator.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClient {

    public static void main(String[] args) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);

        SumRequest request = SumRequest.newBuilder()
                .setFirstNumber(25)
                .setSecondNumber(10)
                .build();

        SumResponse response = stub.sum(request);

        System.out.println(request.getFirstNumber() + " + " + request.getSecondNumber() +
                " = " + response.getSumResult());

        MultiplyRequest multiplyRequest = MultiplyRequest.newBuilder()
                .setFirstNumber(25)
                .setSecondNumber(11)
                .build();

        MultiplyResponse multiplyResponse = stub.multiply(multiplyRequest);
        System.out.println("Multiply:"+ multiplyRequest.getFirstNumber() + " * "
                + multiplyRequest.getSecondNumber() +
                " = "+ multiplyResponse.getMultiResult());

        channel.shutdown();
    }
}

package com.github.travis.grpc.calculator.server;

import com.proto.calculator.*;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
        //super.sum(request, responseObserver);
        SumResponse sumResponse = SumResponse.newBuilder()
                .setSumResult(request.getFirstNumber() + request.getSecondNumber())
                .build();

        responseObserver.onNext(sumResponse);

        responseObserver.onCompleted();
    }

    @Override
    public void multiply(MultiplyRequest request, StreamObserver<MultiplyResponse> responseObserver) {
//        super.multiply(request, responseObserver);

        // Pretty much same as before (sum) but multiply stuff...
        MultiplyResponse multiplyResponse = MultiplyResponse.newBuilder()
                .setMultiResult(request.getFirstNumber() * request.getSecondNumber())
                .build();

        responseObserver.onNext(multiplyResponse);
        responseObserver.onCompleted();
    }
}

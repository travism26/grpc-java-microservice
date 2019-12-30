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

	@Override
	public void primeNumberDecomposition(PrimeNumberDecompositionRequest request, StreamObserver<PrimeNumberDecompositionResponse> responseObserver) {
//        super.primeNumberDecomposition(request, responseObserver);

		Integer number = request.getNumber();
		Integer divisor = 2;
		while (number > 1) {
			if (number % divisor == 0) {
				number /= divisor;
				responseObserver.onNext(PrimeNumberDecompositionResponse.newBuilder()
						.setPrimeFactor(divisor)
						.build());
			} else {
				divisor += 1;
			}
		}
		responseObserver.onCompleted();
	}

	@Override
	public StreamObserver<ComputeAverageRequest> computeAverage(StreamObserver<ComputeAverageResponse> responseObserver) {
//        return super.computeAverage(responseObserver);
		StreamObserver<ComputeAverageRequest> requestObserver = new StreamObserver<ComputeAverageRequest>() {
			
			double sum = 0;
			int numCounter = 0;
			@Override
			public void onNext(ComputeAverageRequest value) {
				numCounter++;
				sum += value.getNumber();
			}

			@Override
			public void onError(Throwable t) {
				// this is when client sends an error ignore for now...
			}

			@Override
			public void onCompleted() {
				// this is when the client is complete sending info
				double average = (double) sum / numCounter;
				responseObserver.onNext(
						ComputeAverageResponse.newBuilder()
								.setAverage(average)
								.build()
				);
				responseObserver.onCompleted();
			}
		};
		return requestObserver;
	}
}

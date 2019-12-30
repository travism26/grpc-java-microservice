package com.github.travis.grpc.calculator.client;

import com.proto.calculator.*;
import com.proto.greet.GreetServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClient {
	
	public static void main(String[] args) {
		CalculatorClient obj = new CalculatorClient();
		obj.run();
	}
	public void run() {
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
				.usePlaintext()
				.build();
//		getSumCall(channel);
//		getMultiplyCall(channel);
//		getPrimeNumberDecompositionCall(channel);
		doClientStreamingCall(channel);
		channel.shutdown();
	}
	
	private void getSumCall(ManagedChannel channel) {
		CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);
		SumRequest request = SumRequest.newBuilder()
				.setFirstNumber(25)
				.setSecondNumber(10)
				.build();
		
		SumResponse response = stub.sum(request);
		
		System.out.println(request.getFirstNumber() + " + " + request.getSecondNumber() +
				" = " + response.getSumResult());
	}
	private void getMultiplyCall(ManagedChannel channel) {
		CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);
		
		MultiplyRequest multiplyRequest = MultiplyRequest.newBuilder()
				.setFirstNumber(25)
				.setSecondNumber(11)
				.build();
		
		MultiplyResponse multiplyResponse = stub.multiply(multiplyRequest);
		System.out.println("Multiply:" + multiplyRequest.getFirstNumber() + " * "
				+ multiplyRequest.getSecondNumber() +
				" = " + multiplyResponse.getMultiResult());
	}
	private void getPrimeNumberDecompositionCall(ManagedChannel channel) {
		CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);
		
		Integer num = 567890304;
		stub.primeNumberDecomposition(PrimeNumberDecompositionRequest.newBuilder()
				.setNumber(num)
				.build())
				.forEachRemaining(primeFactor -> {
					System.out.println(primeFactor);
				});
	}
	private void doClientStreamingCall(ManagedChannel channel) {
		CalculatorServiceGrpc.CalculatorServiceStub asyncClient = CalculatorServiceGrpc.newStub(channel);
		
		CountDownLatch latch = new CountDownLatch(1);
		StreamObserver<ComputeAverageRequest> requestObserver = asyncClient.computeAverage(new StreamObserver<ComputeAverageResponse>() {
			@Override
			public void onNext(ComputeAverageResponse value) {
				System.out.println("Received a message from the server...");
				System.out.println("Response:" + value.getAverage());
			}
			
			@Override
			public void onError(Throwable t) {
			// this is if server receives an error...
			}
			
			@Override
			public void onCompleted() {
				// this is when the server is done with my request.
				System.out.println("Server done with our request.");
				latch.countDown();
			}
		});
		
		requestObserver.onNext(ComputeAverageRequest.newBuilder()
				.setNumber(5)
				.build());
		requestObserver.onNext(ComputeAverageRequest.newBuilder()
				.setNumber(10)
				.build());
		// 15 / 2 = 7.5
		requestObserver.onCompleted();
		
		try {
			latch.await(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

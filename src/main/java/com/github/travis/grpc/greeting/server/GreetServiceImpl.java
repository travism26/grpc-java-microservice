package com.github.travis.grpc.greeting.server;

import com.proto.greet.*;
import io.grpc.Context;
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
	
	@Override
	public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {
//        super.greetManyTimes(request, responseObserver);
		String firstName = request.getGreeting().getFirstName();
		
		try {
			for (int i = 0; i < 10; i++) {
				String result = "Hello " + firstName + ", response number: " + i;
				GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder()
						.setResult(result)
						.build();
				
				responseObserver.onNext(response);
				Thread.sleep(1000L);
			}
			
			responseObserver.onCompleted();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			responseObserver.onCompleted();
		}
		
	}
	
	@Override
	public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {
		StreamObserver<LongGreetRequest> requestObserver = new StreamObserver<LongGreetRequest>() {
			String result = "";
			
			@Override
			public void onNext(LongGreetRequest value) {
				// Client sends a message
				result += "Whats up " + value.getGreeting().getFirstName() + "! ";
			}
			
			@Override
			public void onError(Throwable t) {
				// Client sends an Error
			}
			
			@Override
			public void onCompleted() {
				// Client is done
				
				// this is when we want to return a response (ResponseObserver)
				responseObserver.onNext(
						LongGreetResponse.newBuilder()
								.setResult(result)
								.build()
				);
				responseObserver.onCompleted();
			}
		};
//        return super.longGreet(responseObserver)
		
		return requestObserver;
	}
	
	@Override
	public StreamObserver<GreetEveryoneRequest> greetEveryone(StreamObserver<GreetEveryoneResponse> responseObserver) {
		StreamObserver<GreetEveryoneRequest> requestObserver = new StreamObserver<GreetEveryoneRequest>() {
			@Override
			public void onNext(GreetEveryoneRequest value) {
				// the client sends a message
				String response = "Hello, " + value.getGreeting().getFirstName();
				GreetEveryoneResponse greetEveryoneResponse = GreetEveryoneResponse.newBuilder()
						.setResult(response)
						.build();
				responseObserver.onNext(greetEveryoneResponse);
			}
			
			@Override
			public void onError(Throwable t) {
				// Client sends a error message
			}
			
			@Override
			public void onCompleted() {
				// client is done sending messages
				responseObserver.onCompleted();
			}
		};
		
		return requestObserver;
	}
	
	@Override
	public void greetWithDealline(GreetWithDeadlineRequest request, StreamObserver<GreetWithDeadlineResponse> responseObserver) {
		Context context = Context.current();
		try {
			for (int i = 0; i < 3; i++) {
				if (!context.isCancelled()){
					System.out.println("Sleep for 100ms");
					Thread.sleep(300);
				} else{
					return;
				}
			}
			System.out.println("Sending the response");
			GreetWithDeadlineResponse greetWithDeadlineResponse = GreetWithDeadlineResponse.newBuilder()
					.setResponse("Hello " + request.getGreeting().getFirstName())
					.build();
			responseObserver.onNext(greetWithDeadlineResponse);
			
			responseObserver.onCompleted();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		super.greetWithDealline(request, responseObserver);
	}
}

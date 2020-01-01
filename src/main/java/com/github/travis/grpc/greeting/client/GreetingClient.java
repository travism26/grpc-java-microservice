package com.github.travis.grpc.greeting.client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
//		doClientStreamingCall(channel);
		doBiDiStreamingCall(channel);
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
	
	private void doClientStreamingCall(ManagedChannel channel) {
		
		GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);
		CountDownLatch latch = new CountDownLatch(1);
		
		StreamObserver<LongGreetRequest> requestObserver = asyncClient.longGreet(new StreamObserver<LongGreetResponse>() {
			@Override
			public void onNext(LongGreetResponse value) {
				// we get a response from the server.
				
				System.out.println("Received a response from the server");
				System.out.println(value.getResult());
			}
			
			@Override
			public void onError(Throwable t) {
				System.out.println("Got an error:" + t.toString());
				// we get an error from the server.
			}
			
			@Override
			public void onCompleted() {
				// the server is done sending us data.
				// onCompleted will be called right after onNext()
				System.out.println("Server has completed sending us stuff");
				
				latch.countDown();
			}
		});
		
		// Stream MSG #1
		System.out.println("Send message 1");
		requestObserver.onNext(LongGreetRequest.newBuilder()
				.setGreeting(Greeting.newBuilder()
						.setFirstName("Trav")
						.build())
				.build());
		// Stream MSG #2
		System.out.println("Send message 2");
		requestObserver.onNext(LongGreetRequest.newBuilder()
				.setGreeting(Greeting.newBuilder()
						.setFirstName("jen")
						.build())
				.build());
		
		// Stream MSG #3
		System.out.println("Send message 3");
		requestObserver.onNext(LongGreetRequest.newBuilder()
				.setGreeting(Greeting.newBuilder()
						.setFirstName("Marc")
						.build())
				.build());
		
		// Inform the server that the client is down sending data...
		requestObserver.onCompleted();
		try {
			latch.await(3L, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void doBiDiStreamingCall(ManagedChannel channel) {
		GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);
		
		CountDownLatch latch = new CountDownLatch(1);
		
		StreamObserver<GreetEveryoneRequest> requestObserver = asyncClient.greetEveryone(new StreamObserver<GreetEveryoneResponse>() {
			@Override
			public void onNext(GreetEveryoneResponse value) {
				System.out.println("Response from server: " + value.getResult());
			}
			
			@Override
			public void onError(Throwable t) {
				latch.countDown();
			}
			
			@Override
			public void onCompleted() {
				System.out.println("Server is done sending data");
				latch.countDown();
			}
		});
		
		Arrays.asList("Travis", "Martin", "Marky mark", "Jonny").forEach(
				name -> {
					System.out.println("Sending: " + name);
					requestObserver.onNext(GreetEveryoneRequest.newBuilder()
						.setGreeting(Greeting.newBuilder()
								.setFirstName(name)
								.build())
						.build());
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
		);
		
		try {
			latch.await(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

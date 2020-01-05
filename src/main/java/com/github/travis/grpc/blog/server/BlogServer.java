package com.github.travis.grpc.blog.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class BlogServer {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Server server = ServerBuilder.forPort(50051)
				.addService(new BlogserviceImpl())
				.build();
		
		server.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Received Shutdown Request");
			server.shutdown();
			System.out.println("Successfull stopper the server");
		}));
		
		server.awaitTermination();
	}
}

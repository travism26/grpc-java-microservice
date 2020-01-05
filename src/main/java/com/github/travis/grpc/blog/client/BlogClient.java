package com.github.travis.grpc.blog.client;

import com.github.travis.grpc.calculator.client.CalculatorClient;
import com.proto.blog.Blog;
import com.proto.blog.BlogServiceGrpc;
import com.proto.blog.CreateBlogRequest;
import com.proto.blog.CreateBlogResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BlogClient {
	public static void main(String[] args) {
		System.out.println("Starting the blog client");
		BlogClient main = new BlogClient();
		main.run();
	}
	
	private void run(){
		
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",50051)
				.usePlaintext()
				.build();
		
		BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(channel);
		
		Blog blog = Blog.newBuilder()
				.setAuthorId("Travis_Martin")
				.setTitle("Travis New blog")
				.setContent("Hello bud this is a new blog i created")
				.build();
		
		CreateBlogResponse createResponse = blogClient.createBlog(
				CreateBlogRequest.newBuilder()
						.setBlog(blog)
						.build()
		);
		System.out.println("Received Create blog response");
		System.out.println(createResponse.toString());
	}
}

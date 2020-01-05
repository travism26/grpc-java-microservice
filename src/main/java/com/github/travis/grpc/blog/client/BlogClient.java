package com.github.travis.grpc.blog.client;

import com.github.travis.grpc.calculator.client.CalculatorClient;
import com.proto.blog.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BlogClient {
	public static void main(String[] args) {
		System.out.println("Starting the blog client");
		BlogClient main = new BlogClient();
		main.run();
	}
	
	private void run() {
		
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
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
		
		String blogid = createResponse.getBlog().getId();
		System.out.println("Reading a blog with VALID id");
		ReadBlogResponse readBlogResponse = blogClient.readBlog(ReadBlogRequest.newBuilder()
				.setBlogId(blogid)
				.build());
		
		System.out.println(readBlogResponse.toString());
		
//		System.out.println("Looking for a blog that does NOT exist");
//		ReadBlogResponse readBlogResponseNotFound = blogClient.readBlog(ReadBlogRequest.newBuilder()
//				.setBlogId("5e1261a1203df50f4f9d470f")
//				.build());
//		System.out.println(readBlogResponseNotFound.toString());
	}
}

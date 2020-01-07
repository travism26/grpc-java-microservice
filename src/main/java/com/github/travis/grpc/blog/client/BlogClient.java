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
		
		// trigger NOT FOUND ERROR
//		System.out.println("Looking for a blog that does NOT exist");
//		ReadBlogResponse readBlogResponseNotFound = blogClient.readBlog(ReadBlogRequest.newBuilder()
//				.setBlogId("5e1261a1203df50f4f9d470f")
//				.build());
//		System.out.println(readBlogResponseNotFound.toString());
		
		doUpdateClientCall(channel);
	}
	
	public void doUpdateClientCall(ManagedChannel channel) {
		BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(channel);
		
		// Create a blog
		Blog first_blog = Blog.newBuilder()
				.setAuthorId("Travis")
				.setTitle("Travis New blog")
				.setContent("Hello bud this is a new blog i created")
				.build();
		
		System.out.println("Creating a blog first_blog");
		CreateBlogResponse createBlogResponse = blogClient.createBlog(
				CreateBlogRequest.newBuilder()
						.setBlog(first_blog)
						.build());
		String first_blog_id = createBlogResponse.getBlog().getId();
		
		// update the blog object passing in the first blog id.
		Blog update_first_blog = Blog.newBuilder()
				.setId(first_blog_id)
				.setAuthorId("Jes Martin (updated)")
				.setTitle("Martins new blog (updated)")
				.setContent("Sup bud (updated)")
				.build();
		
		// Execute the updateBlog() function on server side.
		System.out.println("Updating the blog!");
		UpdateBlogResponse updateBlogResponse = blogClient.updateBlog(UpdateBlogRequest.newBuilder()
				.setBlog(update_first_blog)
				.build());
		
		System.out.println("Updated the blog!");
		System.out.println(updateBlogResponse.toString());
		
	}
}

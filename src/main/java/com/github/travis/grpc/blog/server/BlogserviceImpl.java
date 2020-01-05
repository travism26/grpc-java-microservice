package com.github.travis.grpc.blog.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.proto.blog.Blog;
import com.proto.blog.BlogServiceGrpc;
import com.proto.blog.CreateBlogRequest;
import com.proto.blog.CreateBlogResponse;
import com.sun.webkit.dom.DocumentImpl;
import io.grpc.stub.StreamObserver;
import org.bson.Document;

public class BlogserviceImpl extends BlogServiceGrpc.BlogServiceImplBase {
	
	// Database stuff this can be abstracted easily.
	private MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
	private MongoDatabase database = mongoClient.getDatabase("mydb");
	private MongoCollection<Document> collection = database.getCollection("blog");
	
	
	@Override
	public void createBlog(CreateBlogRequest request, StreamObserver<CreateBlogResponse> responseObserver) {
		System.out.println("Received Create Blog request");
		Blog blog = request.getBlog();
		Document doc = new Document("author_id", blog.getAuthorId())
				.append("title", blog.getTitle())
				.append("content", blog.getContent());
		// Create the document in the mongoDB (insert)
		System.out.println("Inserting blog");
		collection.insertOne(doc);
		System.out.println("Inserted blog");
		
		// this is the MongoDB generated ID
		String id = doc.getObjectId("_id").toString();
		System.out.println("Inserved blog: " + id);
		
//		CreateBlogResponse response = CreateBlogResponse.newBuilder()
//				.setBlog(Blog.newBuilder()
//				.setAuthorId(blog.getAuthorId())
//				.setContent(blog.getContent())
//				.setId(id)).build();
		// easier way
		CreateBlogResponse response = CreateBlogResponse.newBuilder()
				.setBlog(blog.toBuilder().setId(id).build())
				.build();
		
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}

package com.github.travis.grpc.blog.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.proto.blog.*;
import com.sun.webkit.dom.DocumentImpl;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

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
	
	@Override
	public void readBlog(ReadBlogRequest request, StreamObserver<ReadBlogResponse> responseObserver) {
//		super.readBlog(request, responseObserver);
		System.out.println("Got the readblog request");
		String blogId = request.getBlogId();
		Document result = null;
		try {
			result = collection.find(eq("_id", new ObjectId(blogId)))
					.first();
		} catch (Exception e) {
			responseObserver.onError(
					Status.NOT_FOUND
							.withDescription("The blog with that id was not found.")
							.augmentDescription(e.getLocalizedMessage())
							.asRuntimeException()
			);
		}
		
		if (result == null) {
			System.out.println("The blog id passed in was NOT_FOUND");
			responseObserver.onError(
					Status.NOT_FOUND
							.withDescription("The blog with the corresponding id was not found")
							.asRuntimeException()
			);
		} else {
			System.out.println("Found the blogid returning results...");
			Blog blog = DocumentToBlog(result);
			responseObserver.onNext(ReadBlogResponse.newBuilder().setBlog(blog).build());
			responseObserver.onCompleted();
		}
	}
	
	@Override
	public void updateBlog(UpdateBlogRequest request, StreamObserver<UpdateBlogResponse> responseObserver) {
//		super.updateBlog(request, responseObserver);
		System.out.println("Got the readblog request");
		Blog blog = request.getBlog();
		String blogId = blog.getId();
		Document result = null;
		try {
			result = collection.find(eq("_id", new ObjectId(blogId)))
					.first();
		} catch (Exception e) {
			responseObserver.onError(
					Status.NOT_FOUND
							.withDescription("The blog with that id was not found.")
							.augmentDescription(e.getLocalizedMessage())
							.asRuntimeException()
			);
		}
		
		if (result == null) {
			responseObserver.onError(
					Status.NOT_FOUND
							.withDescription("We did not find that blog error!")
							.asRuntimeException()
			);
		} else {
			Document replacement = new Document("author_id", blog.getAuthorId())
					.append("title", blog.getTitle())
					.append("content", blog.getContent())
					.append("_id", new ObjectId(blogId));
			
			collection.replaceOne(eq("_id", result.getObjectId("_id")), replacement);
			
			responseObserver.onNext(
					UpdateBlogResponse.newBuilder()
							.setBlog(DocumentToBlog(replacement))
							.build()
			);
			
			responseObserver.onCompleted();
		}
		
	}
	
	private Blog DocumentToBlog(Document document) {
		return Blog.newBuilder()
				.setAuthorId(document.getString("author_id"))
				.setTitle(document.getString("title"))
				.setContent(document.getString("content"))
				.setId(document.getObjectId("_id").toString())
				.build();
	}
}

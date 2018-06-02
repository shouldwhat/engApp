package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

@SpringBootApplication
public class AbountGoogleVisionApiApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(AbountGoogleVisionApiApplication.class, args);

		try {
			callVisionApi();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void callVisionApi() throws IOException, Exception {
		
		// Instantiates a client
	    try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

	      // The path to the image file to annotate
	      String fileName = "src/main/resources/sample.jpg";

	      // Reads the image file into memory
	      Path path = Paths.get(fileName);
	      byte[] data = Files.readAllBytes(path);
	      ByteString imgBytes = ByteString.copyFrom(data);

	      // Builds the image annotation request
	      List<AnnotateImageRequest> requests = new ArrayList<>();
	      Image img = Image.newBuilder().setContent(imgBytes).build();
	      Feature feat = Feature.newBuilder().setType(Type.DOCUMENT_TEXT_DETECTION).build();
	      
	      
	      
	      AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
	          .addFeatures(feat)
	          .setImage(img)
	          .build();
	      requests.add(request);

	      // Performs label detection on the image file
	      BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
	      List<AnnotateImageResponse> responses = response.getResponsesList();

	      for (AnnotateImageResponse res : responses) {
	        if (res.hasError()) {
	          System.out.printf("Error: %s\n", res.getError().getMessage());
	          return;
	        }
	        
//	        TextAnnotation fullTextAnnotation = res.getFullTextAnnotation();
//	        System.out.println(String.format("%s", fullTextAnnotation.getText()));
//	        for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
	        for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
	        	System.out.println(String.format("%s", annotation.getDescription()));
//	          annotation.getAllFields().forEach((k, v) ->
//	              System.out.printf("%s : %s\n", k, v.toString()));
	        }
	      }
	    }
	}
}

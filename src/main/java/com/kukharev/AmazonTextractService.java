package com.kukharev;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.*;
import software.amazon.awssdk.services.textract.model.Document;


import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class AmazonTextractService {

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretAccessKey}")
    private String secretAccessKey;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    private final S3Client s3Client;
    private final TextractClient textractClient;

    public AmazonTextractService() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);

        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        this.textractClient = TextractClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    public GetDocumentTextDetectionResponse extractText(MultipartFile file) throws IOException, S3Exception {
        // Export to S3
        String key = uploadFileToS3(file);

        // Running document analysis in Textract
        String jobId = startDocumentTextDetection(key);

        // Waiting for the analysis to complete and receiving the result
        return getDocumentTextDetection(jobId);
    }

    private String uploadFileToS3(MultipartFile file) throws IOException, S3Exception {
        String key = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(),
                Paths.get(file.getOriginalFilename()));
        return key;
    }

    private String startDocumentTextDetection(String s3Key) {
        StartDocumentTextDetectionRequest request = StartDocumentTextDetectionRequest.builder()
                .documentLocation(DocumentLocation.builder()
                        .s3Object(software.amazon.awssdk.services.textract.model.S3Object.builder()
                                .bucket(bucketName)
                                .name(s3Key)
                                .build())
                        .build())
                .build();

        StartDocumentTextDetectionResponse response = textractClient.startDocumentTextDetection(request);
        return response.jobId();
    }

    private GetDocumentTextDetectionResponse getDocumentTextDetection(String jobId) {
        GetDocumentTextDetectionRequest request = GetDocumentTextDetectionRequest.builder()
                .jobId(jobId)
                .build();

        GetDocumentTextDetectionResponse response = textractClient.getDocumentTextDetection(request);

        return response;


        //If we convert the received data into a string immediately, then we can use the code below.
        //In this case, you will need to change the code in several other places.

        /*
            StringBuilder extractedText = new StringBuilder();
            response.blocks().forEach(block -> {
                if (block.blockType().toString().equals("LINE")) {
                    extractedText.append(block.text()).append("\n");
                }
            });
            return extractedText.toString();
        */
    }
}

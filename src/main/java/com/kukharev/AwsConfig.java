package com.kukharev;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.textract.TextractClient;

@Configuration
public class AwsConfig {

    @Bean
    public TextractClient textractClient() {
        return TextractClient.builder()
                .region(Region.of("YOUR_AWS_REGION"))
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of("YOUR_AWS_REGION"))
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }
}

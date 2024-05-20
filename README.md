### File Processing Application

This project is a Spring Boot application that processes PDF and image files by extracting text using Amazon Textract and generates a DOCX file with the extracted text. The application supports both a REST API for file uploads and a simple web interface.

### Features
- Upload PDF or image files to be processed.
- Extract text from uploaded files using Amazon Textract.
- Generate a DOCX file preserving the layout of the original document.
- Simple web interface for file uploads.
- REST API endpoint for programmatic uploads.

### Technologies Used
- Java 11
- Spring Boot
- Spring MVC
- Amazon S3 and Textract SDKs
- Apache POI for DOCX generation
- Thymeleaf for the web interface
- JUnit 5 and Mockito for testing

### Prerequisites
- Java 11
- Maven
- AWS account with access to Textract and S3
- AWS credentials configured in `application.properties`

### Installation

 **Configure AWS Credentials:**
   Create an `application.properties` file in `src/main/resources` with your AWS credentials:
   ```properties
   aws.accessKeyId=YOUR_ACCESS_KEY
   aws.secretAccessKey=YOUR_SECRET_KEY
   aws.region=YOUR_AWS_REGION
   aws.s3.bucketName=YOUR_S3_BUCKET_NAME
   ```

### Project Structure

- **src/main/java/com/kukharev/**:
  - **AmazonTextractService.java:** Service to interact with Amazon Textract.
  - **DocxGenerationService.java:** Service to generate DOCX files.
  - **FileProcessingService.java:** Main service to coordinate file processing.
  - **FileUploadController.java:** REST controller for handling file uploads.
  - **UIController.java:** Controller for handling web interface requests.

- **src/test/java/com/kukharev/**:
  - **AmazonTextractServiceTest.java:** Unit tests for AmazonTextractService.
  - **DocxGenerationServiceTest.java:** Unit tests for DocxGenerationService.
  - **FileProcessingServiceTest.java:** Unit tests for FileProcessingService.

### Testing

Run the tests with Maven:
```bash
mvn test
```

### License

This project is licensed under the MIT License.

---

package com.kukharev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.GetDocumentTextDetectionResponse;

import java.util.List;

@Service
public class FileProcessingService {

    private final AmazonTextractService amazonTextractService;
    private final DocxGenerationService docxGenerationService;

    @Autowired
    public FileProcessingService(AmazonTextractService amazonTextractService, DocxGenerationService docxGenerationService) {
        this.amazonTextractService = amazonTextractService;
        this.docxGenerationService = docxGenerationService;
    }

    public void processFile(MultipartFile file) throws Exception {
        GetDocumentTextDetectionResponse extractedData = amazonTextractService.extractText(file);
        docxGenerationService.generateDocx(extractedData, file.getOriginalFilename());
    }
}

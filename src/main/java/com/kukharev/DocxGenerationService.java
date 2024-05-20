package com.kukharev;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.BlockType;
import software.amazon.awssdk.services.textract.model.GetDocumentTextDetectionResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DocxGenerationService {

    public void generateDocx(GetDocumentTextDetectionResponse textractDocument, String originalFilename) throws IOException {
        // Создание нового документа
        XWPFDocument doc = new XWPFDocument();

        for (Block block : textractDocument.blocks()) {
            if (block.blockType().equals(BlockType.LINE)) {
                XWPFParagraph paragraph = doc.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.setText(block.text());
                // If you want to save positions, you can use the properties of paragraphs and sliders
                // paragraph.setAlignment(ParagraphAlignment.LEFT);
                // run.setFontSize(12);
            }
        }

        // Setting the file name and save path.
        //The file name is set automatically, but if you add a name parameter to the method, it can be set manually.
        String outputFilename = originalFilename.replaceFirst("[.][^.]+$", "") + ".docx";
        Path outputPath = Paths.get("output", outputFilename);

        // Creating a folder if it doesn't exist.
        //You can set the save file path by specifying this information in the method declaration.
        Files.createDirectories(outputPath.getParent());

        // Saving a document to a file
        try (FileOutputStream out = new FileOutputStream(outputPath.toFile())) {
            doc.write(out);
        }

        doc.close();
    }
}

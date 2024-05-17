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
                // Если требуется сохранить позиции, можно использовать свойства параграфов и бегунков
                // paragraph.setAlignment(ParagraphAlignment.LEFT);
                // run.setFontSize(12);
            }
        }

        // Установка имени файла и пути для сохранения
        String outputFilename = originalFilename.replaceFirst("[.][^.]+$", "") + ".docx";
        Path outputPath = Paths.get("output", outputFilename);

        // Создание папки, если она не существует
        Files.createDirectories(outputPath.getParent());

        // Сохранение документа в файл
        try (FileOutputStream out = new FileOutputStream(outputPath.toFile())) {
            doc.write(out);
        }

        // Закрытие документа
        doc.close();
    }
}

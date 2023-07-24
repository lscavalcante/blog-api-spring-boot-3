package com.lscavalcante.blog.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PdfService {

    public Resource createPdf() throws IOException {
        try {
            PDDocument document = new PDDocument();
            PDDocumentInformation info = new PDDocumentInformation();
            info.setTitle("CREATE A NEW PDF: ");
            document.setDocumentInformation(info);

            for (int i = 0; i < 2; i++) {
                //Creating a blank page
                PDPage blankPage = new PDPage();
                PDPageContentStream contentStream = new PDPageContentStream(document, blankPage);

                //Begin the Content stream
                contentStream.beginText();

                //Setting the font to the Content stream
                PDFont pdfFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                contentStream.setFont(pdfFont, 12);

                String text = "This is the sample document and we are adding content to it.";
                contentStream.newLineAtOffset(100, 700);

                //Adding text in the form of string
                contentStream.showText(text);

                //Ending the content stream
                contentStream.endText();

                //Closing the content stream
                contentStream.close();

                //Adding the blank page to the document
                document.addPage(blankPage);
            }

            //Saving the document
            document.save("uploads/my_doc.pdf");

            //Closing the document
            document.close();

            Path imagePath = Paths.get("uploads").resolve("my_doc.pdf").toAbsolutePath().normalize();

            if (!Files.exists(imagePath) || Files.isDirectory(imagePath)) {
                throw new RuntimeException("Imagem não encontrada");
            }

            // How you can delete the file
            // File file = new File(imagePath.toUri(););
            // file.delete();


            return new UrlResource(imagePath.toUri());
        } catch (Exception e) {
            throw new RuntimeException("Error when tried to create a pdf file" + e.toString());
        }
    }

}

package ru.clevertec.generator;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import ru.clevertec.entity.Entity;
import ru.clevertec.exception.PDFGenerationException;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityPdfGenerator implements PdfGenerator {
    private static final String FILE = "generated/CashReceipt_%s.pdf";
    public static final String TEMPLATE = "src/main/resources/Clevertec_Template.pdf";
    private static final Font TITLE_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 17, Font.BOLD);

    public void generate(Entity... entities) {
        var document = new Document(PageSize.A4, 30, 0, 120, 0);
        var out = new ByteArrayOutputStream();

        try {
            var writer = PdfWriter.getInstance(document,
                    new FileOutputStream(String.format(FILE, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MMM-dd-HH-mm-ss")))));
            PdfWriter.getInstance(document, out);
            document.open();

            PdfReader reader = new PdfReader(TEMPLATE);
            PdfImportedPage page = writer.getImportedPage(reader, 1);
            PdfContentByte contentByte = writer.getDirectContentUnder();
            contentByte.addTemplate(page, 0, 0);

            addHeader(document);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(95);
            addTableHeader(table);
            Arrays.stream(entities).forEach(e -> addRows(table, e));

            document.add(table);
            document.close();
        } catch (Exception e) {
            throw new PDFGenerationException("Error, while generating pdf file. ", e);
        }
    }

    private void addHeader(Document document) {
        var header = new Paragraph("Entities".toUpperCase(), TITLE_FONT);
        header.setAlignment(Element.ALIGN_CENTER);
        try {
            document.add(header);
            document.add(Chunk.NEWLINE);
        } catch (DocumentException e) {
            throw new PDFGenerationException("Error while header generation.", e);
        }
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of(Entity.Fields.id, Entity.Fields.createdAt, Entity.Fields.modifiedAt, Entity.Fields.isActive, Entity.Fields.collection)
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, Entity entity) {
        table.addCell(String.valueOf(entity.getId()));
        table.addCell(String.valueOf(entity.getCreatedAt()));
        table.addCell(String.valueOf(entity.getModifiedAt()));
        table.addCell(String.valueOf(entity.isActive()));
        table.addCell(entity.getCollection().stream().map(Object::toString).collect(Collectors.joining(",")));
    }
}

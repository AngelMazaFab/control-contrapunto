package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.model.ComprobantePago;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Servicio para la generación de comprobantes en PDF.
 */
@Service
public class ServicioComprobantePdf {

    private static final Color ORANGE_HEADER = new Color(244, 140, 91);
    private static final Color LIGHT_RED = new Color(220, 53, 69);

    /** Genera el archivo PDF del comprobante. */
    public byte[] generarPdf(ComprobantePago comprobante) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            
            FooterEvent event = new FooterEvent();
            writer.setPageEvent(event);
            
            document.open();

            // Fuentes
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
            Font fontDate = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.DARK_GRAY);
            Font fontNormalBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
            Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);
            Font fontTableHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
            Font fontTotalLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.BLACK);
            Font fontTotalValue = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.BLACK);
            Font fontPagado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, LIGHT_RED);

            // 1. Encabezado
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1f, 2f});
            
            PdfPCell logoCell = new PdfPCell();
            logoCell.setBorder(Rectangle.NO_BORDER);
            try {
                ClassPathResource resource = new ClassPathResource("static/images/contrapunto-logo.png");
                if (resource.exists()) {
                    Image logo = Image.getInstance(resource.getURL());
                    logo.scaleToFit(150, 100);
                    logoCell.addElement(logo);
                }
            } catch (Exception e) {}
            headerTable.addCell(logoCell);
            
            PdfPCell titleCell = new PdfPCell();
            titleCell.setBorder(Rectangle.NO_BORDER);
            titleCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            
            Paragraph title = new Paragraph("COMPROBANTE DE PAGO", fontTitle);
            title.setAlignment(Element.ALIGN_RIGHT);
            titleCell.addElement(title);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d - MMMM - yyyy", new Locale("es", "MX"));
            String formattedDate = comprobante.getFechaPago().format(formatter).toUpperCase();
            Paragraph date = new Paragraph(formattedDate, fontDate);
            date.setAlignment(Element.ALIGN_RIGHT);
            titleCell.addElement(date);
            
            headerTable.addCell(titleCell);
            document.add(headerTable);
            document.add(new Paragraph("\n\n"));

            // 2. Datos Alumno
            String nombreAlumno = comprobante.getAlumno().getNombreAlumno().toUpperCase();
            Paragraph clientData = new Paragraph();
            clientData.add(new Chunk("NOMBRE: ", fontNormalBold));
            clientData.add(new Chunk(nombreAlumno, fontNormal));
            document.add(clientData);
            document.add(new Paragraph("\n\n"));

            // 3. Tabla Detalles
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3f, 1f});
            
            PdfPCell h1 = new PdfPCell(new Phrase("DESCRIPCIÓN", fontTableHeader));
            h1.setBackgroundColor(ORANGE_HEADER);
            h1.setPadding(8);
            table.addCell(h1);
            
            PdfPCell h2 = new PdfPCell(new Phrase("PRECIO", fontTableHeader));
            h2.setBackgroundColor(ORANGE_HEADER);
            h2.setPadding(8);
            h2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(h2);
            
            PdfPCell c1 = new PdfPCell(new Phrase(comprobante.getDescripcion(), fontNormal));
            c1.setPadding(8);
            c1.setBorderColor(Color.LIGHT_GRAY);
            table.addCell(c1);
            
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
            String montoStr = currencyFormat.format(comprobante.getImporte());
            
            PdfPCell c2 = new PdfPCell(new Phrase(montoStr, fontNormal));
            c2.setPadding(8);
            c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c2.setBorderColor(Color.LIGHT_GRAY);
            table.addCell(c2);
            
            document.add(table);
            
            // 4. Totales
            PdfPTable totalsTable = new PdfPTable(2);
            totalsTable.setWidthPercentage(100);
            totalsTable.setWidths(new float[]{3f, 1f});
            
            PdfPCell emptyCell = new PdfPCell();
            emptyCell.setBorder(Rectangle.NO_BORDER);
            totalsTable.addCell(emptyCell);
            
            PdfPTable totalBox = new PdfPTable(2);
            totalBox.setWidthPercentage(100);
            totalBox.setWidths(new float[]{1f, 1.5f});
            
            PdfPCell totalLabel = new PdfPCell(new Phrase("TOTAL", fontTotalLabel));
            totalLabel.setBorder(Rectangle.BOX);
            totalLabel.setPadding(8);
            totalLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalLabel.setBackgroundColor(new Color(240, 240, 240));
            totalBox.addCell(totalLabel);
            
            PdfPCell totalValue = new PdfPCell(new Phrase(montoStr, fontTotalValue));
            totalValue.setBorder(Rectangle.BOX);
            totalValue.setPadding(8);
            totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalBox.addCell(totalValue);
            
            PdfPCell totalContainer = new PdfPCell(totalBox);
            totalContainer.setBorder(Rectangle.NO_BORDER);
            totalsTable.addCell(totalContainer);
            
            document.add(totalsTable);
            document.add(new Paragraph("\n\n"));
            
            // 5. Sello Pagado
            Paragraph stamp = new Paragraph("PAGADO", fontPagado);
            stamp.setAlignment(Element.ALIGN_RIGHT);
            document.add(stamp);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF", e);
        }
    }

    /** Evento para inyectar pie de página. */
    class FooterEvent extends PdfPageEventHelper {
        Font fontFooter = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.DARK_GRAY);

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            Phrase footer = new Phrase("WhatsApp 74 72 19 93 20\nEje Central 4, Col. Burócratas, Chilpancingo, Gro.", fontFooter);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, footer, document.left(), document.bottom() - 20, 0);
        }
    }
}

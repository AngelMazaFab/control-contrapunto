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
 * Servicio dedicado a la generación de documentos PDF.
 * Utiliza la librería OpenPDF (fork de iText) para dibujar los comprobantes de pago.
 */
@Service
public class ComprobantePdfService {

    // Constantes de estilo visual corporativo
    private static final Color ORANGE_HEADER = new Color(244, 140, 91); // #F48C5B
    private static final Color LIGHT_RED = new Color(220, 53, 69); // PAGADO color

    /**
     * Construye un archivo PDF en memoria a partir de los datos de un comprobante.
     * 
     * @param comprobante La entidad que contiene los datos de la transacción.
     * @return Arreglo de bytes que representa el archivo PDF binario.
     */
    public byte[] generarPdf(ComprobantePago comprobante) {
        // Bloque: Inicialización del Flujo
        // ByteArrayOutputStream permite escribir el archivo en memoria sin tocar el disco duro
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Se configura el documento con tamaño A4 y márgenes de 50 puntos
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            
            // Bloque: Configuración de Pie de Página
            // Se inyecta un evento que se dispara automáticamente al final de cada página
            FooterEvent event = new FooterEvent();
            writer.setPageEvent(event);
            
            // Se abre el documento para comenzar a escribir en él
            document.open();

            // Bloque: Definición de Tipografía
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
            Font fontDate = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.DARK_GRAY);
            Font fontNormalBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
            Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);
            Font fontTableHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
            Font fontTotalLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.BLACK);
            Font fontTotalValue = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.BLACK);
            Font fontPagado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, LIGHT_RED);

            // Bloque: 1. Encabezado (Logo + Título y Fecha)
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1f, 2f});
            
            // Celda del Logo: Intenta cargar la imagen del logo desde los recursos estáticos
            PdfPCell logoCell = new PdfPCell();
            logoCell.setBorder(Rectangle.NO_BORDER);
            try {
                ClassPathResource resource = new ClassPathResource("static/images/contrapunto-logo.png");
                if (resource.exists()) {
                    Image logo = Image.getInstance(resource.getURL());
                    logo.scaleToFit(150, 100);
                    logoCell.addElement(logo);
                }
            } catch (Exception e) {
                // Si la imagen no existe o falla, se ignora para no quebrar la generación del PDF
            }
            headerTable.addCell(logoCell);
            
            // Celda de Título y Fecha
            PdfPCell titleCell = new PdfPCell();
            titleCell.setBorder(Rectangle.NO_BORDER);
            titleCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            
            Paragraph title = new Paragraph("COMPROBANTE DE PAGO", fontTitle);
            title.setAlignment(Element.ALIGN_RIGHT);
            titleCell.addElement(title);
            
            // Formatea la fecha al estilo "25 - MAYO - 2026"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d - MMMM - yyyy", new Locale("es", "MX"));
            String formattedDate = comprobante.getFechaPago().format(formatter).toUpperCase();
            Paragraph date = new Paragraph(formattedDate, fontDate);
            date.setAlignment(Element.ALIGN_RIGHT);
            titleCell.addElement(date);
            
            headerTable.addCell(titleCell);
            document.add(headerTable);
            
            document.add(new Paragraph("\n\n")); // Espaciador

            // Bloque: 2. Datos del Cliente
            String nombreAlumno = comprobante.getAlumno().getNombreAlumno().toUpperCase();
            Paragraph clientData = new Paragraph();
            clientData.add(new Chunk("NOMBRE: ", fontNormalBold));
            clientData.add(new Chunk(nombreAlumno, fontNormal));
            document.add(clientData);
            
            document.add(new Paragraph("\n\n")); // Espaciador

            // Bloque: 3. Tabla de Detalles del Pago
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3f, 1f});
            
            // Cabeceras de la tabla
            PdfPCell h1 = new PdfPCell(new Phrase("DESCRIPCIÓN", fontTableHeader));
            h1.setBackgroundColor(ORANGE_HEADER);
            h1.setPadding(8);
            table.addCell(h1);
            
            PdfPCell h2 = new PdfPCell(new Phrase("PRECIO", fontTableHeader));
            h2.setBackgroundColor(ORANGE_HEADER);
            h2.setPadding(8);
            h2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(h2);
            
            // Fila con los datos reales del comprobante
            PdfPCell c1 = new PdfPCell(new Phrase(comprobante.getDescripcion(), fontNormal));
            c1.setPadding(8);
            c1.setBorderColor(Color.LIGHT_GRAY);
            table.addCell(c1);
            
            // Formatear el importe como moneda ($ 1,000.00)
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
            String montoStr = currencyFormat.format(comprobante.getImporte());
            
            PdfPCell c2 = new PdfPCell(new Phrase(montoStr, fontNormal));
            c2.setPadding(8);
            c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c2.setBorderColor(Color.LIGHT_GRAY);
            table.addCell(c2);
            
            document.add(table);
            
            // Bloque: 4. Fila de Totales
            PdfPTable totalsTable = new PdfPTable(2);
            totalsTable.setWidthPercentage(100);
            totalsTable.setWidths(new float[]{3f, 1f});
            totalsTable.setSpacingBefore(0);
            
            PdfPCell emptyCell = new PdfPCell();
            emptyCell.setBorder(Rectangle.NO_BORDER);
            totalsTable.addCell(emptyCell); // Celda vacía para alinear el total a la derecha
            
            // Caja específica para el TOTAL
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
            totalContainer.setPadding(0);
            totalsTable.addCell(totalContainer);
            
            document.add(totalsTable);
            
            document.add(new Paragraph("\n\n")); // Espaciador final
            
            // Bloque: 5. Sello de "PAGADO"
            Paragraph stamp = new Paragraph("PAGADO", fontPagado);
            stamp.setAlignment(Element.ALIGN_RIGHT);
            document.add(stamp);

            // Bloque: Cierre y Exportación
            document.close();
            return baos.toByteArray(); // Extrae el archivo en forma de bytes
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el comprobante PDF", e);
        }
    }

    /**
     * Clase interna encargada de inyectar el texto del pie de página automáticamente.
     */
    class FooterEvent extends PdfPageEventHelper {
        Font fontFooter = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.DARK_GRAY);

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            Phrase footer = new Phrase("WhatsApp 74 72 19 93 20\nEje Central 4, Col. Burócratas, Chilpancingo, Gro.", fontFooter);
            // Posiciona el texto del pie de página a 20 puntos del margen inferior
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                    footer,
                    document.left(),
                    document.bottom() - 20, 0);
        }
    }
}

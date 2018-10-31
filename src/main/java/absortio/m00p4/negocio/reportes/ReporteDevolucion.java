package absortio.m00p4.negocio.reportes;

import absortio.m00p4.negocio.model.DetalleDevolucionModel;
import absortio.m00p4.negocio.model.DevolucionModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.FormateadorDecimal;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

public class ReporteDevolucion {

    public void pdfdevolucion(DevolucionModel doc, List<DetalleDevolucionModel> detalles) {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File fileDirectory = directoryChooser.showDialog(null);


            OutputStream file = new FileOutputStream(new File(fileDirectory.getPath()+File.separator +doc.getNodevolucion()+".pdf"));


            Document document = new Document();

            PdfWriter.getInstance(document, file);

            Font fontdatosbold=new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD);

            Font fontdatos=new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.NORMAL);

            document.open();

            Chunk glue = new Chunk(new VerticalPositionMark());
            Paragraph titulo=new Paragraph("NOTA DE CREDITO",new Font(Font.FontFamily.TIMES_ROMAN, 16,
                    Font.BOLD));
            titulo.add(new Chunk(glue));
            titulo.add(doc.getNodevolucion());
            document.add(titulo);

            document.add(new Paragraph(" "));
            document.add(new LineSeparator());

            Paragraph fecha=new Paragraph("F. emision: "+doc.getFechaEmision().toString(),fontdatos);
            fecha.setAlignment(Element.ALIGN_RIGHT);
            document.add(fecha);
            document.add(new Paragraph(" "));
            document.add(new LineSeparator());

            //document.add(new Paragraph("NOTA DE CREDITO"));
            //document.add(new Paragraph("No Doc.: "+doc.getNodevolucion()));
            document.add(new Paragraph("DATOS",fontdatos));
            document.add(new Paragraph("Doc. Venta: "+doc.getDocumento().getNoDocumento(),fontdatos));
            //TODO
            //document.add(new Paragraph("Cliente: "+ doc.getDocumento().getCliente().getNombres(),fontdatos));
            //TODO
            //document.add(new Paragraph("RUC/DNI: "+ doc.getDocumento().getCliente().getDocumentoIdentidad(),fontdatos));


            document.add(new Paragraph(" "));
            document.add(new LineSeparator());

            document.add(new Paragraph("DETALLE ", fontdatos));
            document.add(new Paragraph(" "));


            PdfPTable my_report_table = new PdfPTable(6);
            //create a cell object
            PdfPCell table_cell;

            table_cell=new PdfPCell(new Phrase("Cod Barras",fontdatos));
            my_report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("Nombre Prod",fontdatos));
            my_report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("Id Lote",fontdatos));
            my_report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("Precio Unit.",fontdatos));
            my_report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("Cantidad",fontdatos));
            my_report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("Importe",fontdatos));
            my_report_table.addCell(table_cell);


            for(int i=0;i<detalles.size();i++) {
                table_cell=new PdfPCell(new Phrase(detalles.get(i).getIdLote().getProducto().getCodigoBarras(),fontdatos));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(detalles.get(i).getIdLote().getProducto().getNombre(),fontdatos));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(detalles.get(i).getIdLote().getId().toString(),fontdatos));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(FormateadorDecimal.formatear(detalles.get(i).getpU()),fontdatos));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(detalles.get(i).getCantidad().toString(),fontdatos));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(FormateadorDecimal.formatear(detalles.get(i).getImporte()),fontdatos));
                my_report_table.addCell(table_cell);

            }
                    /* Attach report table to PDF */
            document.add(my_report_table);

            document.add(new Paragraph(" "));
            document.add(new LineSeparator());

            Paragraph subtotal=new Paragraph("Subtotal :"+ FormateadorDecimal.formatear(doc.getSubtotal()),fontdatos);
            subtotal.setAlignment(Element.ALIGN_RIGHT);
            document.add(subtotal);
            Double impuestos=doc.getImpuestos();
            Double impuestostrunc = BigDecimal.valueOf(impuestos)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
            Paragraph impuestosx=new Paragraph("Impuestos :"+ impuestostrunc.toString(),fontdatos);
            impuestosx.setAlignment(Element.ALIGN_RIGHT);
            document.add(impuestosx);
            Paragraph descuentos=new Paragraph("Descuentos :"+ FormateadorDecimal.formatear(doc.getDescuentos()),fontdatos);
            descuentos.setAlignment(Element.ALIGN_RIGHT);
            document.add(descuentos);
            Paragraph total=new Paragraph("TOTAL :"+ FormateadorDecimal.formatear(doc.getTotal()), fontdatosbold);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);
            //document.add(new Paragraph("Subtotal :"+ doc.getSubtotal().toString()));
            //Double impuestos=doc.getIgv().getValor()*doc.getSubtotal();
            //document.add(new Paragraph("Impuestos :"+ doc.getImpuestos().toString()));
            //document.add(new Paragraph("Descuentos :"+ doc.getDescuentos().toString()));
            //document.add(new Paragraph("TOTAL :"+ doc.getTotal().toString()));


            document.close();

            file.close();


        } catch (Exception e) {


            e.printStackTrace();

        }
    }
}

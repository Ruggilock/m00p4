package absortio.m00p4.negocio.reportes;

import absortio.m00p4.negocio.model.DespachoModel;
import absortio.m00p4.negocio.model.DetalleDespachoModel;
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
import java.util.List;

public class ReporteDespacho {
    public void pdfdevolucion(DespachoModel doc, List<DetalleDespachoModel> detalles) {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File fileDirectory = directoryChooser.showDialog(null);


            OutputStream file = new FileOutputStream(new File(fileDirectory.getPath() + File.separator + doc.getSobreNombre() + "-" + doc.getId() + ".pdf"));


            Document document = new Document();

            PdfWriter.getInstance(document, file);

            Font fontdatosbold=new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD);

            Font fontdatos=new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.NORMAL);


            document.open();

            Chunk glue = new Chunk(new VerticalPositionMark());
            Paragraph titulo=new Paragraph("GUIA DE REMISION",new Font(Font.FontFamily.TIMES_ROMAN, 16,
                    Font.BOLD));
            titulo.add(new Chunk(glue));
            titulo.add(doc.getSobreNombre() + "-" + doc.getId());
            document.add(titulo);

            document.add(new Paragraph(" "));
            document.add(new LineSeparator());
            //document.add(new Paragraph(" "));
            Paragraph fecha=new Paragraph("F. emision: "+doc.getFecha().toString(),fontdatos);
            fecha.setAlignment(Element.ALIGN_RIGHT);
            document.add(fecha);
            document.add(new Paragraph(" "));
            document.add(new LineSeparator());

            //document.add(new Paragraph("GUIA DE REMISION"));
            //document.add(new Paragraph("No Doc.: " + doc.getSobreNombre() + "-" + doc.getId()));
            //document.add(new Paragraph("Nombre Cliente: "+ doc.getDocumento().getCliente().getNombres()));
            //document.add(new Paragraph("No Doc. Cliente: "+ doc.getDocumento().getCliente().getDocumentoIdentidad()));
            //document.add(new Paragraph("Fecha de proforma: "+doc.getFechaEmision().toString()));
            document.add(new Paragraph("DATOS",fontdatos));
            document.add(new Paragraph(("Transportista: " + doc.getIdTransportista().getNombreCompleto()),fontdatos));
            document.add(new Paragraph(("Doc. Transportista: " + doc.getIdTransportista().getDocumentoIdentidad().toString()),fontdatos));
            //document.add(new Paragraph("Fecha de emision: " + doc.getFecha().toString()));
            document.add(new Paragraph(" "));
            document.add(new LineSeparator());

            document.add(new Paragraph("DETALLE ", fontdatos));
            document.add(new Paragraph(" "));

            PdfPTable my_report_table = new PdfPTable(4);
            //create a cell object
            PdfPCell table_cell;

            table_cell = new PdfPCell(new Phrase("No. Documento",fontdatos));
            my_report_table.addCell(table_cell);
            table_cell = new PdfPCell(new Phrase("Cod Barras",fontdatos));
            my_report_table.addCell(table_cell);
            table_cell = new PdfPCell(new Phrase("Nombre Prod",fontdatos));
            my_report_table.addCell(table_cell);
            table_cell = new PdfPCell(new Phrase("Cantidad Atendida",fontdatos));
            my_report_table.addCell(table_cell);

            //List<DetalleDespachoModel> detalles=doc.getDetalleDespachos();


            for(int i=0;i<detalles.size();i++) {
                table_cell=new PdfPCell(new Phrase(detalles.get(i).getIddetalleEntrega().getIdDetalleDocumento().getIdDocumento().getNoDocumento(),fontdatos));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(detalles.get(i).getIddetalleEntrega().getIdDetalleDocumento().getIdLote().getProducto().getCodigoBarras(),fontdatos));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(detalles.get(i).getIddetalleEntrega().getIdDetalleDocumento().getIdLote().getProducto().getNombre(),fontdatos));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(detalles.get(i).getCantidadAtendida().toString(),fontdatos));
                my_report_table.addCell(table_cell);


            }

            document.add(my_report_table);
            document.close();

            file.close();


        } catch (Exception e) {


            e.printStackTrace();

        }
    }
}

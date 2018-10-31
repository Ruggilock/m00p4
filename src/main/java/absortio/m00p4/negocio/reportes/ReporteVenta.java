package absortio.m00p4.negocio.reportes;

import absortio.m00p4.negocio.model.*;

import java.io.File;

import java.io.FileOutputStream;

import java.io.IOException;
import java.io.OutputStream;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;


import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.FormateadorDecimal;
import absortio.m00p4.negocio.model.auxiliares.DetalleDespachoAux;
import com.itextpdf.text.*;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import com.itextpdf.text.Document;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import javafx.stage.DirectoryChooser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReporteVenta {


    public void pdfventa(DocumentoModel doc, List<DetalleDocumentoModel> detalles, List<FacturaParcialModel> facturasParciales, List<List<DetalleFacturaParcialModel>> detallesFacturaParcial, List<List<DetalleDespachoAux>> detalleDespachoAuxes) {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File fileDirectory = directoryChooser.showDialog(null);


            OutputStream file = new FileOutputStream(new File(fileDirectory.getPath() + File.separator + doc.getNoDocumento() + ".pdf"));


            Document document = new Document();

            PdfWriter.getInstance(document, file);

            Font fontdatosbold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD);

            Font fontdatos = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.NORMAL);

            document.open();

            Chunk glue = new Chunk(new VerticalPositionMark());
            Paragraph titulo;
            if (doc.getEstado().equals("proforma")) {
                titulo = new Paragraph("DOCUMENTO DE PROFORMA", new Font(Font.FontFamily.TIMES_ROMAN, 16,
                        Font.BOLD));
            } else {
                titulo = new Paragraph("DOCUMENTO DE VENTA", new Font(Font.FontFamily.TIMES_ROMAN, 16,
                        Font.BOLD));
            }
            titulo.add(new Chunk(glue));
            titulo.add(doc.getNoDocumento());
            document.add(titulo);

            document.add(new Paragraph(" "));
            document.add(new LineSeparator());
            //document.add(new Paragraph(" "));
            Paragraph fecha = new Paragraph("F. emision: " + doc.getFechaEmision().toString(), fontdatos);
            fecha.setAlignment(Element.ALIGN_RIGHT);
            document.add(fecha);
            document.add(new Paragraph(" "));
            document.add(new LineSeparator());
            //document.add(new Paragraph("No Doc.: "+doc.getNoDocumento(),fontdatos));
            document.add(new Paragraph("DATOS CLIENTE ", fontdatos));
            document.add(new Paragraph("Nombre : " + doc.getCliente().getNombres(), fontdatos));
            document.add(new Paragraph("RUC/DNI: " + doc.getCliente().getDocumentoIdentidad(), fontdatos));

            document.add(new Paragraph(" "));
            document.add(new LineSeparator());


            document.add(new Paragraph("DETALLE ", fontdatos));
            document.add(new Paragraph(" "));
            PdfPTable my_report_table = new PdfPTable(7);
            //create a cell object
            PdfPCell table_cell;

            table_cell = new PdfPCell(new Phrase("Cod Barras", fontdatos));
            my_report_table.addCell(table_cell);
            table_cell = new PdfPCell(new Phrase("Nombre Prod", fontdatos));
            my_report_table.addCell(table_cell);
            table_cell = new PdfPCell(new Phrase("Id Lote", fontdatos));
            my_report_table.addCell(table_cell);
            table_cell = new PdfPCell(new Phrase("Precio Unit.", fontdatos));
            my_report_table.addCell(table_cell);
            table_cell = new PdfPCell(new Phrase("Cantidad", fontdatos));
            my_report_table.addCell(table_cell);
            table_cell = new PdfPCell(new Phrase("Descuento", fontdatos));
            my_report_table.addCell(table_cell);
            table_cell = new PdfPCell(new Phrase("Importe", fontdatos));
            my_report_table.addCell(table_cell);

            for (int i = 0; i < detalles.size(); i++) {
                table_cell = new PdfPCell(new Phrase(detalles.get(i).getIdLote().getProducto().getCodigoBarras(), fontdatos));
                my_report_table.addCell(table_cell);
                table_cell = new PdfPCell(new Phrase(detalles.get(i).getIdLote().getProducto().getNombre(), fontdatos));
                my_report_table.addCell(table_cell);
                table_cell = new PdfPCell(new Phrase(detalles.get(i).getIdLote().getId().toString(), fontdatos));
                my_report_table.addCell(table_cell);
                table_cell = new PdfPCell(new Phrase(FormateadorDecimal.formatear(detalles.get(i).getpU()), fontdatos));
                my_report_table.addCell(table_cell);
                table_cell = new PdfPCell(new Phrase(detalles.get(i).getCantidad().toString(), fontdatos));
                my_report_table.addCell(table_cell);
                table_cell = new PdfPCell(new Phrase(FormateadorDecimal.formatear(detalles.get(i).getdU()), fontdatos));
                my_report_table.addCell(table_cell);
                table_cell = new PdfPCell(new Phrase(FormateadorDecimal.formatear(detalles.get(i).getImporte()), fontdatos));
                my_report_table.addCell(table_cell);

            }
                    /* Attach report table to PDF */
            document.add(my_report_table);

            document.add(new Paragraph(" "));
            document.add(new LineSeparator());


            Paragraph subtotal = new Paragraph("Subtotal :" + FormateadorDecimal.formatear(doc.getSubtotal()), fontdatos);
            subtotal.setAlignment(Element.ALIGN_RIGHT);
            document.add(subtotal);
            Double impuestos = doc.getIgv().getValor() * doc.getSubtotal();
            Double impuestostrunc = BigDecimal.valueOf(impuestos)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
            Paragraph impuestosx = new Paragraph("Impuestos :" + impuestostrunc.toString(), fontdatos);
            impuestosx.setAlignment(Element.ALIGN_RIGHT);
            document.add(impuestosx);
            Paragraph fletes = new Paragraph("Flete :" + FormateadorDecimal.formatear(doc.getFleteTotal()), fontdatos);
            fletes.setAlignment(Element.ALIGN_RIGHT);
            document.add(fletes);
            Paragraph descuentos = new Paragraph("Descuentos :" + FormateadorDecimal.formatear(doc.getDescuentoTotal()), fontdatos);
            descuentos.setAlignment(Element.ALIGN_RIGHT);
            document.add(descuentos);
            Paragraph total = new Paragraph("TOTAL :" + FormateadorDecimal.formatear(doc.getMontoTotal()), fontdatosbold);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);


            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            document.newPage();

            document.add(new Paragraph("DATOS ENTREGA ", fontdatos));
            document.add(new Paragraph("Direccion: " + doc.getDireccion() + " " + doc.getFlete().getDistrito().getDistrito() + " " + doc.getFlete().getDistrito().getProvincia() + " " + doc.getFlete().getDistrito().getDepartamento()));

            document.add(new Paragraph(" "));
            document.add(new LineSeparator());

            document.add(new Paragraph("DETALLE ENTREGA ", fontdatos));
            document.add(new Paragraph(" "));

            PdfPTable my_report_table2 = new PdfPTable(5);
            //create a cell object
            PdfPCell table_cell2;

            table_cell2 = new PdfPCell(new Phrase("F. Entrega", fontdatos));
            my_report_table2.addCell(table_cell2);
            table_cell2 = new PdfPCell(new Phrase("Cod Barras", fontdatos));
            my_report_table2.addCell(table_cell2);
            table_cell2 = new PdfPCell(new Phrase("Producto", fontdatos));
            my_report_table2.addCell(table_cell2);
            table_cell2 = new PdfPCell(new Phrase("Id Lote", fontdatos));
            my_report_table2.addCell(table_cell2);
            table_cell2 = new PdfPCell(new Phrase("Cantidad", fontdatos));
            my_report_table2.addCell(table_cell2);

            for (int i = 0; i < detalles.size(); i++) {
                List<DetalleEntregaModel> detallesentregas = detalles.get(i).getDetalleEntregas();
                for (int j = 0; j < detallesentregas.size(); j++) {
                    table_cell2 = new PdfPCell(new Phrase(detallesentregas.get(j).getFechaEntrega().toString(), fontdatos));
                    my_report_table2.addCell(table_cell2);
                    table_cell2 = new PdfPCell(new Phrase(detalles.get(i).getIdLote().getProducto().getCodigoBarras(), fontdatos));
                    my_report_table2.addCell(table_cell2);
                    table_cell2 = new PdfPCell(new Phrase(detalles.get(i).getIdLote().getProducto().getNombre(), fontdatos));
                    my_report_table2.addCell(table_cell2);
                    table_cell2 = new PdfPCell(new Phrase(detalles.get(i).getIdLote().getId().toString(), fontdatos));
                    my_report_table2.addCell(table_cell2);
                    table_cell2 = new PdfPCell(new Phrase(detallesentregas.get(j).getCantidadtotal().toString(), fontdatos));
                    my_report_table2.addCell(table_cell2);

                }

            }

            document.add(my_report_table2);

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            for (int j = 1; j <= facturasParciales.size(); j++) {
                FacturaParcialModel facturaParcialModel = facturasParciales.get(j - 1);
                List<DetalleFacturaParcialModel> detalleFacturaParcialModels = detallesFacturaParcial.get(j - 1);
                List<DetalleDespachoAux> detalleDespachoAuxes1 = detalleDespachoAuxes.get(j - 1);
                document.newPage();
                Chunk glue2 = new Chunk(new VerticalPositionMark());
                Paragraph titulo2;
                if (doc.getEstado().equals("proforma")) {
                    titulo2 = new Paragraph("DOCUMENTO DE PROFORMA", new Font(Font.FontFamily.TIMES_ROMAN, 16,
                            Font.BOLD));
                } else {
                    titulo2 = new Paragraph("DOCUMENTO DE VENTA PARCIAL No. " + j, new Font(Font.FontFamily.TIMES_ROMAN, 16,
                            Font.BOLD));
                }
                titulo2.add(new Chunk(glue2));
                titulo2.add(facturaParcialModel.getNoDocumento());
                document.add(titulo2);

                document.add(new Paragraph(" "));
                document.add(new LineSeparator());
                //document.add(new Paragraph(" "));
                Paragraph fecha2 = new Paragraph("F. emision: " + (facturaParcialModel.getFecha()).toString(), fontdatos);
                fecha2.setAlignment(Element.ALIGN_RIGHT);
                document.add(fecha2);
                document.add(new Paragraph(" "));
                document.add(new LineSeparator());
                //document.add(new Paragraph("No Doc.: "+doc.getNoDocumento(),fontdatos));
                document.add(new Paragraph("DATOS CLIENTE ", fontdatos));
                document.add(new Paragraph("Nombre : " + doc.getCliente().getNombres(), fontdatos));
                document.add(new Paragraph("RUC/DNI: " + doc.getCliente().getDocumentoIdentidad(), fontdatos));

                document.add(new Paragraph(" "));
                document.add(new LineSeparator());


                document.add(new Paragraph("DETALLE ", fontdatos));
                document.add(new Paragraph(" "));
                PdfPTable my_report_table3 = new PdfPTable(7);

                PdfPCell table_cell3;

                table_cell3 = new PdfPCell(new Phrase("Cod Barras", fontdatos));
                my_report_table3.addCell(table_cell3);
                table_cell3 = new PdfPCell(new Phrase("Nombre Prod", fontdatos));
                my_report_table3.addCell(table_cell3);
                table_cell3 = new PdfPCell(new Phrase("Id Lote", fontdatos));
                my_report_table3.addCell(table_cell3);
                table_cell3 = new PdfPCell(new Phrase("Precio Unit.", fontdatos));
                my_report_table3.addCell(table_cell3);
                table_cell3 = new PdfPCell(new Phrase("Cantidad", fontdatos));
                my_report_table3.addCell(table_cell3);
                table_cell3 = new PdfPCell(new Phrase("Descuento", fontdatos));
                my_report_table3.addCell(table_cell3);
                table_cell3 = new PdfPCell(new Phrase("Importe", fontdatos));
                my_report_table3.addCell(table_cell3);

                for (int i = 0; i < detalleFacturaParcialModels.size(); i++) {
                    table_cell3 = new PdfPCell(new Phrase(detalleDespachoAuxes1.get(i).lote.getProducto().getCodigoBarras(), fontdatos));
                    my_report_table3.addCell(table_cell3);
                    table_cell3 = new PdfPCell(new Phrase(detalleDespachoAuxes1.get(i).lote.getProducto().getNombre(), fontdatos));
                    my_report_table3.addCell(table_cell3);
                    table_cell3 = new PdfPCell(new Phrase(detalleDespachoAuxes1.get(i).lote.getId().toString(), fontdatos));
                    my_report_table3.addCell(table_cell3);
                    table_cell3 = new PdfPCell(new Phrase(FormateadorDecimal.formatear(detalleDespachoAuxes1.get(i).precioUnitario), fontdatos));
                    my_report_table3.addCell(table_cell3);
                    table_cell3 = new PdfPCell(new Phrase(detalleDespachoAuxes1.get(i).cantidad.toString(), fontdatos));
                    my_report_table3.addCell(table_cell3);
                    table_cell3 = new PdfPCell(new Phrase(FormateadorDecimal.formatear(detalleDespachoAuxes1.get(i).descuentoUnitario/detalleDespachoAuxes1.get(i).cantidad), fontdatos));
                    my_report_table3.addCell(table_cell3);
                    table_cell3 = new PdfPCell(new Phrase(FormateadorDecimal.formatear(detalleFacturaParcialModels.get(i).getSubTotal()), fontdatos));
                    my_report_table3.addCell(table_cell3);

                }

                document.add(my_report_table3);

                document.add(new Paragraph(" "));
                document.add(new LineSeparator());


                Paragraph subtotal1 = new Paragraph("Subtotal :" + FormateadorDecimal.formatear(facturaParcialModel.getSubTotal()), fontdatos);
                subtotal1.setAlignment(Element.ALIGN_RIGHT);
                document.add(subtotal1);
                Double impuestos1 = facturaParcialModel.getImpuestos();
                Double impuestos1trunc = BigDecimal.valueOf(impuestos1)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
                Paragraph impuestosx1 = new Paragraph("Impuestos :" + impuestos1trunc.toString(), fontdatos);
                impuestosx1.setAlignment(Element.ALIGN_RIGHT);
                document.add(impuestosx1);
                Paragraph fletes1 = new Paragraph("Flete :" + FormateadorDecimal.formatear(facturaParcialModel.getFlete()), fontdatos);
                fletes1.setAlignment(Element.ALIGN_RIGHT);
                document.add(fletes1);
                Paragraph descuentos1 = new Paragraph("Descuentos :" + FormateadorDecimal.formatear(facturaParcialModel.getDescuento()), fontdatos);
                descuentos1.setAlignment(Element.ALIGN_RIGHT);
                document.add(descuentos1);
                Paragraph total1 = new Paragraph("TOTAL :" + FormateadorDecimal.formatear(facturaParcialModel.getTotal()), fontdatosbold);
                total1.setAlignment(Element.ALIGN_RIGHT);
                document.add(total1);

            }



            document.close();

            file.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generarReporteVentas(ArrayList<DocumentoModel> documentoModels) throws IOException {
        File archivosXLS = new File(SystemSingleton.getInstance().getRute() + File.separator + "Ventas" + ".xlsx");
        if (archivosXLS.exists()) {
            archivosXLS.delete();
        }
        archivosXLS.createNewFile();

        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet("hoja1");
        XSSFSheet hoja = workbook.getSheetAt(0);

        int indiceVenta = -1;
        String[] titulos = {"No DOCUMENTO", "CLIENTE", "FECHA DE EMISIÓN", "MONTO", "ESTADO"};
        Row encabezados = hoja.createRow(0);

        // Creamos el encabezado
        for (int i = 1; i <= titulos.length; i++) {
            Cell celda = encabezados.createCell(i);
            celda.setCellValue(titulos[i - 1]);
        }

        for (int i = 1; i <= documentoModels.size(); i++) {
            Row fila = hoja.createRow(i);
            Cell cell = fila.createCell(1);
            cell.setCellValue(documentoModels.get(indiceVenta + i).getNoDocumento());
            cell = fila.createCell(2);
            cell.setCellValue(documentoModels.get(indiceVenta + i).getCliente().getNombres());
            cell = fila.createCell(3);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            cell.setCellValue(sdf.format(documentoModels.get(indiceVenta + i).getFechaEmision()));
            cell = fila.createCell(4);
            cell.setCellValue(documentoModels.get(indiceVenta + i).getMontoTotal());
            cell = fila.createCell(5);
            cell.setCellValue(documentoModels.get(indiceVenta + i).getEstado());

        }
        FileOutputStream archivo = new FileOutputStream(archivosXLS);
        workbook.write(archivo);
        archivo.close();
    }
}


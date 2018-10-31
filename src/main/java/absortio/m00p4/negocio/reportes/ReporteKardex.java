package absortio.m00p4.negocio.reportes;

import absortio.m00p4.negocio.model.KardexModel;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReporteKardex {

    public void generarReporte(List<KardexModel> kardexModelList) throws IOException{
        File archivosXLS = new File(SystemSingleton.getInstance().getRute() + File.separator + "Kardex " + ".xlsx");
        if (archivosXLS.exists()) archivosXLS.delete();
        archivosXLS.createNewFile();

        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet("hoja1");
        XSSFSheet hoja = workbook.getSheetAt(0);

        int indice = -1;

        // Cabeceras
        String[] titulos = {"ID KARDEX", "FECHA", "TIPO OPERACIÓN", "MOTIVO OPERACIÓN", "CANTIDAD", "ID LOTE", "SECTOR ALMACÉN", "BLOQUE", "RACK"};
        Row encabezados = hoja.createRow(0);

        // Creamos el encabezado
        for(int i = 1; i <= titulos.length; i++) {
            Cell celda = encabezados.createCell(i);
            celda.setCellValue(titulos[i-1]);
        }

        for (int i = 1; i <= kardexModelList.size(); i++) {
            Row fila = hoja.createRow(i);
            Cell cell = fila.createCell(1);
            cell.setCellValue(kardexModelList.get(indice+i).getId());
            cell = fila.createCell(2);
            cell.setCellValue(kardexModelList.get(indice+i).getFechaoperacion().toString());
            cell = fila.createCell(3);
            cell.setCellValue(kardexModelList.get(indice+i).getMotivooperacion().getTipooperacion().getNombre());
            cell = fila.createCell(4);
            cell.setCellValue(kardexModelList.get(indice+i).getMotivooperacion().getNombre());
            cell = fila.createCell(5);
            cell.setCellValue(kardexModelList.get(indice+i).getCantidad());
            cell = fila.createCell(6);
            cell.setCellValue(kardexModelList.get(indice+i).getLote().getId());
            cell = fila.createCell(7);
            cell.setCellValue(kardexModelList.get(indice+i).getBloque().getIdRack().getIdSectorAlmacen().getNombre());
            cell = fila.createCell(8);
            cell.setCellValue(kardexModelList.get(indice+i).getBloque().getIdRack().getIdentificador());
            cell = fila.createCell(9);
            cell.setCellValue(kardexModelList.get(indice+i).getBloque().getIdentificador());
        }
        FileOutputStream archivo = new FileOutputStream(archivosXLS);
        workbook.write(archivo);
        archivo.close();
    }
}

package absortio.m00p4.negocio.reportes;


import absortio.m00p4.negocio.model.UnidadTransporteModel;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReporteUnidadTransporte {

    public void generarReporteUnidadTransporte(List<UnidadTransporteModel> transporteList) throws IOException {
         File archivosXLS = new File(SystemSingleton.getInstance().getRute() + File.separator + "Unidades de transporte" + ".xlsx");
        if (archivosXLS.exists()) archivosXLS.delete();
        archivosXLS.createNewFile();

        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet("hoja1");
        XSSFSheet hoja = workbook.getSheetAt(0);

        int indiceTransportista = -1;

        // Cabeceras
        String[] titulos = {"PLACA", "MARCA", "MODELO", "PESO SOPORTADO", "VOLUMEN SOPORTADO"};
        Row encabezados = hoja.createRow(0);

        // Creamos el encabezado
        for(int i = 1; i <= titulos.length; i++) {
            Cell celda = encabezados.createCell(i);
            celda.setCellValue(titulos[i-1]);
        }

        for (int i = 1; i <= transporteList.size(); i++) {
            Row fila = hoja.createRow(i);
            Cell cell = fila.createCell(1);
            cell.setCellValue(transporteList.get(indiceTransportista+i).getPlaca());
            cell = fila.createCell(2);
            cell.setCellValue(transporteList.get(indiceTransportista+i).getMarca());
            cell = fila.createCell(3);
            cell.setCellValue(transporteList.get(indiceTransportista+i).getModelo());
            cell = fila.createCell(4);
            cell.setCellValue(transporteList.get(indiceTransportista+i).getPesoSoportado());
            cell = fila.createCell(5);
            cell.setCellValue(transporteList.get(indiceTransportista+i).getVolumenSoportado());

        }
        FileOutputStream archivo = new FileOutputStream(archivosXLS);
        workbook.write(archivo);
        archivo.close();
    }
}

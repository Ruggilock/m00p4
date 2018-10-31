package absortio.m00p4.negocio.reportes;

import absortio.m00p4.negocio.model.FleteCapacidadModel;
import absortio.m00p4.negocio.model.FleteDistritoModel;
import absortio.m00p4.negocio.model.FleteModel;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReporteFleteDistrito {
    public void generarReporte(List<FleteDistritoModel> fleteslist) throws IOException {

        File archivosXLS = new File(SystemSingleton.getInstance().getRute() + File.separator + "Fletes distritos" + ".xlsx");
        if (archivosXLS.exists()) archivosXLS.delete();
        archivosXLS.createNewFile();

        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet("hoja1");
        XSSFSheet hoja = workbook.getSheetAt(0);

        int indice = -1;
        // Cabeceras
        String[] titulos = {"DEPARTAMENTO", "PROVINCIA", "DISTRITO", "COSTO"};
        Row encabezados = hoja.createRow(0);

        // Creamos el encabezado
        for(int i = 1; i <= titulos.length; i++) {
            Cell celda = encabezados.createCell(i);
            celda.setCellValue(titulos[i-1]);
        }

        for (int i = 1; i <= fleteslist.size(); i++) {
            Row fila = hoja.createRow(i);
            Cell cell = fila.createCell(1);
            cell.setCellValue(fleteslist.get(indice+i).getDepartamento());
            cell = fila.createCell(2);
            cell.setCellValue(fleteslist.get(indice+i).getProvincia());
            cell = fila.createCell(3);
            cell.setCellValue(fleteslist.get(indice+i).getDistrito());
            cell = fila.createCell(4);
            cell.setCellValue(fleteslist.get(indice+i).getCosto());
        }
        FileOutputStream archivo = new FileOutputStream(archivosXLS);
        workbook.write(archivo);
        archivo.close();
    }
}

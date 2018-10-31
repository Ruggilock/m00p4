package absortio.m00p4.negocio.reportes;

import absortio.m00p4.negocio.model.FleteCapacidadModel;
import absortio.m00p4.negocio.model.FleteDistritoModel;
import absortio.m00p4.negocio.model.FleteModel;
import absortio.m00p4.negocio.model.auxiliares.Flete;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReporteFlete {

    public void generarReporteRol(List<FleteDistritoModel> fleteslist) throws IOException {

        File archivosXLS = new File(SystemSingleton.getInstance().getRute() + File.separator + "Fletes" + ".xlsx");
        if (archivosXLS.exists()) archivosXLS.delete();
        archivosXLS.createNewFile();

        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet("hoja1");
        XSSFSheet hoja = workbook.getSheetAt(0);

        int indice = -1;
        // Cabeceras
        String[] titulos = {"DEPARTAMENTO", "PROVINCIA", "DISTRITO", "CAPACIDAD", "PESO MÍNIMO", "PESO MÁXIMO", "UNIDAD PESO", "VOLUMEN MÍNIMO", "VOLUMEN MÁXIMO", "UNIDAD VOLUMEN", "COSTO"};
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
            List<FleteModel>fletes=fleteslist.get(indice+i).getFletes();
            for(int j=0;j<fletes.size();j++){
                FleteCapacidadModel capacidad=fletes.get(j).getCapacidad();
                cell = fila.createCell(5);
                cell.setCellValue(capacidad.getPesoMin());
                cell = fila.createCell(6);
                cell.setCellValue(capacidad.getPesoMax());
                cell = fila.createCell(7);
                cell.setCellValue(capacidad.getUnidadPeso());
                cell = fila.createCell(8);
                cell.setCellValue(capacidad.getUnidadvolumen());
                cell = fila.createCell(9);
                cell.setCellValue(capacidad.getVolumenMin());
                cell = fila.createCell(10);
                cell.setCellValue(capacidad.getVolumenMax());
                cell = fila.createCell(11);
                cell.setCellValue(capacidad.getUnidadvolumen());
                cell = fila.createCell(12);
                cell.setCellValue(capacidad.getCosto());
            }
        }
        FileOutputStream archivo = new FileOutputStream(archivosXLS);
        workbook.write(archivo);
        archivo.close();
    }

}

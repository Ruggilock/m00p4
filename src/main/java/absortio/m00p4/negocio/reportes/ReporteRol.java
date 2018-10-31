package absortio.m00p4.negocio.reportes;

import absortio.m00p4.negocio.model.PermisoModel;
import absortio.m00p4.negocio.model.RolModel;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReporteRol {

    public void generarReporteRol(List<RolModel> rolModelList) throws IOException {

        File archivosXLS = new File(SystemSingleton.getInstance().getRute() + File.separator + "Roles" + ".xlsx");
        if (archivosXLS.exists()) archivosXLS.delete();
        archivosXLS.createNewFile();

        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet("hoja1");
        XSSFSheet hoja = workbook.getSheetAt(0);

        int indice = -1;
        // Cabeceras
        String[] titulos = {"NOMBRE", "DESCRIPCIÓN", "PERMISOS" };
        Row encabezados = hoja.createRow(0);

        // Creamos el encabezado
        for(int i = 1; i <= titulos.length; i++) {
            Cell celda = encabezados.createCell(i);
            celda.setCellValue(titulos[i-1]);
        }

        for (int i = 1; i <= rolModelList.size(); i++) {
            Row fila = hoja.createRow(i);
            Cell cell = fila.createCell(1);
            cell.setCellValue(rolModelList.get(indice+i).getNombre());
            cell = fila.createCell(2);
            cell.setCellValue(rolModelList.get(indice+i).getDescripcion());
            cell = fila.createCell(3);
            String permisos=permisosToString(rolModelList.get(indice+i).getPermisos());
            cell.setCellValue(permisos);
        }
        FileOutputStream archivo = new FileOutputStream(archivosXLS);
        workbook.write(archivo);
        archivo.close();
    }

    private String permisosToString(List<PermisoModel> permisos){
        String cadena="";
        int cant = permisos.size();
        if (cant > 0){
            cadena = permisos.get(0).getNombre();

            for(int z=1; z< permisos.size();z++){
                cadena=cadena+"// "+permisos.get(z).getNombre();
            }
        }

        return  cadena;
    }
}

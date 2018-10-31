package absortio.m00p4.negocio.reportes;


import absortio.m00p4.negocio.model.UsuarioModel;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReporteEmpleado {

    public void generarReporteEmpleado(List<UsuarioModel> usuarioList) throws IOException {
        File archivosXLS = new File(SystemSingleton.getInstance().getRute() + File.separator + "Empleados" + ".xlsx");
        if (archivosXLS.exists()) archivosXLS.delete();
        archivosXLS.createNewFile();

        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet("hoja1");
        XSSFSheet hoja = workbook.getSheetAt(0);

        int indiceUsuario = -1;

        // Cabeceras
        String[] titulos = {"DOCUMENTO", "NOMBRE", "CORREO", "ROL" };
        Row encabezados = hoja.createRow(0);

        // Creamos el encabezado
        for(int i = 1; i <= titulos.length; i++) {
            Cell celda = encabezados.createCell(i);
            celda.setCellValue(titulos[i-1]);
        }

        for (int i = 1; i <= usuarioList.size(); i++) {
            Row fila = hoja.createRow(i);
            Cell cell = fila.createCell(1);
            cell.setCellValue(usuarioList.get(indiceUsuario+i).getDocumentoIdentidad());
            cell = fila.createCell(2);
            cell.setCellValue(usuarioList.get(indiceUsuario+i).getNombreCompleto());
            cell = fila.createCell(3);
            cell.setCellValue(usuarioList.get(indiceUsuario+i).getCorreo());
            cell = fila.createCell(4);
            cell.setCellValue(usuarioList.get(indiceUsuario+i).getIdRol().getNombre());

        }
        FileOutputStream archivo = new FileOutputStream(archivosXLS);
        workbook.write(archivo);
        archivo.close();
    }
}

package absortio.m00p4.negocio.reportes;

import absortio.m00p4.negocio.model.ClienteModel;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.List;

public class ReporteCliente {

    public void generarReporteCliente(List<ClienteModel> clienteList) throws IOException {
         File archivosXLS = new File(SystemSingleton.getInstance().getRute() + File.separator + "Clientes" + ".xlsx");
        if (archivosXLS.exists()) archivosXLS.delete();
        archivosXLS.createNewFile();

        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet("hoja1");
        XSSFSheet hoja = workbook.getSheetAt(0);

        int indiceClientes = -1;

        // Cabeceras
        String[] titulos = {"TIPO DOCUMENTO", "DOCUMENTO", "NOMBRE", "CORREO", "TELÉFONO", "DIRECCION"};
        Row encabezados = hoja.createRow(0);

        // Creamos el encabezado
        for(int i = 1; i <= titulos.length; i++) {
            Cell celda = encabezados.createCell(i);
            celda.setCellValue(titulos[i-1]);
        }

        // Data
        for (int i = 1; i <= clienteList.size(); i++) {
            Row fila = hoja.createRow(i);
            Cell cell = fila.createCell(1);
            cell.setCellValue(clienteList.get(indiceClientes+i).getTipoDocumento());
            cell = fila.createCell(2);
            cell.setCellValue(clienteList.get(indiceClientes+i).getDocumentoIdentidad());
            cell = fila.createCell(3);
            cell.setCellValue(clienteList.get(indiceClientes+i).getNombres());
            cell = fila.createCell(4);
            cell.setCellValue(clienteList.get(indiceClientes+i).getCorreo());
            cell = fila.createCell(5);
            cell.setCellValue(clienteList.get(indiceClientes+i).getTelefono());
            cell = fila.createCell(6);
            cell.setCellValue(clienteList.get(indiceClientes+i).getDireccion());
        }
        FileOutputStream archivo = new FileOutputStream(archivosXLS);
        workbook.write(archivo);
        archivo.close();
    }
}

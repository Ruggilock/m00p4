package absortio.m00p4.negocio.reportes;


import absortio.m00p4.negocio.model.TransportistaModel;
import absortio.m00p4.negocio.model.UsuarioModel;
import absortio.m00p4.negocio.model.auxiliares.Transportista;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReporteTransportista {

    public void generarReporteTransportista(List<TransportistaModel> transportistaList) throws IOException {
        File archivosXLS = new File(SystemSingleton.getInstance().getRute() + File.separator + "Transportistas" + ".xlsx");
        if (archivosXLS.exists()) archivosXLS.delete();
        archivosXLS.createNewFile();

        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet("hoja1");
        XSSFSheet hoja = workbook.getSheetAt(0);

        int indiceTransportista = -1;
        // Cabeceras
        String[] titulos = {"TIPO DOCUMENTO", "DOCUMENTO", "NOMBRE", "TELEFONO"};
        Row encabezados = hoja.createRow(0);

        // Creamos el encabezado
        for(int i = 1; i <= titulos.length; i++) {
            Cell celda = encabezados.createCell(i);
            celda.setCellValue(titulos[i-1]);
        }

        for (int i = 1; i <= transportistaList.size(); i++) {
            Row fila = hoja.createRow(i);
            Cell cell = fila.createCell(1);
            cell.setCellValue(transportistaList.get(indiceTransportista+i).getTipoDocumento());
            cell = fila.createCell(2);
            cell.setCellValue(transportistaList.get(indiceTransportista+i).getDocumentoIdentidad());
            cell = fila.createCell(3);
            cell.setCellValue(transportistaList.get(indiceTransportista+i).getNombreCompleto());
            cell = fila.createCell(4);
            cell.setCellValue(transportistaList.get(indiceTransportista+i).getTelefono());
        }

        FileOutputStream archivo = new FileOutputStream(archivosXLS);
        workbook.write(archivo);
        archivo.close();
    }
}

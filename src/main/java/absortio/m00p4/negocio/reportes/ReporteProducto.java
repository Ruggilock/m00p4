package absortio.m00p4.negocio.reportes;


import absortio.m00p4.negocio.model.ProductoModel;
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

public class ReporteProducto {

    public void generarReporteProductos(List<ProductoModel> productoList) throws IOException {
         File archivosXLS = new File(SystemSingleton.getInstance().getRute() + File.separator + "Productos" + ".xlsx");
        if (archivosXLS.exists()) archivosXLS.delete();
        archivosXLS.createNewFile();

        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet("hoja1");
        XSSFSheet hoja = workbook.getSheetAt(0);

        int indiceProducto = -1;

        // Cabeceras
        String[] titulos = {"CODIGO BARRAS", "NOMBRE", "DESCRIPCIÓN", "CATEGORÍA", "MARCA", "MODELO"};
        Row encabezados = hoja.createRow(0);

        // Creamos el encabezado
        for(int i = 1; i <= titulos.length; i++) {
            Cell celda = encabezados.createCell(i);
            celda.setCellValue(titulos[i-1]);
        }

        for (int i = 1; i <= productoList.size(); i++) {
            Row fila = hoja.createRow(i);
            Cell cell = fila.createCell(1);
            cell.setCellValue(productoList.get(indiceProducto+i).getCodigoBarras());
            cell = fila.createCell(2);
            cell.setCellValue(productoList.get(indiceProducto+i).getNombre());
            cell = fila.createCell(3);
            cell.setCellValue(productoList.get(indiceProducto+i).getDescripcion());
            cell = fila.createCell(4);
            cell.setCellValue(productoList.get(indiceProducto + i).getCategoriaProducto().getNombre());
            cell = fila.createCell(5);
            cell.setCellValue(productoList.get(indiceProducto+i).getMarca());
            cell = fila.createCell(6);
            cell.setCellValue(productoList.get(indiceProducto+i).getModelo());
        }

        FileOutputStream archivo = new FileOutputStream(archivosXLS);
        workbook.write(archivo);
        archivo.close();
    }
}

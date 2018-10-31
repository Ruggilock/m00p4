package absortio.m00p4.negocio.reportes;

import absortio.m00p4.negocio.model.*;
import absortio.m00p4.negocio.model.auxiliares.ProductosAlmacen;
import absortio.m00p4.negocio.repository.KardexRepository;
import absortio.m00p4.negocio.service.KardexService;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import javafx.fxml.Initializable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ReporteProductosAlmacen {

    @Autowired
    KardexService proAlmacenService;
    @Autowired
    private ApplicationContext context;

    public void generarReporte(List<KardexModel> proAlmacenModels) throws IOException{
        File archivosXLS = new File(SystemSingleton.getInstance().getRute() + File.separator + "Productos Almacen " + ".xlsx");
        if (archivosXLS.exists()) archivosXLS.delete();
        archivosXLS.createNewFile();

        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet("hoja1");
        XSSFSheet hoja = workbook.getSheetAt(0);

        int indice = -1;

        // Cabeceras
        String[] titulos = {"LOTE", "BLOQUE", "COD BARRAS", "PRODUCTO", "SECTOR ALMACÉN", "RACK", "CATEGORIA", "MARCA", "MODELO", "STOCK FÍSICO"};
        Row encabezados = hoja.createRow(0);

        // Creamos el encabezado
        for(int i = 1; i <= titulos.length; i++) {
            Cell celda = encabezados.createCell(i);
            celda.setCellValue(titulos[i-1]);
        }


        List<String> mylista = new ArrayList<>();
        String myid = "";

        if (!proAlmacenModels.isEmpty()) {
            for (int i = 1; i <= proAlmacenModels.size(); i++) {
                BloqueModel bloqueAux = new BloqueModel();
                bloqueAux = proAlmacenModels.get(indice+i).getBloque();
                RackModel rackAux = new RackModel();
                rackAux = bloqueAux.getIdRack();
                SectorAlmacenModel sectorAux = new SectorAlmacenModel();
                sectorAux = rackAux.getIdSectorAlmacen();
                LoteModel loteAux = new LoteModel();
                loteAux = proAlmacenModels.get(indice+i).getLote();
                ProductoModel productoAux = new ProductoModel();
                productoAux = loteAux.getProducto();

                myid = loteAux.getId().toString() + bloqueAux.getId().toString();
                if (!mylista.contains(myid) ) {
                    Row fila = hoja.createRow(mylista.size() + 1);
                    Cell cell = fila.createCell(1);
                    cell.setCellValue(loteAux.getObservacion());
                    cell = fila.createCell(2);
                    cell.setCellValue(bloqueAux.getIdentificador());
                    cell = fila.createCell(3);
                    cell.setCellValue(productoAux.getCodigoBarras());
                    cell = fila.createCell(4);
                    cell.setCellValue(productoAux.getNombre());
                    cell = fila.createCell(5);
                    cell.setCellValue(sectorAux.getNombre());
                    cell = fila.createCell(6);
                    cell.setCellValue(rackAux.getIdentificador());
                    cell = fila.createCell(7);
                    cell.setCellValue(productoAux.getCategoriaProducto().getNombre());
                    cell = fila.createCell(8);
                    cell.setCellValue(productoAux.getMarca());
                    cell = fila.createCell(9);
                    cell.setCellValue(productoAux.getModelo());
                    cell = fila.createCell(10);

                    cell.setCellValue(0);

                    mylista.add(myid);
                }
            }
        }

        FileOutputStream archivo = new FileOutputStream(archivosXLS);
        workbook.write(archivo);
        archivo.close();
    }


}



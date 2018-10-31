package absortio.m00p4.negocio.ui.menu.almacenes;

import absortio.m00p4.negocio.model.*;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.service.*;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import absortio.m00p4.negocio.ui.menu.almacenes.funciones.FuncionesMapa;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class CargarMapaController {

    @Autowired
    MapaService mapaService;
    @Autowired
    SectorAlmacenService sectorAlmacenService;
    @Autowired
    RackService rackService;
    @Autowired
    BloqueService bloqueService;
    @Autowired
    PisoService pisoService;
    @Autowired
    FuncionesMapa llenarMapa;
    @Autowired
    PermisoService permisoService;
    @FXML
    Button botonCargarMapa;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;


    @FXML
    void clickCargarMapa(MouseEvent event) throws IOException {
        UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("almacenCrear", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            String logMensaje="El usuario "+ UsuarioSingleton.getInstance().getUsuarioModel().getNombres()+ "intento cargar mapa sin permisos";
            LOGGERAUDIT.info(logMensaje);
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }


        if (SystemSingleton.getInstance().getRute() == null) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = directoryChooser.showDialog(null);
            if (file.getPath() != null) {
                SystemSingleton.getInstance().setRute(file.getPath());
            }
        }
        if (SystemSingleton.getInstance().getRute() != null) {
            leerExcel();
            botonCargarMapa.setDisable(true);
        }
    }

    private void leerExcel() throws IOException {
        String ruta = SystemSingleton.getInstance().getRute() + File.separator + "mapa" + ".xlsx";
        InputStream file = new FileInputStream(ruta);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        Sheet hoja0 = workbook.getSheetAt(0);

        Integer inicioDeRacks = 1;

        Row descripcion = hoja0.getRow(1);
        String h0descripcion=descripcion.getCell(1).toString();
        Row ancho = hoja0.getRow(2);
        Integer h0Ancho = (int) ancho.getCell(1).getNumericCellValue();
        Row largo = hoja0.getRow(3);
        Integer h0Largo = (int) largo.getCell(1).getNumericCellValue();
        Row numeroAlmacen = hoja0.getRow(4);
        Integer h0NumeroAlmacen = (int)numeroAlmacen.getCell(1).getNumericCellValue();
        Row puerta = hoja0.getRow(5);
        Integer h0PuertaX = (int)puerta.getCell(1).getNumericCellValue();
        Integer h0PuertaY = (int)puerta.getCell(2).getNumericCellValue();

        MapaModel mapaModelInsert = new MapaModel();
        mapaModelInsert.setImagen("mapa.xlsx");
        mapaModelInsert.setAncho(h0Ancho);
        mapaModelInsert.setLargo(h0Largo);
        mapaModelInsert.setDescripcion(h0descripcion);
        mapaModelInsert.setRuta(ruta);
        mapaModelInsert.setEstado("activo");
        mapaModelInsert.setPuertax(h0PuertaX);
        mapaModelInsert.setPuertay(h0PuertaY);

        String idGeneradoMapa= crearId(mapaService.findAllMapa().size()+1);
        for (int i = 1; i <=h0NumeroAlmacen; i++) {
            Sheet hoja1 = workbook.getSheetAt(1);
            Row linea= hoja1.getRow(i);
            String h1Nombre=linea.getCell(1).toString();
            String h1Descripcion=linea.getCell(2).toString();
            String h1Tipo=linea.getCell(3).toString();
            String h1Orientacion=linea.getCell(4).toString();
            Integer h1XIni = (int)linea.getCell(5).getNumericCellValue();
            Integer h1YIni = (int)linea.getCell(6).getNumericCellValue();
            Integer h1XFin= (int)linea.getCell(7).getNumericCellValue();
            Integer h1YFin = (int)linea.getCell(8).getNumericCellValue();
            Integer h1NumRack = (int)linea.getCell(9).getNumericCellValue();

            String idGeneradoAlmacen = crearId(i);

            SectorAlmacenModel sectorAlmacenModelInsert = new SectorAlmacenModel();
            sectorAlmacenModelInsert.setNombre(h1Nombre);
            sectorAlmacenModelInsert.setDescripcion(h1Descripcion);
            sectorAlmacenModelInsert.setTipo(h1Tipo);
            sectorAlmacenModelInsert.setPosXIni(h1XIni);
            sectorAlmacenModelInsert.setPosYIni(h1YIni);
            sectorAlmacenModelInsert.setPosXFin(h1XFin);
            sectorAlmacenModelInsert.setPosYFin(h1YFin);
            sectorAlmacenModelInsert.setOrientacion(h1Orientacion);
            sectorAlmacenModelInsert.setIdentificador(idGeneradoMapa + idGeneradoAlmacen);
            sectorAlmacenModelInsert.setEstado("activo");
            mapaModelInsert.addSectorAlmacen(sectorAlmacenModelInsert);
            Sheet hojaRack = workbook.getSheetAt(inicioDeRacks + i);
            for (int j = 1; j <= h1NumRack; j++) {
                Row lineaRack = hojaRack.getRow(j);
                Integer hRXIni = (int) lineaRack.getCell(1).getNumericCellValue();
                Integer hRYIni = (int) lineaRack.getCell(2).getNumericCellValue();
                Integer hRTamanio = (int) lineaRack.getCell(3).getNumericCellValue();
                Integer hRPisos = (int) lineaRack.getCell(4).getNumericCellValue();
                String idGeneradoRack = crearId(j);
                RackModel rackModelInsert = new RackModel();
                //rackModelInsert.setIdSectorAlmacen(sectorAlmacenModel);
                rackModelInsert.setOrientacion(h1Orientacion);
                rackModelInsert.setTamanio(hRTamanio);
                rackModelInsert.setPosXIni(hRXIni);
                rackModelInsert.setPosXFin(hRXIni + hRTamanio - 1);
                rackModelInsert.setPosYIni(hRYIni);
                rackModelInsert.setPosYFin(hRYIni + hRTamanio - 1);
                rackModelInsert.setPisos(hRPisos);
                rackModelInsert.setIdentificador(sectorAlmacenModelInsert.getIdentificador() + idGeneradoRack);
                rackModelInsert.setEstado("activo");
                sectorAlmacenModelInsert.addRack(rackModelInsert);

                llenarRack(hRTamanio,hRPisos,rackModelInsert,hRXIni,hRYIni,h1Orientacion);
            }
        }
        mapaService.save(mapaModelInsert);

        llenarMapa.crearMatriz(mapaModelInsert);
        llenarMapa.llenarMapa(mapaModelInsert);
        SystemSingleton.getInstance().setMapa(mapaModelInsert);
        llenarMapa.printMatriz(mapaModelInsert);

    }

    private void llenarRack(Integer tamanio, Integer pisos, RackModel idRack,
                            Integer posXIni, Integer posYIni, String orientacion) {
        for (int posicion = 0; posicion < tamanio; posicion++) {
            for (int altura = 1; altura <=pisos; altura++)
            {
                crearBloque(altura,idRack,idRack.getIdentificador(),posXIni,posYIni,posicion,orientacion);
            }
        }
    }
    private void crearBloque(Integer altura , RackModel idRack,String identificadorRack,Integer posXini,
                                Integer posYIni,Integer posicion, String orientacion){
        PisoModel piso = pisoService.getById(altura);
        String identificadorBloqueA = identificadorRack + "A" + posicion + piso.getTipo();
        String identificadorBloqueB = identificadorRack + "B" + posicion + piso.getTipo();
        BloqueModel bloque;
        BloqueModel bloque2;
        bloque = new BloqueModel();
        bloque.setPiso(piso);
        //bloque.setIdRack(idRack);
        bloque.setIdentificador(identificadorBloqueA);
        bloque.setPosZ(piso.getId());
        bloque.setPesoDisponible(piso.getMaxPeso());
        bloque.setVolumenDisponible(piso.getMaxVolumen());
        bloque.setUnidadPeso("kg");
        bloque.setUnidadVolumen("m3");
        bloque.setEstado("activo");

        bloque2 = new BloqueModel();
        bloque2.setPiso(piso);
        bloque2.setIdentificador(identificadorBloqueB);
        bloque2.setPosZ(piso.getId());
        bloque2.setPesoDisponible(piso.getMaxPeso());
        bloque2.setVolumenDisponible(piso.getMaxVolumen());
        bloque2.setUnidadPeso("kg");
        bloque2.setUnidadVolumen("m3");
        bloque2.setEstado("activo");

        if(orientacion.compareTo("H")==0){
            bloque.setPosX(posXini+posicion);
            bloque.setPosY(posYIni);

            bloque2.setPosX(posXini+posicion);
            bloque2.setPosY(posYIni+1);

        }else{
            bloque.setPosX(posXini);
            bloque.setPosY(posYIni+posicion);

            bloque2.setPosX(posXini+1);
            bloque2.setPosY(posYIni+posicion);

        }
        idRack.addBloque(bloque);
        idRack.addBloque(bloque2);


    }
    private String crearId(Integer contador) {
        String resultado ="";
        Integer longitud = (int) (Math.log10(contador) + 1);
        switch (longitud) {
            case 1: {
                resultado = "00" + contador;
                break;
            }
            case 2: {
                resultado= "0" + contador;
                break;
            }
            case 3: {
                resultado = "" + contador;
                break;
            }
        }
        return resultado;
    }


}

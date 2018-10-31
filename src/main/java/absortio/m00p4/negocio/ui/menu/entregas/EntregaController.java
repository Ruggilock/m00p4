package absortio.m00p4.negocio.ui.menu.entregas;

import absortio.m00p4.negocio.model.auxiliares.Entrega;
import absortio.m00p4.negocio.model.auxiliares.Producto;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

@Component
public class EntregaController implements Initializable {

    @FXML
    Pane paneEntrega;

    @FXML
    DatePicker fechaEntrega;

    @FXML
    private TableView<Entrega> tablaProductos;

    @FXML
    private TableColumn<Entrega, Integer> columnaN;
    @FXML
    private TableColumn<Entrega, String> columnaProducto;
    @FXML
    private TableColumn<Entrega, Integer> columnaCantidad;
    @FXML
    private TableColumn<Entrega, Spinner> columnaCantidadEntrega;

    HashMap<Integer,Integer>cantidadesPedidas;
    HashMap<Integer,Integer>cantidadesProgramadas;

    ObservableList<Entrega> entregas;



    @Autowired
    private ApplicationContext context ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entregas= FXCollections.observableArrayList (  );
        cantidadesPedidas= new HashMap<>();
        cantidadesProgramadas= new HashMap<>();
        fechaEntrega.setValue(LocalDate.now());
        inicializarTablaProductos ();
    }



    private void inicializarTablaProductos() {

        columnaN.setCellValueFactory ( new PropertyValueFactory<Entrega, Integer> ( "n" ) );
        columnaProducto.setCellValueFactory ( new PropertyValueFactory<Entrega, String> ( "nombre" ) );
        columnaCantidad.setCellValueFactory ( new PropertyValueFactory<Entrega, Integer> ( "cantPedida" ) );
        columnaCantidadEntrega.setCellValueFactory ( new PropertyValueFactory<Entrega, Spinner> ( "spinner" ) );
        tablaProductos.setItems ( entregas );
    }
    private void cargarGrilla() {
        tablaProductos.setItems ( entregas );
    }


    @FXML
    public  void asignarProductosYEntregas(ObservableList<Entrega>entregasProgramadas ){
/*        this.cantidadesPedidas=cantidadesPedidas;
        this.cantidadesProgramadas=cantidadesProgramadas;*/
        for (int i = 0; i < entregasProgramadas.size () ; i++) {
            Entrega entrega= new Entrega ();
            entrega.setN ( entregasProgramadas.get ( i ).getN () );
            entrega.setCodigo ( entregasProgramadas.get ( i ).getCodigo () );
            entrega.setNombre ( entregasProgramadas.get ( i ).getNombre () );
            entrega.setCantPedida (  entregasProgramadas.get ( i ).getCantPedida () );
            entrega.setCantidadEntrega (entregasProgramadas.get ( i ).getSpinner ().getValue ());
            //Integer a=this.cantidadesPedidas.get(entrega.getN())-this.cantidadesProgramadas.get(entrega.getN());
            Spinner<Integer> spin = new Spinner<> ();
            SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory ( 0, entrega.getCantPedida(), 0 );
            spin.setEditable(true);
            spin.setValueFactory ( spinnerValueFactory );
            entrega.setSpinner ( spin );
            entregas.add ( entrega );
        }
        cargarGrilla ();
    }

    @FXML
    public ObservableList<Entrega> asignarEntrega() {
        ObservableList<Entrega> entregasAsignadas=FXCollections.observableArrayList();
        for (int i = 0; i < entregas.size () ; i++) {
            Entrega e= new Entrega();
            e.setN(entregas.get ( i ).getN());
            e.setCodigo(entregas.get ( i ).getCodigo());
            e.setNombre(entregas.get ( i ).getNombre());
            e.setCantPedida(entregas.get(i).getCantPedida());
            e.setCantidadEntrega (entregas.get ( i ).getSpinner ().getValue ());
            if(fechaEntrega.getValue ()!=null) {
                java.sql.Date dpCurrentDate1 = java.sql.Date.valueOf ( fechaEntrega.getValue () );
                e.setFechaEntrega ( dpCurrentDate1 );
            }
            entregasAsignadas.add(e);
        }
        return entregasAsignadas;


    }
/*    @FXML
    public  void setCantidadProgramada(){
        for (Entrega ent:entregas
             ) {
            ent.setCantidadEntrega(ent.getSpinner().getValue());
            cantidadesProgramadas.put(ent.getN(),ent.getCantidadEntrega());
        }
    }*/


}

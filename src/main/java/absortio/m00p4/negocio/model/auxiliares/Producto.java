package absortio.m00p4.negocio.model.auxiliares;

import absortio.m00p4.negocio.service.CondicionesComercialesService;
import absortio.m00p4.negocio.service.ProductoService;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import org.omg.CORBA.PRIVATE_MEMBER;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import java.util.Date;

public class Producto {
    @Autowired
    CondicionesComercialesService condicionService;
    @Autowired
    ProductoService productoService;

    private Integer n;
    private String codigo;
    private String nombre;
    private String marca;
    private String categoria;
    private String modelo;
    private String descripcion;
    private Double precio;
    private String unidadVenta;
    private Double descuento;
    private Double importe;
    private Integer cantidadMax;

    private Integer idRegalo;

    private Integer codigoP;
    private Integer cantP;
    private Integer cantidadEntrega;
    private Date fechaEntrega;

    private ComboBox<String> comboBox;
    private Button buttonEliminar;


    public Spinner<Integer> getSpinner() {
        return spinner;
    }

    public void setSpinner(Spinner<Integer> spinner) {
        this.spinner = spinner;
    }

    private Spinner<Integer> spinner;

    public Producto(){
        idRegalo = 0;
        setComboBox(new ComboBox<>());{

            for (int i = 0; i < 30; i++) comboBox.getItems ().add ( String.valueOf ( i ) );
        }
          comboBox.setEditable ( true );
//        comboBox.setOnAction((event2 -> {
//
//                }));
        spinner= new Spinner<> (  ) ;
        SpinnerValueFactory spinnerValueFactory=new SpinnerValueFactory.IntegerSpinnerValueFactory ( 0,1000,0 );
        spinner.setEditable ( true );
        spinner.setValueFactory ( spinnerValueFactory );

        setButtonEliminar(new Button());
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public  void asignarObjeto(Producto producto){
        this.categoria = producto.categoria;
        this.codigo = producto.codigo;
        this.marca = producto.marca;
        this.modelo = producto.modelo;
        this.n = producto.n;
        this.nombre = producto.nombre;
        this.setUnidadVenta(producto.getUnidadVenta());

    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getUnidadVenta() {
        return unidadVenta;
    }

    public void setUnidadVenta(String unidadVenta) {
        this.unidadVenta = unidadVenta;
    }

    /*public Double calcularDescuento(int cantidad) {//solo tocar para nueve venta
        //ya deberia estar definido el precio

        ArrayList<CondicionesComercialesModel> condicionModels =(ArrayList)condicionService.findAllCondicion();
        Double desc = 0;
        int indicador1=0,indicador2=0,indicador3=0,indicador4=0,id,cant1,cant2;
        String categoria;
        for (int i = 0; i<condicionModels.size();i++){
            if(condicionModels.get(i).getTipo().compareTo("cantidad*cantidad")==0){//indicador1,2x1
                id = productoService.hallarIdByCodigoBarras(codigo);
                if(id==condicionModels.get(i).getCodigo1() && indicador1 ==0){
                    indicador1 = 1;
                    cant1 = condicionModels.get(i).getCantidad1();
                    cant2 = condicionModels.get(i).getCantidad2();
                    desc += (cant1-cant2)*(cantidad/cant1)*precio;

                }
            }else if (condicionModels.get(i).getTipo().compareTo("cantidad*gratis")==0){//indicador2,100x1
                id = productoService.hallarIdByCodigoBarras(codigo);
                if (id==condicionModels.get(i).getCodigo1()&& indicador2 ==0){
                    cant1 = cantidad/condicionModels.get(i).getCantidad1();
                    if(cant1>0){
                        indicador2=1;
                        codigoP = condicionModels.get(i).getCodigo2();
                        cantP = cant1*condicionModels.get(i).getCantidad2();

                    }
                }

            }else if(condicionModels.get(i).getTipo().compareTo("porcentaje*producto")==0 && indicador3==0){//indicador3


                id = productoService.hallarIdByCodigoBarras(codigo);
                if(id==condicionModels.get(i).getCodigo1()&& indicador3 == 0){
                    indicador3 = 1;
                    desc += cantidad*precio*condicionModels.get(i).getPorcentaje();

                }

            }else if(condicionModels.get(i).getTipo().compareTo("porcentaje*categoria")==0 && indicador4==0){//indicador4

                categoria = productoService.hallarNombreCategoria(codigo);
                if (categoria.compareTo(condicionModels.get(i).getCategoria())==0){
                    indicador4 = 1;
                    desc += cantidad*precio*condicionModels.get(i).getPorcentaje();
                }

            }

            if(indicador3 == 1 && indicador4 ==1) break;

        }
        descuento = desc;

        return desc;
    }*/

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getDescuento() {
        return descuento;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }


    public ComboBox<String> getComboBox() {
        return comboBox;
    }

    public void setComboBox(ComboBox<String> comboBox) {
        this.comboBox = comboBox;
    }

    public Integer getCantidadMax() {
        return cantidadMax;
    }

    public void setCantidadMax(Integer cantidadMax) {
        this.cantidadMax = cantidadMax;
    }
    public Integer getCodigoP() {
        return codigoP;
    }

    public void setCodigoP(Integer codigoP) {
        this.codigoP = codigoP;
    }

    public Integer getCantP() {
        return cantP;
    }

    public void setCantP(Integer cantP) {
        this.cantP = cantP;
    }

    public Integer getCantidadEntrega() {
        return cantidadEntrega;
    }

    public void setCantidadEntrega(Integer cantidadEntrega) {
        this.cantidadEntrega = cantidadEntrega;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Button getButtonEliminar() {
        return buttonEliminar;
    }

    public void setButtonEliminar(Button buttonEliminar) {
        this.buttonEliminar = buttonEliminar;
    }

    public Integer getIdRegalo() {
        return idRegalo;
    }

    public void setIdRegalo(Integer idRegalo) {
        this.idRegalo = idRegalo;
    }
}

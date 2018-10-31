package absortio.m00p4.negocio.model.auxiliares;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class DetalleFacturaParcial {
    private  Integer n;
    private String codigobarras;
    private Integer lote;
    private String nombreproducto;
    private Integer cantidad;
    private Double preciounitario;
    private  Double descuentounitario;
    private  Integer id;
    private Double importe;

    private ComboBox<String> comboBox;

    private Button buttonEliminar;

    private TextField textfield;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public String getCodigobarras() {
        return codigobarras;
    }

    public void setCodigobarras(String codigobarras) {
        this.codigobarras = codigobarras;
    }

    public Integer getLote() {
        return lote;
    }

    public void setLote(Integer lote) {
        this.lote = lote;
    }

    public String getNombreproducto() {
        return nombreproducto;
    }

    public void setNombreproducto(String nombreproducto) {
        this.nombreproducto = nombreproducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPreciounitario() {
        return preciounitario;
    }

    public void setPreciounitario(Double preciounitario) {
        this.preciounitario = preciounitario;
    }

    public Double getDescuentounitario() {
        return descuentounitario;
    }

    public void setDescuentounitario(Double descuentounitario) {
        this.descuentounitario = descuentounitario;
    }

    public ComboBox<String> getComboBox() {
        return comboBox;
    }

    public void setComboBox(ComboBox<String> comboBox) {
        this.comboBox = comboBox;
    }

    public Button getButtonEliminar() {
        return buttonEliminar;
    }

    public void setButtonEliminar(Button buttonEliminar) {
        this.buttonEliminar = buttonEliminar;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TextField getTextfield() {
        return textfield;
    }

    public void setTextfield(TextField textfield) {
        this.textfield = textfield;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public void asignarObjeto(DetalleFacturaParcial detalledoc){
        this.setN(detalledoc.getN());
        this.setPreciounitario(detalledoc.getPreciounitario());
        this.setDescuentounitario(detalledoc.getDescuentounitario());
        this.setNombreproducto(detalledoc.getNombreproducto());
        this.setCantidad(detalledoc.getCantidad());
        this.setLote(detalledoc.getLote());
        this.setCodigobarras(detalledoc.getCodigobarras());
        this.setComboBox(new ComboBox<>());
        this.setButtonEliminar(new Button());
        this.setTextfield(new TextField());
        this.setImporte(0.0);
    }
}



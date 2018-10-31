package absortio.m00p4.negocio.model.auxiliares;

import absortio.m00p4.negocio.model.ProductoModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Coordenada;
import javafx.scene.control.Button;

import java.util.ArrayList;

public class Simulacion {
    private String name;
    private Double puntuacion;
    private Button buttonDescargar;
    private Button buttonVisualizar;
    private Integer id;
    private String rutaSerializada;
    private ArrayList<ArrayList<Coordenada>> coordenadas;
    private ArrayList<ProductoModel> productos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Double puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Button getButtonDescargar() {
        return buttonDescargar;
    }

    public void setButtonDescargar(Button buttonDescargar) {
        this.buttonDescargar = buttonDescargar;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRutaSerializada() {
        return rutaSerializada;
    }

    public void setRutaSerializada(String rutaSerializada) {
        this.rutaSerializada = rutaSerializada;
    }

    public Button getButtonVisualizar() {
        return buttonVisualizar;
    }

    public void setButtonVisualizar(Button buttonVisualizar) {
        this.buttonVisualizar = buttonVisualizar;
    }

    public ArrayList<ArrayList<Coordenada>> getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(ArrayList<ArrayList<Coordenada>> coordenadas) {
        this.coordenadas = coordenadas;
    }

    public void setProductos(ArrayList<ProductoModel> productos) {
        this.productos = productos;
    }

    public ArrayList<ProductoModel> getProductos() {
        return productos;
    }
}

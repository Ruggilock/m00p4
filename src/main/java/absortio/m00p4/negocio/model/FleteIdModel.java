package absortio.m00p4.negocio.model;

import java.io.Serializable;

public class FleteIdModel implements Serializable{


    private Integer idcapacidad;
    private  Integer iddistrito;

    public Integer getIdcapacidad() {
        return idcapacidad;
    }

    public void setIdcapacidad(Integer idcapacidad) {
        this.idcapacidad = idcapacidad;
    }

    public Integer getIddistrito() {
        return iddistrito;
    }

    public void setIddistrito(Integer iddistrito) {
        this.iddistrito = iddistrito;
    }
}

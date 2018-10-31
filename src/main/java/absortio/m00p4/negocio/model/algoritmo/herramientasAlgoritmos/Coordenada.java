package absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos;

public class Coordenada {
    private Integer x;
    private Integer y;
    private Integer z;

    public Coordenada(Integer px, Integer py, Integer pz) {
        x = px;
        y = py;
        z = pz;
    }

    public void setX(Integer px) {
        x = px;
    }

    public void setY(Integer py) {
        y = py;
    }

    public void setZ(Integer pz) {
        z = pz;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    public Integer manhattan(Coordenada coord) {
        return Math.abs(coord.x - x) + Math.abs(coord.y - y) + Math.abs(coord.z - z);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Coordenada)) return false;
        Coordenada coord = (Coordenada) other;
        return (x == coord.x && y == coord.y && z == coord.z);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + x.hashCode();
        result = 31 * result + y;
        result = 31 * result + z.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(" + x.toString() + "; " + y.toString() + "; " + z.toString() + ")";
    }

    public String toString2d() {
        return "(" + x.toString() + "; " + y.toString() + ")";
    }
}

package utn.trabajo_practico.clases;

import utn.trabajo_practico.anotaciones.Columna;
import utn.trabajo_practico.anotaciones.Id;
import utn.trabajo_practico.anotaciones.Tabla;

import java.util.Objects;

@Tabla(nombre = "Direcciones")
public class Domicilio
{
    @Id
    @Columna(nombre = "d_id")
    private String id;
    @Columna(nombre = "d_calle")
    private String calle;
    @Columna(nombre = "d_altura")
    private Integer altura;
    @Columna(nombre = "d_codigoPostal")
    private Integer codigoPostal;
    @Columna(nombre = "d_localidad")
    private String localidad;

    public Domicilio()
    {

    }

    public Domicilio(String id, String calle, Integer altura, Integer codigoPostal, String localidad)
    {
        this.id = id;
        this.calle = calle;
        this.altura = altura;
        this.codigoPostal = codigoPostal;
        this.localidad = localidad;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCalle()
    {
        return calle;
    }

    public void setCalle(String calle)
    {
        this.calle = calle;
    }

    public Integer getAltura()
    {
        return altura;
    }

    public void setAltura(Integer altura)
    {
        this.altura = altura;
    }

    public Integer getCodigoPostal()
    {
        return codigoPostal;
    }

    public void setCodigoPostal(Integer codigoPostal)
    {
        this.codigoPostal = codigoPostal;
    }

    public String getLocalidad()
    {
        return localidad;
    }

    public void setLocalidad(String localidad)
    {
        this.localidad = localidad;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("\'");
        sb.append(id).append('\'').append(",");
        sb.append('\'').append(calle).append('\'').append(",");
        sb.append('\'').append(altura).append('\'').append(",");
        sb.append('\'').append(codigoPostal).append('\'').append(",");
        sb.append('\'').append(localidad).append('\'');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Domicilio)) return false;
        Domicilio domicilio = (Domicilio) o;
        return Objects.equals(getId(), domicilio.getId()) && Objects.equals(getCalle(), domicilio.getCalle()) && Objects.equals(getAltura(), domicilio.getAltura()) && Objects.equals(getCodigoPostal(), domicilio.getCodigoPostal()) && Objects.equals(getLocalidad(), domicilio.getLocalidad());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId(), getCalle(), getAltura(), getCodigoPostal(), getLocalidad());
    }
}

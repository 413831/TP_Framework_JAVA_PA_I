package utn.trabajo_practico.clases;

import utn.trabajo_practico.anotaciones.Columna;
import utn.trabajo_practico.anotaciones.Id;
import utn.trabajo_practico.anotaciones.Tabla;

import java.math.BigInteger;
import java.util.Objects;

@Tabla(nombre = "Autos")
public class Auto
{
    @Id
    @Columna(nombre = "a_id")
    private BigInteger id;
    @Columna(nombre = "a_marca")
    private String marca;
    @Columna(nombre = "a_modelo")
    private String modelo;
    @Columna(nombre = "a_precio")
    private Integer precio;

    public Auto()
    {

    }

    public Auto(String marca, String modelo, Integer precio)
    {
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
    }

    public Auto(BigInteger id, String marca, String modelo, Integer precio)
    {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
    }

    public BigInteger getId()
    {
        return id;
    }

    public void setId(BigInteger id)
    {
        this.id = id;
    }

    public String getMarca()
    {
        return marca;
    }

    public void setMarca(String marca)
    {
        this.marca = marca;
    }

    public String getModelo()
    {
        return modelo;
    }

    public void setModelo(String modelo)
    {
        this.modelo = modelo;
    }

    public Integer getPrecio()
    {
        return precio;
    }

    public void setPrecio(Integer precio)
    {
        this.precio = precio;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Auto{");
        sb.append("id=").append(id);
        sb.append(", marca='").append(marca).append('\'');
        sb.append(", modelo='").append(modelo).append('\'');
        sb.append(", precio=").append(precio);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Auto)) return false;
        Auto auto = (Auto) o;
        return Objects.equals(getId(), auto.getId()) && Objects.equals(getMarca(), auto.getMarca()) && Objects.equals(getModelo(), auto.getModelo()) && Objects.equals(getPrecio(), auto.getPrecio());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId(), getMarca(), getModelo(), getPrecio());
    }
}

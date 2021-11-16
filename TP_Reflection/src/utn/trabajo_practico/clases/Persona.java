package utn.trabajo_practico.clases;

import utn.trabajo_practico.anotaciones.Columna;
import utn.trabajo_practico.anotaciones.Compuesto;
import utn.trabajo_practico.anotaciones.Id;
import utn.trabajo_practico.anotaciones.Tabla;

import java.util.Objects;

@Tabla(nombre = "Personas")
public class Persona
{
    @Columna(nombre = "p_nombre")
    private String nombre;
    @Columna(nombre = "p_apellido")
    private String apellido;
    @Id
    @Columna(nombre = "p_dni")
    private Integer dni;
    @Compuesto
    @Columna(nombre = "p_domicilio")
    private Domicilio domicilio;
    @Columna(nombre = "p_telefono")
    private Integer telefono;
    @Columna(nombre = "p_email")
    private String email;

    public Persona()
    {}

    public Persona(String nombre, String apellido, Integer dni, Domicilio domicilio, Integer telefono, String email)
    {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.domicilio = domicilio;
        this.telefono = telefono;
        this.email = email;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getApellido()
    {
        return apellido;
    }

    public void setApellido(String apellido)
    {
        this.apellido = apellido;
    }

    public Integer getDni()
    {
        return dni;
    }

    public void setDni(Integer dni)
    {
        this.dni = dni;
    }

    public Domicilio getDomicilio()
    {
        return domicilio;
    }

    public void setDomicilio(Domicilio domicilio)
    {
        this.domicilio = domicilio;
    }

    public Integer getTelefono()
    {
        return telefono;
    }

    public void setTelefono(Integer telefono)
    {
        this.telefono = telefono;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Persona{");
        sb.append("nombre='").append(nombre).append('\'');
        sb.append(", apellido='").append(apellido).append('\'');
        sb.append(", dni=").append(dni);
        sb.append(", domicilio=").append(domicilio);
        sb.append(", telefono=").append(telefono);
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Persona)) return false;
        Persona persona = (Persona) o;
        return Objects.equals(getNombre(), persona.getNombre()) && Objects.equals(getApellido(), persona.getApellido()) && Objects.equals(getDni(), persona.getDni()) && Objects.equals(getDomicilio(), persona.getDomicilio()) && Objects.equals(getTelefono(), persona.getTelefono()) && Objects.equals(getEmail(), persona.getEmail());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getNombre(), getApellido(), getDni(), getDomicilio(), getTelefono(), getEmail());
    }
}

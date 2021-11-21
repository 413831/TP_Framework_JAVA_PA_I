package utn.trabajo_practico.test;

import org.junit.Assert;
import org.junit.Test;
import utn.trabajo_practico.clases.Auto;
import utn.trabajo_practico.clases.Domicilio;
import utn.trabajo_practico.clases.Persona;
import utn.trabajo_practico.servicios.Consultas;

import java.math.BigInteger;
import java.util.ArrayList;


/**
 * @author Alejandro Planes
 * @version 1.0
 */
public class ServiciosTest
{
    /**
     * Test de nuevo registro en base de datos
     */
    @Test
    public void guardarObjetoOK()
    {
        Auto auto = new Auto("Ford", "Focus", 123400);
        try
        {
            Consultas.guardar(auto);
            Assert.assertTrue(true);
        }
        catch (Exception ex)
        {
            Assert.fail();
        }
    }

    /**
     * Test de modificación de registro de base de datos
     */
    @Test
    public void modificarObjetoOK()
    {
        Auto auto = new Auto(new BigInteger(String.valueOf(34)), "Ford", "Focus", 123400);
        try
        {
            Object object = Consultas.modificar(auto);
            Assert.assertEquals(auto,object);
        }
        catch (Exception ex)
        {
            Assert.fail();
        }
    }

    /**
     * Test de obtención por ID de registro de base de datos
     */
    @Test
    public void obtenerObjetoPorIdOK()
    {
        Auto auto = new Auto(new BigInteger(String.valueOf(38)), "Ford", "Focus", 123400);
        try
        {
            Object object = Consultas.obtenerPorId(auto.getClass(), 38);
            Assert.assertEquals(auto,object);
        }
        catch (Exception ex)
        {
            Assert.fail();
        }
    }

    /**
     * Test de eliminación de registro de base de datos
     */
    @Test
    public void eliminarObjetoOK()
    {
        Auto auto = new Auto(new BigInteger(String.valueOf(41)), "Ford", "Focus", 123400);
        try
        {
            boolean response = Consultas.eliminar(auto);
            if(response) Assert.assertTrue(true);
        }
        catch (Exception ex)
        {
            Assert.fail();
        }
    }

    /**
     * Test de obtencion de todos los registros de una tabla de la base de datos
     */
    @Test
    public void obtenerTodosOK()
    {
        try
        {
            ArrayList lista = Consultas.obtenerTodos(Auto.class);
            Assert.assertTrue(lista.size() > 0);
        }
        catch (Exception ex)
        {
            Assert.fail();
        }
    }

    /**
     * Test de nuevo registro en base de datos con una entidad como columna
     */
    @Test
    public void guardarEnCascadaOK()
    {
        try
        {
            Domicilio domicilio = new Domicilio("Calle", 1234, 222, "CABA");
            Persona persona = new Persona("33222111", "Pepito", "Gomez", domicilio, 113232322, "mail@mail.com");
            Consultas.guardar(persona);
            Assert.assertTrue(true);
        }
        catch (Exception ex)
        {
            Assert.fail();
        }
    }
}

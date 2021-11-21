package utn.trabajo_practico.test;

import org.junit.Assert;
import org.junit.Test;
import utn.trabajo_practico.anotaciones.Tabla;
import utn.trabajo_practico.clases.Auto;
import utn.trabajo_practico.utilidades.UBean;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * @author Alejandro Planes
 * @version 1.0
 */
public class UtilidadesTest
{
    /**
     * Test de obtención de atributos de un objeto
     */
    @Test
    public void obtenerAtributosOK()
    {
        try
        {
            UBean ubean = new UBean();
            Auto auto = new Auto(new BigInteger(String.valueOf(35)), "Ford", "Focus", 123400);
            ArrayList<Field> atributos = ubean.obtenerAtributos(auto);

            Assert.assertNotNull(atributos);
        }
        catch (Exception ex)
        {
            Assert.fail();
        }
    }

    /**
     * Test de ejecución de Getter de un objeto
     */
    @Test
    public void ejecutarGetOK()
    {
        try
        {
            UBean ubean = new UBean();
            Auto auto = new Auto(new BigInteger(String.valueOf(35)), "Ford", "Focus", 123400);
            Object valor = ubean.ejecutarGet(auto, "Id");

            Assert.assertEquals(auto.getId(),valor);
        }
        catch (Exception ex)
        {
            Assert.fail();
        }
    }

    /**
     * Test de ejecución de un Setter de un objeto
     */
    @Test
    public void ejecutarSetOK()
    {
        try
        {
            UBean ubean = new UBean();
            Auto auto = new Auto(new BigInteger(String.valueOf(35)), "Ford", "Focus", 123400);
            String nuevoValor = "Fiesta";
            Auto objetoNuevoValor = (Auto) ubean.ejecutarSet(auto, "Modelo", nuevoValor);

            Assert.assertEquals(objetoNuevoValor.getModelo(), nuevoValor);
        }
        catch (Exception ex)
        {
            Assert.fail();
        }
    }

    /**
     * Test de obtención de anotación de clase
     */
    @Test
    public void obtenerAnotacionOK()
    {
        try
        {
            UBean ubean = new UBean();
            Auto auto = new Auto(new BigInteger(String.valueOf(35)), "Ford", "Focus", 123400);
            Object anotacion = ubean.obtenerAnotaciones(auto, Tabla.class);

            Assert.assertNotNull(anotacion);
        }
        catch (Exception ex)
        {
            Assert.fail();
        }
    }


}

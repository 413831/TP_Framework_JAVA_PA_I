package utn.trabajo_practico.utilidades;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author Alejandro Planes
 * @version 1.0
 */
public class UBean
{
    /**
     * Método para obtener todos los atributos declarados de un Objeto
     * @param objeto Objecto para obtener atributos
     * @return Retorna un ArrayList de tipo Field con los atributos declarados o inicializado sin elementos
     */
    public ArrayList<Field> obtenerAtributos(@NotNull Object objeto)
    {
        ArrayList<Field> listaAtributos = new ArrayList<Field>();

        Field[] atributos = objeto.getClass().getDeclaredFields();

        for (Field atributo : atributos)
        {
            listaAtributos.add(atributo);
        }
        return listaAtributos;
    }

    /**
     * Método para ejecutar un Setter de un objeto
     * @param object Objeto del cual se desea ejecutar el Setter
     * @param att Nombre del atributo para setear valor
     * @param valor Valor para el atributo a setear
     * @return Retorna el objeto recibido por parámetros
     */
    public Object ejecutarSet(@NotNull Object object, @NotNull String att, Object valor)
    {
        Method[] methods = object.getClass().getDeclaredMethods();
        String attribute = att.substring(0,1).toUpperCase() + att.substring(1);

        try
        {
            for (Method method: methods)
            {
                if(method.getName().startsWith("set" + attribute))
                {
                    method.invoke(object, valor);
                }
            }
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * Método para ejecutar un Getter de un objeto
     * @param object Objeto del cual se desea obtener el valor del atributo
     * @param att Nombre del atributo para obtener valor
     * @return Retorna el valor del atributo recibido por parámetro o un objeto tipo Object
     */
    public Object ejecutarGet(@NotNull Object object, @NotNull String att)
    {
        Object value = new Object();
        Method[] methods = object.getClass().getDeclaredMethods();
        String attribute = att.substring(0,1).toUpperCase() + att.substring(1);

        try
        {
            for (Method method: methods)
            {
                if(method.getName().startsWith("get" + attribute))
                {
                    value = method.invoke(object);
                }
            }
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Método para obtener anotaciones de clase de un objeto
     * @param object Objeto del cual se desea obtener las anotaciones
     * @param anotacion Nombre de la anotacion a obtener
     * @return Retorna un objeto de tipo Object con información de la anotacion buscada o un objeto de tipo Object inicializado
     */
    public Object obtenerAnotaciones(@NotNull Object object, Class anotacion)
    {
        Object objetoAnotacion = new Object();

        objetoAnotacion = object.getClass().getAnnotation(anotacion);

        return objetoAnotacion;
    }

}

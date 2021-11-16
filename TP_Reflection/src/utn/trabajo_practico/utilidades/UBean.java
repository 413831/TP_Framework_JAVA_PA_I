package utn.trabajo_practico.utilidades;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class UBean
{
    public ArrayList<Field> obtenerAtributos(Object objeto)
    {
        ArrayList<Field> listaAtributos = new ArrayList<Field>();

        Field[] atributos = objeto.getClass().getDeclaredFields();

        for (Field atributo : atributos)
        {
            listaAtributos.add(atributo);
        }
        return listaAtributos;
    }

    public void ejecutarSet(Object o, String att, Object valor)
    {
        try
        {
            // FIXME Mejorar
            o.getClass().getMethod("set" + att,o.getClass()).invoke(o.getClass(),valor);
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    public Object ejecutarGet(Object o, String att)
    {
        Object value = new Object();
        Method[] methods = o.getClass().getDeclaredMethods();
        String attribute = att.substring(0,1).toUpperCase() + att.substring(1);

        try
        {
            // FIXME Mejorar
            for (Method method: methods)
            {
                if(method.getName().startsWith("get" + attribute))
                {
                    value = method.invoke(o);
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

    public Object obtenerAnotaciones(Object o, Class anotacion)
    {
        return o.getClass().getAnnotation(anotacion);
    }

}

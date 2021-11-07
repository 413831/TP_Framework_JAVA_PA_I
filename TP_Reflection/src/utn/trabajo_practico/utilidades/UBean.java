package utn.trabajo_practico.utilidades;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class UBean
{
    public ArrayList<Field> obtenerAtributos(Object objeto)
    {
        ArrayList<Field> atributos = new ArrayList<Field>();

        for (Field atributo : objeto.getClass().getFields())
        {
            atributos.add(atributo);
        }
        return atributos;
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
        try
        {
            // FIXME Mejorar
            o.getClass().getMethod("get" + att,o.getClass()).invoke(o.getClass());
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
    }

}

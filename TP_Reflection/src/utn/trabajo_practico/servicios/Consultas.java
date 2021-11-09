package utn.trabajo_practico.servicios;

import utn.trabajo_practico.utilidades.UBean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class Consultas
{
    private static String tabla;
    private static Conexion conexion;

    public static void guardar(Object o)
    {
        UBean ubean = new UBean();
        Map<String,String> valoresInsert = new HashMap<String,String>();
        int columna = 0;

        try
        {
            // Se obtienen nombre de atributos
            ArrayList<Field> atributos = ubean.obtenerAtributos(o);
            // Se enlazan valores de los atributos del objeto recibido
            for (Field atributo: atributos)
            {
                String valor = String.valueOf(ubean.ejecutarGet(o, atributo.getName()));
                valoresInsert.put(atributo.getName(),valor);
            }
            // Construccion de consulta
            String query = "insert into " + Consultas.tabla + " (" +valoresInsert.keySet().toString() + ") " + "values (" + valoresInsert.values().toString() + ")";
            PreparedStatement insert = Consultas.conexion.getConnection().prepareStatement(query);
            insert.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void modificar(Object o)
    {

    }

    public static void eliminar(Object o)
    {
        UBean ubean = new UBean();
        String query = "DELETE FROM " + Consultas.tabla + " WHERE id = " + ubean.ejecutarGet(o,"Id");
        try
        {
            PreparedStatement delete = Consultas.conexion.getConnection().prepareStatement(query);
            delete.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static Class obtenerPorId(Class c, Object id)
    {
        UBean ubean = new UBean();
        try
        {
            String query = "SELECT " + c.getMethod("get" + id.toString(), c).toString() + " FROM " +  Consultas.tabla ;
            PreparedStatement select = Consultas.conexion.getConnection().prepareStatement(query);
            ResultSet result = select.executeQuery();
            Constructor constructor = c.getConstructor();
            Object objeto = constructor.newInstance();

            while(result.next())
            {

            }

        }
        catch (NoSuchMethodException | SQLException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

}

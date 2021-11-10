package utn.trabajo_practico.servicios;

import utn.trabajo_practico.anotaciones.Columna;
import utn.trabajo_practico.anotaciones.Id;
import utn.trabajo_practico.anotaciones.Tabla;
import utn.trabajo_practico.utilidades.UBean;
import utn.trabajo_practico.utilidades.UConexion;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class Consultas
{
    private static UConexion conexion;

    public static void guardar(Object o)
    {
        try
        {
            UBean ubean = new UBean();
            // Obtengo la tabla asociada a la clase del objeto
            Tabla tabla = (Tabla) ubean.obtenerAnotaciones(o, Tabla.class);
            // Se obtienen nombre de atributos
            ArrayList<Field> atributos = ubean.obtenerAtributos(o);
            // Construccion de consulta
            String query = "insert into " + tabla.nombre();
            String columnas = " (";
            String valores = " (";
            // Se enlazan valores de los atributos del objeto recibido
            for (Field atributo: atributos)
            {
                Columna columna = (Columna) atributo.getAnnotation(Columna.class);
                columnas += columna.nombre() + ",";
                valores += ubean.ejecutarGet(o,atributo.getName()) + ",";
            }
            columnas += ") ";
            valores += ")";
            query += columnas + "values" + valores + ";";

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
        try
        {
            UBean ubean = new UBean();
            Tabla tabla = (Tabla) ubean.obtenerAnotaciones(o, Tabla.class);
            ArrayList<Field> atributos = ubean.obtenerAtributos(o);

            String query = "UPDATE " + tabla.nombre() + "SET ";

            for (Field atributo: atributos)
            {
                Columna columna = (Columna) ubean.obtenerAnotaciones(o,Columna.class);
                query += columna.nombre() + " = " + ubean.ejecutarGet(o,atributo.getName()) + ",";
            }

            PreparedStatement update = Consultas.conexion.getConnection().prepareStatement(query);
            update.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void eliminar(Object o)
    {
        try
        {
            UBean ubean = new UBean();
            ArrayList<Field> atributos = ubean.obtenerAtributos(o);
            Tabla tabla = (Tabla) ubean.obtenerAnotaciones(o,Tabla.class);

            String query = "DELETE FROM " + tabla.nombre() + " WHERE ";

            for (Field atributo: atributos)
            {
                if(atributo.getAnnotation(Id.class) != null)
                {
                    query += atributo.getName() + " = " + ubean.ejecutarGet(o,atributo.getName());
                }
            }
            PreparedStatement delete = Consultas.conexion.getConnection().prepareStatement(query);
            delete.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static Object obtenerPorId(Class c, Object id)
    {
        try
        {
            UBean ubean = new UBean();
            Tabla tabla = (Tabla) c.getAnnotation(Tabla.class);
            Field[] atributos = c.getDeclaredFields();
            Constructor[] constructores = c.getConstructors();
            // Busco el constructor con todos los argumentos
            Optional<Constructor> constructor = Arrays.stream(constructores)
                                                      .filter(con -> con.getParameterCount() == atributos.length)
                                                      .findFirst();
            Object[] parametros = constructor.get().getParameterTypes();
            String query = "SELECT ";
            String atributo_id = "";
            for (Field atributo: atributos)
            {
                String nombreAtributo = atributo.getAnnotation(Columna.class).nombre();
                query += nombreAtributo + ",";
                if(atributo.getAnnotation(Id.class) != null)
                {
                    atributo_id = nombreAtributo;
                }
            }
            query += "FROM " + tabla.nombre() + " WHERE " + atributo_id + " = " + id + ";";

            // Ejecuto el SELECT
            PreparedStatement select = Consultas.conexion.getConnection().prepareStatement(query);
            ResultSet result = select.executeQuery();
            Object[] objeto = new Object[parametros.length];

            while(result.next())
            {
                for (int i = 0; i < atributos.length; i++)
                {
                    // Se recupera valor del result con nombre de columna en orden de atributos de clase
                    objeto[i] = result.getObject(atributos[i].getAnnotation(Columna.class).nombre());
                }
            }
            return constructor.get().newInstance(objeto);
        }
        catch (SQLException e)
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
        return new Object();
    }

}

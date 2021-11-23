package utn.trabajo_practico.servicios;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utn.trabajo_practico.anotaciones.*;
import utn.trabajo_practico.utilidades.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Alejandro Planes
 * @version 1.0
 */
public class Consultas
{
    /**
     * Método para guardar un objeto en la base de datos
     * @param object Objeto que se desea guardar en la base de datos
     * @return Retorna el objeto guardado con el Id generado por la base de datos
     */
    public static Object guardar(Object object)
    {
        try
        {
            UBean ubean = new UBean();
            // DB Table for object Class
            Tabla table = (Tabla) ubean.obtenerAnotaciones(object, Tabla.class);
            // Class declared attributes
            ArrayList<Field> atributos = ubean.obtenerAtributos(object);
            // Query initialization
            String query = "INSERT INTO " + table.nombre();
            String columns = " (";
            String values = " (";
            // Gathering columns and values for the query
            for (Field atributo: atributos)
            {
                if(atributo.getAnnotation(Id.class) != null)
                {
                    continue;
                }
                System.out.println(atributo.getName());
                Columna columna = atributo.getAnnotation(Columna.class);
                columns += columna.nombre() + ",";

                if(atributo.getAnnotation(Compuesto.class) != null)
                {
                    Object idAttribute = Consultas.guardarCompuesto(object,atributo);
                    values += idAttribute + ",";
                }
                else if(atributo.getType().equals(Integer.class))
                {
                    values += ubean.ejecutarGet(object,atributo.getName()) + ",";
                }
                else
                {
                    values += "'" + ubean.ejecutarGet(object,atributo.getName()) + "',";
                }
            }
            columns = columns.substring(0,columns.length()-1);
            columns += ") ";
            values = values.substring(0,values.length()-1);
            values += ")";
            // Query creation
            query += columns + "VALUES" + values + ";";
            System.out.println(query);

            PreparedStatement insert = UConexion.getInstance().getConnection()
                                                         .prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            // Query execution
            insert.execute();
            // Query generated keys to set object ID
            ResultSet generatedKeys = insert.getGeneratedKeys();
            if(generatedKeys.first())
            {
                for (Field attribute: atributos)
                {
                    if(attribute.getAnnotation(Id.class) != null)
                    {
                        object = ubean.ejecutarSet(object,attribute.getName(),generatedKeys.getObject(1));
                    }
                }
            }
            insert.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (ClassCastException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
        }
        return object;
    }

    /**
     * Método para guardar o actualizar un objeto en la base de datos
     * @param object Objeto que se desea guardar en la base de datis
     * @return Retorna el objeto guardado en la base de datos o el objeto actualizado en la misma
     */
    public static Object guardarModificar(Object object)
    {
        UBean ubean = new UBean();
        ArrayList<Field> atributos = ubean.obtenerAtributos(object);
        Object id = new Object();

        for (Field atributo: atributos)
        {
            if(atributo.getAnnotation(Id.class) != null)
            {
                id = ubean.ejecutarGet(object,atributo.getName());
            }
        }
        if(Consultas.obtenerPorId(object.getClass(),id) != null)
        {
            // Query method execution
            object = Consultas.modificar(object);
        }
        else
        {
            // Query method execution
            object = Consultas.guardar(object);
        }
        return object;
    }

    /**
     * Método para modificar un objeto en la base de datos
     * @param object Objeto a modificar
     * @return Retorna el objeto modificado
     */
    public static Object modificar(Object object)
    {
        try
        {
            UBean ubean = new UBean();
            Tabla tabla = (Tabla) ubean.obtenerAnotaciones(object, Tabla.class);
            ArrayList<Field> atributos = ubean.obtenerAtributos(object);
            // Query initialization
            String query = "UPDATE " + tabla.nombre() + " SET ";
            // Query filter initialization
            String filter = " WHERE ";

            for (Field atributo: atributos)
            {
                Columna columna = atributo.getAnnotation(Columna.class);
                if(atributo.getAnnotation(Id.class) != null)
                {
                    filter += columna.nombre() +  " = '" + ubean.ejecutarGet(object,atributo.getName()) + "';";
                }
                else if(atributo.getClass().equals(Integer.class))
                {
                    query += columna.nombre() + " = " + ubean.ejecutarGet(object,atributo.getName()) + ",";
                }
                else
                {
                    query += columna.nombre() + " = '" + ubean.ejecutarGet(object,atributo.getName()) + "',";
                }
            }
            query = query.substring(0,query.length()-1);
            PreparedStatement update = UConexion.getInstance().getConnection().prepareStatement(query + filter);
            // Query execution
            update.execute();
            update.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * Método para eliminar un objeto de la base de datos
     * @param object Objeto a eliminar
     * @return Retorna true si se logró eliminar de lo contrario false
     */
    public static boolean eliminar(Object object)
    {
        try
        {
            UBean ubean = new UBean();
            ArrayList<Field> atributos = ubean.obtenerAtributos(object);
            Tabla tabla = (Tabla) ubean.obtenerAnotaciones(object,Tabla.class);
            // Query initialization
            String query = "DELETE FROM " + tabla.nombre() + " WHERE ";

            for (Field atributo: atributos)
            {
                if(atributo.getAnnotation(Id.class) != null)
                {
                    query += atributo.getAnnotation(Columna.class).nombre() + " = " + ubean.ejecutarGet(object,atributo.getName());
                }
            }
            System.out.println("DELETE");
            PreparedStatement delete = UConexion.getInstance().getConnection().prepareStatement(query);
            // Query execution
            boolean response = delete.execute();
            delete.close();
            return response;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Método para obtener un objeto por su ID
     * @param clazz Clase del objeto que se desea recuperar
     * @param id Id del objeto a recuperar
     * @return Retorna una instancia de la Clase recibida por parámetro
     */
    public static @Nullable Object obtenerPorId(@NotNull Class clazz, Object id)
    {
        try
        {
            Tabla table = (Tabla) clazz.getAnnotation(Tabla.class);
            Field[] atributos = clazz.getDeclaredFields();
            Constructor[] constructores = clazz.getConstructors();
            // Get the constructor with all attributes
            Optional<Constructor> constructor = Arrays.stream(constructores)
                                                      .filter(con -> con.getParameterCount() == atributos.length)
                                                      .findFirst();
            Object[] parametros = constructor.get().getParameterTypes();
            // Query initialization
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
            query = query.substring(0,query.length()-1);
            // Query construction
            query += " FROM " + table.nombre() + " WHERE " + atributo_id + " = " + id + ";";

            System.out.println(query);
            // Query execution
            System.out.println("SELECT");
            PreparedStatement select = UConexion.getInstance().getConnection().prepareStatement(query);
            ResultSet result = select.executeQuery();
            Object[] arguments = new Object[parametros.length];
            // Iteration over query ResultSet
            while(result.next())
            {
                for (int i = 0; i < atributos.length; i++)
                {
                    // Retrieving ResultSet values with columns name in order
                    arguments[i] = result.getObject(atributos[i].getAnnotation(Columna.class).nombre());
                    // Autogenerated ID casting
                    if(i == 0)
                    {
                        arguments[i] = new BigInteger(String.valueOf(arguments[i]));
                    }
                }
            }
            select.close();
            return constructor.get().newInstance(arguments);
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
        return null;
    }

    /**
     * Método para obtener todos los objetos de una tabla
     * @param clazz Clase de la cual se desea obtener objetos
     * @return Retorna un ArrayList con todos los objetos encontrados o un ArrayList vacío
     */
    public static @Nullable ArrayList obtenerTodos(@NotNull Class clazz)
    {
        try
        {
            ArrayList<Object> list = new ArrayList<Object>();
            Tabla table = (Tabla) clazz.getAnnotation(Tabla.class);
            Field[] atributos = clazz.getDeclaredFields();
            Constructor[] constructores = clazz.getConstructors();
            // Get the constructor with all attributes
            Optional<Constructor> constructor = Arrays.stream(constructores)
                                                      .filter(con -> con.getParameterCount() == atributos.length)
                                                      .findFirst();
            Object[] parametros = constructor.get().getParameterTypes();
            String query = "SELECT * FROM " + table.nombre() + ";";
            System.out.println(query);
            // Query execution
            PreparedStatement select = UConexion.getInstance().getConnection().prepareStatement(query);
            ResultSet result = select.executeQuery();

            while(result.next())
            {
                Object[] arguments = new Object[parametros.length];

                for (int i = 0; i < atributos.length; i++)
                {
                    // Retrieving ResultSet values with columns name in order
                    arguments[i] = result.getObject(atributos[i].getAnnotation(Columna.class).nombre());
                    // Autogenerated ID casting
                    if(i == 0)
                    {
                        arguments[i] = new BigInteger(String.valueOf(arguments[i]));
                    }
                }
                list.add(constructor.get().newInstance(arguments));
            }
            select.close();
            return list;
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
        return null;
    }

    /**
     * Método para guardar un atributo tipo Object en la base de datos
     * @param object Objeto del cual se obtiene el atributo tipo Object
     * @param attribute Atributo para guardar
     * @return Retorna el ID generado por la base de datos del atributo guardado
     */
    public static Object guardarCompuesto(Object object, @NotNull Field attribute)
    {
        try
        {
            UBean ubean = new UBean();
            attribute.trySetAccessible();
            // Cast Field to Object type
            Object attributeObject = attribute.get(object);
            // Query method execution
            attributeObject = Consultas.guardar(attributeObject);
            // Getting the object ID value with its getters using ubean helper class
            Field nombreIdCompuesto = Arrays.stream(attributeObject.getClass().getDeclaredFields()).filter(f -> f.getAnnotation(Id.class) != null).findFirst().get();
            Object idAttributeObject = ubean.ejecutarGet(attributeObject, nombreIdCompuesto.getName());
            return idAttributeObject;
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return new Object();
    }
}

package utn.trabajo_practico.servicios;

import utn.trabajo_practico.anotaciones.Columna;
import utn.trabajo_practico.anotaciones.Compuesto;
import utn.trabajo_practico.anotaciones.Id;
import utn.trabajo_practico.anotaciones.Tabla;
import utn.trabajo_practico.utilidades.UBean;
import utn.trabajo_practico.utilidades.UConexion;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class Consultas
{
    static
    {
        System.out.println("CONEXION");
    }

    public static Object guardar(Object o)
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
                System.out.println(atributo.getName());
                Columna columna = atributo.getAnnotation(Columna.class);
                columnas += columna.nombre() + ",";

                System.out.println(columnas);

                if(atributo.getAnnotation(Compuesto.class) != null)
                {
                    System.out.println("Atributo compuesto");
                    Constructor[] constructores = atributo.getType().getConstructors();
                    Field[] auxiliarAtributos = atributo.getType().getDeclaredFields();
                    // Busco el constructor con todos los argumentos
                    Constructor constructor = Arrays.stream(constructores)
                                                              .filter(con -> con.getParameterCount() == auxiliarAtributos.length).findAny().orElse(null);
                    Object[] arguments = new Object[constructor.getParameterTypes().length];

                    for (int i = 0; i < auxiliarAtributos.length; i++)
                    {
                        System.out.println(auxiliarAtributos[i].getName());
                        String nameAttribute = auxiliarAtributos[i].getName().substring(0,1).toUpperCase() + auxiliarAtributos[i].getName().substring(1);
                        Method[] methods = atributo.getType().getDeclaredMethods();

                        for (Method method: methods)
                        {
                            if(method.getName().startsWith("get" + nameAttribute))
                            {
                                //FIXME Pasar valores de los atributos del objeto
                                arguments[i] = method.invoke(o);
                            }
                        }
                    }
                    Object claseAtributo = constructor.newInstance(arguments);

                    for (Field atributoCompuesto: auxiliarAtributos)
                    {
                        System.out.println(atributoCompuesto.getName());
                        if(atributoCompuesto.getAnnotation(Id.class) != null)
                        {
                            valores += "'" + ubean.ejecutarGet(claseAtributo,atributoCompuesto.getName()) + "',";
                        }
                    }
                    //Consultas.guardar(claseAtributo);
                }
                else
                {
                    valores += "'" + ubean.ejecutarGet(o,atributo.getName()) + "',";
                }
            }
            columnas += ") ";
            valores += ")";
            query += columnas + "values" + valores + ";";
            System.out.println(query);

            PreparedStatement insert = UConexion.getInstance().getConnection()
                                                         .prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            System.out.println("INSERT");
            insert.execute();
            ResultSet generatedKeys = insert.getGeneratedKeys();
            if(generatedKeys.next())
            {
                for (Field atributo: atributos)
                {

                    if(atributo.getAnnotation(Id.class) != null)
                    {
                        Columna columna = atributo.getAnnotation(Columna.class);

                        ubean.ejecutarSet(o,atributo.getName(),generatedKeys.getObject(columna.nombre()));
                    }
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            return o;
        }
    }

    public Object guardarModificar(Object o)
    {
        UBean ubean = new UBean();
        ArrayList<Field> atributos = ubean.obtenerAtributos(o);
        Object id = "";

        for (Field atributo: atributos)
        {
            if(atributo.getAnnotation(Id.class) != null)
            {
                Columna columna = atributo.getAnnotation(Columna.class);
                id = atributo.toString();
            }
        }
        if(Consultas.obtenerPorId(o.getClass(),id) != null)
        {
            Consultas.modificar(o);
        }
        else
        {
            o = Consultas.guardar(o);
        }
        return o;
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

            PreparedStatement update = UConexion.getInstance().getConnection().prepareStatement(query);
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
            PreparedStatement delete = UConexion.getInstance().getConnection().prepareStatement(query);
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
            PreparedStatement select = UConexion.getInstance().getConnection().prepareStatement(query);
            ResultSet result = select.executeQuery();
            Object[] arguments = new Object[parametros.length];

            while(result.next())
            {
                for (int i = 0; i < atributos.length; i++)
                {
                    // Se recupera valor del result con nombre de columna en orden de atributos de clase
                    arguments[i] = result.getObject(atributos[i].getAnnotation(Columna.class).nombre());
                }
            }
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

    public ArrayList obtenerTodos(Class c)
    {
        try
        {
            ArrayList<Object> list = new ArrayList<Object>();
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
            query += "FROM " + tabla.nombre() + ";";

            // Ejecuto el SELECT
            PreparedStatement select = UConexion.getInstance().getConnection().prepareStatement(query);
            ResultSet result = select.executeQuery();

            while(result.next())
            {
                Object[] arguments = new Object[parametros.length];

                for (int i = 0; i < atributos.length; i++)
                {
                    // Se recupera valor del result con nombre de columna en orden de atributos de clase
                    arguments[i] = result.getObject(atributos[i].getAnnotation(Columna.class).nombre());
                    list.add(constructor.get().newInstance(arguments));
                }
            }
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

}

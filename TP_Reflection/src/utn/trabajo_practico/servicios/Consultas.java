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
import java.math.BigInteger;
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
            String query = "INSERT INTO " + tabla.nombre();
            String columnas = " (";
            String valores = " (";
            // Se enlazan valores de los atributos del objeto recibido
            for (Field atributo: atributos)
            {
                if(atributo.getAnnotation(Id.class) != null)
                {
                    continue;
                }
                System.out.println(atributo.getName());
                Columna columna = atributo.getAnnotation(Columna.class);
                columnas += columna.nombre() + ",";

                System.out.println(valores);
/*
                if(atributo.getAnnotation(Compuesto.class) != null)
                {

                }
                else
                {
                    valores += "'" + ubean.ejecutarGet(o,atributo.getName()) + "',";
                }

 */
                if(atributo.getAnnotation(Compuesto.class) != null)
                {
                    Object valorCompuesto = ubean.ejecutarGet(o, atributo.getName());
                    String[] attAux = String.valueOf(valorCompuesto).split("\'");
                    String idCompuesto = attAux[1];
                    System.out.println(idCompuesto);
                    valores += "'" + idCompuesto + "',";
                }
                else if(atributo.getType().equals(Integer.class))
                {
                    valores += ubean.ejecutarGet(o,atributo.getName()) + ",";
                }
                else
                {
                    valores += "'" + ubean.ejecutarGet(o,atributo.getName()) + "',";
                }
            }
            columnas = columnas.substring(0,columnas.length()-1);
            columnas += ") ";
            valores = valores.substring(0,valores.length()-1);
            valores += ")";
            query += columnas + "VALUES" + valores + ";";
            System.out.println(query);

            PreparedStatement insert = UConexion.getInstance().getConnection()
                                                         .prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            System.out.println("INSERT");
            insert.execute();
            ResultSet generatedKeys = insert.getGeneratedKeys();
            if(generatedKeys.first())
            {
                for (Field atributo: atributos)
                {
                    if(atributo.getAnnotation(Id.class) != null)
                    {
                        o = ubean.ejecutarSet(o,atributo.getName(),generatedKeys.getObject(1));
                        System.out.println("GUARDAR -> " + o);
                    }
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (ClassCastException ex)
        {
            ex.printStackTrace();
        }
        return o;
    }

    public static Object guardarModificar(Object o)
    {
        UBean ubean = new UBean();
        ArrayList<Field> atributos = ubean.obtenerAtributos(o);
        Object id = new Object();

        for (Field atributo: atributos)
        {
            if(atributo.getAnnotation(Id.class) != null)
            {
                Columna columna = atributo.getAnnotation(Columna.class);
                id = ubean.ejecutarGet(o,atributo.getName());
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

            String query = "UPDATE " + tabla.nombre() + " SET ";
            String filter = " WHERE ";

            for (Field atributo: atributos)
            {
                Columna columna = atributo.getAnnotation(Columna.class);
                if(atributo.getAnnotation(Id.class) != null)
                {
                    filter += columna.nombre() +  " = '" + ubean.ejecutarGet(o,atributo.getName()) + "';";
                }
                else if(atributo.getClass().equals(Integer.class))
                {
                    query += columna.nombre() + " = " + ubean.ejecutarGet(o,atributo.getName()) + ",";
                }
                else
                {
                    query += columna.nombre() + " = '" + ubean.ejecutarGet(o,atributo.getName()) + "',";
                }
            }
            query = query.substring(0,query.length()-1);
            System.out.println(query + filter);
            PreparedStatement update = UConexion.getInstance().getConnection().prepareStatement(query + filter);
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
                    query += atributo.getAnnotation(Columna.class).nombre() + " = " + ubean.ejecutarGet(o,atributo.getName());
                }
            }
            System.out.println("DELETE");
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
            query = query.substring(0,query.length()-1);
            query += " FROM " + tabla.nombre() + " WHERE " + atributo_id + " = " + id + ";";

            System.out.println(query);
            // Ejecuto el SELECT
            System.out.println("SELECT");
            PreparedStatement select = UConexion.getInstance().getConnection().prepareStatement(query);
            ResultSet result = select.executeQuery();
            Object[] arguments = new Object[parametros.length];

            while(result.next())
            {
                for (int i = 0; i < atributos.length; i++)
                {
                    // Se recupera valor del result con nombre de columna en orden de atributos de clase
                    arguments[i] = result.getObject(atributos[i].getAnnotation(Columna.class).nombre());
                    if(i == 0)
                    {
                        arguments[i] = new BigInteger(String.valueOf(arguments[i]));
                    }
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

    public static ArrayList obtenerTodos(Class c)
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
            String query = "SELECT * FROM " + tabla.nombre() + ";";
            System.out.println(query);
            /*String atributo_id = "";
            for (Field atributo: atributos)
            {
                String nombreAtributo = atributo.getAnnotation(Columna.class).nombre();
                query += nombreAtributo + ",";
                if(atributo.getAnnotation(Id.class) != null)
                {
                    atributo_id = nombreAtributo;
                }
            }
            */

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
                    if(i == 0)
                    {
                        arguments[i] = new BigInteger(String.valueOf(arguments[i]));
                    }
                }
                list.add(constructor.get().newInstance(arguments));
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

    public void guardarCompuesto(Field atributo)
    {
        try
        {
            System.out.println("Atributo compuesto");
            String valores = "";
            UBean ubean = new UBean();
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

                        arguments[i] = method.invoke(atributo);


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
            Consultas.guardar(claseAtributo);
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }


    }

}

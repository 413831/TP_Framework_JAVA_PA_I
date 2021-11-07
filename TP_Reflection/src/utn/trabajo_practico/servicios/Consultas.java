package utn.trabajo_practico.servicios;

import utn.trabajo_practico.utilidades.UBean;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class Consultas
{
    private static String tabla;

    public static void guardar(Object o)
    {
        UBean ubean = new UBean();
        Map<String,String> valoresInsert = new HashMap<String,String>();
        int columna = 0;

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "admin");
            ArrayList<Field> atributos = ubean.obtenerAtributos(o);

            for (Field atributo: atributos)
            {
                String valor = String.valueOf(ubean.ejecutarGet(o, atributo.getName()));
                valoresInsert.put(atributo.getName(),valor);
            }



            PreparedStatement insert = connection.prepareStatement("insert into " + tabla + valoresInsert.keySet().toString() " (nombre) values (?)");
            insert.setString(columna,valor);
            insert.execute();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }




    }

}

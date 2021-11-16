package utn.trabajo_practico.utilidades;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UConexion
{
    private static UConexion miInstancia;
    private Connection connection;

    private UConexion()
    {
        try
        {
            File directory = new File("./");
            System.out.println(directory.getAbsolutePath());
            JSONObject configuration = this.getConfiguration("framework.properties.json");
            System.out.println("Configuracion");
            //Class.forName("com.mysql.cj.jdbc.Driver");
            Class.forName(configuration.get("DRIVER").toString());
            //this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "admin");
            this.connection = DriverManager.getConnection(configuration.get("DB").toString(),
                                                          configuration.get("USER").toString(),
                                                          configuration.get("PASSWORD").toString());
            System.out.println("Conexion");
            System.out.println(this.connection.getMetaData());
            System.out.println(this.connection.getCatalog());
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static UConexion getInstance()
    {
        if(UConexion.miInstancia == null)
        {
            UConexion.miInstancia = new UConexion();
        }
        return UConexion.miInstancia;
    }

    private JSONObject getConfiguration(String file)
    {
        JSONParser jsonParser = new JSONParser();

        try(FileReader reader = new FileReader(file))
        {
            JSONObject object = (JSONObject) jsonParser.parse(reader);
            return object;
        }
        catch (FileNotFoundException exception)
        {
            exception.printStackTrace();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public Connection getConnection()
    {
        return connection;
    }
}

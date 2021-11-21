package utn.trabajo_practico.utilidades;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author Alejandro Planes
 * @version 1.0
 */
public class UConexion
{
    private static UConexion miInstancia;
    private static final String CONFIG_FILE = "framework.properties.json";
    private Connection connection;

    /**
     * Constructor privado para Singleton
     * Se levanta configuración de conexión desde archivo
     */
    private UConexion()
    {
        try
        {
            JSONObject configuration = this.getConfiguration(CONFIG_FILE);
            if(configuration != null)
            {
                Class.forName(configuration.get("DRIVER").toString());
                this.connection = DriverManager.getConnection(configuration.get("DB").toString(),
                                                              configuration.get("USER").toString(),
                                                              configuration.get("PASSWORD").toString());
            }
        }
        catch (NullPointerException | SQLException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Método para obtener una instancia de UConexion por Singleton
     * @return Si la instancia es null se inicializa sino returna la instancia estática
     */
    public static UConexion getInstance()
    {
        if(UConexion.miInstancia == null)
        {
            UConexion.miInstancia = new UConexion();
        }
        return UConexion.miInstancia;
    }

    /**
     * Método para leer información de archivo de configuración
     * @param file Archivo de configuración de conexión
     * @return Retorna un Objeto JSON sino retorna null
     */
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

    /**
     * Setter de atributo private Connection
     * @return Retorna el valor del atributo connection
     */
    public Connection getConnection()
    {
        return this.connection;
    }
}

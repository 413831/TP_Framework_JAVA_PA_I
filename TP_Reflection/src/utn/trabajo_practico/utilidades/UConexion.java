package utn.trabajo_practico.utilidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UConexion
{
    private static UConexion miInstancia;
    private Connection connection;

    private UConexion()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "admin");
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

    public UConexion getInstance()
    {
        if(UConexion.miInstancia == null)
        {
            UConexion.miInstancia = new UConexion();
        }
        return UConexion.miInstancia;
    }

    public static void leer(File archivo) throws IOException
    {
        FileReader fileReader = new FileReader(archivo);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String texto = "";
        while((texto = bufferedReader.readLine()) != null)
        {
            //TODO Retornar String concatenado en vez de imprimir en método (SOLID)
            System.out.println(texto);
        }
        fileReader.close();
    }

    public Connection getConnection()
    {
        return connection;
    }
}

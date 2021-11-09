package utn.trabajo_practico.servicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion
{
    private static Conexion miInstancia;
    private Connection connection;

    private Conexion()
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

    public Conexion getInstance()
    {
        if(Conexion.miInstancia == null)
        {
            Conexion.miInstancia = new Conexion();
        }
        return Conexion.miInstancia;
    }

    public Connection getConnection()
    {
        return connection;
    }
}

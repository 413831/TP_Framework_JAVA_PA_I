package utn.trabajo_practico;

import utn.trabajo_practico.clases.Auto;
import utn.trabajo_practico.clases.Domicilio;
import utn.trabajo_practico.clases.Persona;
import utn.trabajo_practico.servicios.Consultas;
import utn.trabajo_practico.utilidades.UBean;
import utn.trabajo_practico.utilidades.UConexion;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args)
    {
        System.out.println("MAIN");
        Persona persona = new Persona(33222111,"Pepito","Gomez",new Domicilio("CALLE1234","Calle",1234,222,"CABA"),113232322,"mail@mail.com");
        Auto auto = new Auto(new BigInteger(String.valueOf(35)), "Ford", "Focus", 123400);


        Consultas.guardarModificar(auto);
        // TEST
        //Auto nuevoAuto = (Auto) Consultas.obtenerPorId(Auto.class, auto.getId());
        //System.out.println("nuevoAuto = " + nuevoAuto);
        // Consultas.modificar(auto);
        // System.out.println(Consultas.guardar(auto));
        // Consultas.eliminar(persona);
        // System.out.println(Consultas.obtenerPorId(Auto.class,35));

    }
}

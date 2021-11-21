package utn.trabajo_practico;

import utn.trabajo_practico.clases.Auto;
import utn.trabajo_practico.clases.Domicilio;
import utn.trabajo_practico.clases.Persona;
import utn.trabajo_practico.servicios.Consultas;
import utn.trabajo_practico.utilidades.UBean;
import utn.trabajo_practico.utilidades.UConexion;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args)
    {
        System.out.println("MAIN");
        Persona persona = new Persona(33222111,"Pepito","Gomez",new Domicilio("CALLE1234","Calle",1234,222,"CABA"),113232322,"mail@mail.com");
        Auto auto = new Auto("Ford", "Focus", 88000);

        System.out.println(Consultas.guardar(auto));

        // TEST
        //Auto nuevoAuto = (Auto) Consultas.obtenerPorId(Auto.class, auto.getId());
        //System.out.println("nuevoAuto = " + nuevoAuto);
        // System.out.println(Consultas.obtenerPorId(Auto.class,2));
        // Consultas.modificar(auto);

        // Consultas.eliminar(persona);

    }
}

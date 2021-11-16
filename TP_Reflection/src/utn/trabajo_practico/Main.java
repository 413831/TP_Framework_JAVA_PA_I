package utn.trabajo_practico;

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
        Persona persona = new Persona("Pepito","Gomez",33222111,new Domicilio("CALLE1234","Calle",1234,222,"CABA"),113232322,"mail@mail.com");

        Consultas.guardar(persona);

    }
}

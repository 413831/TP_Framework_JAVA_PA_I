package utn.trabajo_practico.anotaciones;

import java.lang.annotation.*;

/**
 * @author Alejandro Planes
 * @version 1.0
 * Anotación para indicar tabla de la base de datos asociada a la Clase
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Tabla
{
    String nombre();
}

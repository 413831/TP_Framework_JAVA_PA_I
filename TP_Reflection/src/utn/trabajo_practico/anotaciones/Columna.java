package utn.trabajo_practico.anotaciones;

import java.lang.annotation.*;

/**
 * @author Alejandro Planes
 * @version 1.0
 * Anotación para identificar atributos de POJO como columnas de una tabla de base de datos
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Columna
{
    String nombre();
}

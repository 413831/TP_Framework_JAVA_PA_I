package utn.trabajo_practico.anotaciones;

import java.lang.annotation.*;

/**
 * @author Alejandro Planes
 * @version 1.0
 * Anotación para identificar atributo como Identificador de un registro de la base de datos
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id
{
}

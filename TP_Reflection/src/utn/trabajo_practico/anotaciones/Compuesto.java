package utn.trabajo_practico.anotaciones;

import java.lang.annotation.*;

/**
 * @author Alejandro Planes
 * @version 1.0
 * Anotación para identificar atributo de tipo Object que deba ser guardado en la base de datos
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Compuesto
{
    Class clazz();
}

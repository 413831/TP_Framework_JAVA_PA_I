package utn.trabajo_practico.anotaciones;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Compuesto
{
    Class clazz();
}

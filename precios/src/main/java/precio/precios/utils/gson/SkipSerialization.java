package precio.precios.utils.gson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface SkipSerialization {
}

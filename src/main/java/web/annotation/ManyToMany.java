package web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value= RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface ManyToMany {
    public String foreignTable();
    public String tableForManyToMany();
    public String mainColumnDB();
    public String foreignColumnDB();

}

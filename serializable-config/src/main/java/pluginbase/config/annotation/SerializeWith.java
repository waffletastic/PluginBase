package pluginbase.config.annotation;

import pluginbase.config.serializers.Serializer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define what {@link Serializer} to use to serialize/deserialize the value of a class field to/from
 * its serialized form.
 */
@Documented
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SerializeWith {
    Class<? extends Serializer> value();
}

package io.github.kobakei.grenade.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation denotes that the field is optional.
 * Created by keisuke on 16/04/17.
 */
@Target(ElementType.FIELD)
public @interface Optional {
}

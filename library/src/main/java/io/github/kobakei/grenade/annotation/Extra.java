package io.github.kobakei.grenade.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation denotes that the field can be put and injected by Grenade.
 * Created by keisukekobayashi on 16/04/08.
 */
@Target(ElementType.FIELD)
public @interface Extra {
    /**
     * Internal key used by getExtra/putExtra.
     * @return
     */
    String key() default "";
}

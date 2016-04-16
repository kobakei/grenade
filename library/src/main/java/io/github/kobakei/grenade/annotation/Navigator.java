package io.github.kobakei.grenade.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation denotes that Grenade generates to build and inject Intent for
 * the class (Activity, Service, BroadcastReceiver)
 * Created by keisukekobayashi on 16/04/08.
 */
@Target(ElementType.TYPE)
public @interface Navigator {
    /**
     * List of sets of required params used to generate constructors.
     * @return
     */
    String[] value() default {};
}

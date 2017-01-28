package io.github.kobakei.grenade.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by keisuke on 2017/01/28.
 */
@Target(ElementType.METHOD)
public @interface OnActivityResult {
    /**
     * Request code to be handled
     * @return
     */
    int requestCode();

    /**
     * Result codes to be handled
     * @return
     */
    int[] resultCodes();
}

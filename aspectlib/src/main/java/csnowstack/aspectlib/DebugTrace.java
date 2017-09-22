package csnowstack.aspectlib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */

@Retention(RetentionPolicy.CLASS)//保留到最后
@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
public  @interface  DebugTrace {

}

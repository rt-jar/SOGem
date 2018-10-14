package com.st.gem.parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {
	public String value() default "";
	
	public String separator() default "";
	
	public int startIdx() default 0;
}

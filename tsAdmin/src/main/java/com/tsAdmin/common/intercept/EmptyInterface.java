package com.tsAdmin.common.intercept;
 
import java.lang.annotation.Inherited;  
import java.lang.annotation.Retention;  
import java.lang.annotation.Target;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.ElementType;  
  
 

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface EmptyInterface {
    String[] value();
}

 
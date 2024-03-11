package com.example.demo.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //w ten sposób definiuje tablice.
@Retention(RetentionPolicy.RUNTIME) //czyli to odpowaida za to jak długo ta adnotacja ma działać
@interface IllegalExceptionProcessing {

}

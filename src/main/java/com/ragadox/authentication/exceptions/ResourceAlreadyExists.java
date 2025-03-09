package com.ragadox.authentication.exceptions;

public class ResourceAlreadyExists extends RuntimeException {

    public ResourceAlreadyExists() {
        super("Resource already exists");
    }

    public ResourceAlreadyExists(Class<?> tClass){
        super(String.format("Resource of type %s already exists",tClass.getSimpleName()));
    }
}

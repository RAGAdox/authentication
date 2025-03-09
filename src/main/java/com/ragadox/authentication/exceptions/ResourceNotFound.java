package com.ragadox.authentication.exceptions;

public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound(Class<?> tClass){
      super(String.format("Resource of type %s not found",tClass.getSimpleName()));
    }

}

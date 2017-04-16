package com.ss.schedulesys.service.errors;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * View Model for transferring error message with a list of field errors.
 */
@Data
@AllArgsConstructor
public class ErrorVM implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;
    
    private final String description;
    
    private List<FieldErrorVM> fieldErrors;
    
    public ErrorVM(String message, String description){
    	this(message, description, null);
    }
    
    public ErrorVM(String message){
    	this(message, null, null);
    }
    
    public void add(String objectName, String field, String message) {
        if (fieldErrors == null) {
            fieldErrors = new ArrayList<>();
        }
        fieldErrors.add(new FieldErrorVM(objectName, field, message));
    }
}
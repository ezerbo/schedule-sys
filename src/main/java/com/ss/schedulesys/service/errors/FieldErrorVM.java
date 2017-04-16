package com.ss.schedulesys.service.errors;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class FieldErrorVM implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    private final String objectName;

    @Getter
    private final String field;
    
    @Getter
    private final String message;

}

package com.pm.patientservice.exception;

public class DOBCannotBeUpdated extends RuntimeException {
    public DOBCannotBeUpdated(String message){
        super(message);
    }
}

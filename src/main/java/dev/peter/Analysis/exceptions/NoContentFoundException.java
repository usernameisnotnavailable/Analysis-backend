package dev.peter.Analysis.exceptions;

public class NoContentFoundException extends RuntimeException {

    public NoContentFoundException(){
        super();
    }
    public NoContentFoundException(String message){
        super(message);
    }

}

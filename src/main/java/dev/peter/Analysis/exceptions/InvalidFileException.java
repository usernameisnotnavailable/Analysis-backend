package dev.peter.Analysis.exceptions;

public class InvalidFileException extends Exception{

    public InvalidFileException() {
        super();
    }
    public InvalidFileException(String message){
         super(message);
    }

}

package br.com.deolhonacamara.exception;

import lombok.Getter;

@Getter
public class RequesterException extends RuntimeException {

    public RequesterException( String message) {
        super(message);
    }

}
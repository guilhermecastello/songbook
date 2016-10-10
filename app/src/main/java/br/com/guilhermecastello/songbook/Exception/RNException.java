package br.com.guilhermecastello.songbook.Exception;



/**
 * Created by guilh on 10/8/2016.
 */

public class RNException extends RuntimeException {

    private String message;

    public RNException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

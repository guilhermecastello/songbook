package br.com.guilhermecastello.songbook.type;

import java.io.Serializable;

/**
 * Created by guilh on 10/19/2016.
 */

public class BaseType implements Serializable {

    private Short status;
    private String result;

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

package com.cesantoni.produccion.productionreader.dao;

/**
 * Created by liel on 04/05/2017.
 */

public class Calibre {
    private String key;
    private String value;

    public Calibre(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Tono){
            Tono c = (Tono )obj;
            if(c.getValue().equals(value) && c.getKey()==key ) return true;
        }
        return false;
    }
}

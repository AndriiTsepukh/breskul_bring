package org.breskul.bobo.prebean;

import lombok.Data;

@Data
public class PreBean<T> {
    private String name;
    private T obj;
    private boolean cooked;

    public PreBean(String name, T obj) {
        this.name = name;
        this.obj = obj;
    }

}

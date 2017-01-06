package com.nata.xdroid.injector;

/**
 * Created by Calvin on 2016/12/10.
 */

public class ContactBean {
    private String name;
    private String number;

    public ContactBean() {
        super();
    }

    public ContactBean(String name, String number) {
        super();
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "ContactBean [name=" + name + ", number=" + number + "]";
    }

}

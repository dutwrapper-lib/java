package io.zoemeow.dutapi.objects.customrequest;

import java.io.Serializable;

public class CustomRequestItem implements Serializable {
    private String name = null;
    private String value = null;

    public CustomRequestItem() { }

    public CustomRequestItem(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

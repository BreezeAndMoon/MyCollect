package com.joint.jointpolice.model;

import org.codehaus.jackson.annotate.JacksonAnnotation;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.jar.Attributes;

/**
 * Created by Joint229 on 2018/2/8.
 */

public class Student {
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Id")
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

}



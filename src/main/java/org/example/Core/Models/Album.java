package org.example.Core.Models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class Album implements Serializable {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String ALBUM = "Album";

    private int id;
    private String name;

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
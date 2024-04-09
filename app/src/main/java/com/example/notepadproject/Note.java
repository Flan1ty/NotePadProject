package com.example.notepadproject;

import java.io.Serializable;

public class Note implements Serializable {

    private String id;

    private String title;

    private String description;

    public Note(
            String id,
            String title,
            String description
    ) {
        if (id == null) {
            id = "";
        }
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Note() {

    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

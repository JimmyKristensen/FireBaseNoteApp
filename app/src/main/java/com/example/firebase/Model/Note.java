package com.example.firebase.Model;

public class Note {
    private String text;
    private String imageName;

    public Note(String text, String imageName) {
        this.text = text;
        this.imageName = imageName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}

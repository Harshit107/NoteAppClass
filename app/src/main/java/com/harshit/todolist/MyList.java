package com.harshit.todolist;

public class MyList {

    String text;
    String id;

    public MyList(String text, String id) {
        this.text = text;
        this.id = id;
    }
    public MyList(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

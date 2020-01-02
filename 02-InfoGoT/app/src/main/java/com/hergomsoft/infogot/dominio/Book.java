package com.hergomsoft.infogot.dominio;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Book {

    private int idb;
    private String name;
    private int npages;
    private String released;
    private List<Character> characters;

    public Book(JSONObject json){
        try {
            String url = json.getString("url");
            idb = Integer.parseInt(url.substring(url.lastIndexOf("/") + 1));
            name = json.getString("name");
            npages = json.getInt("numberOfPages");
            released = json.getString("released");
            released = released.substring(0, released.length() - 9);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    public int getIdb() {
        return idb;
    }

    public String getName() {
        return name;
    }

    public int getNpages() {
        return npages;
    }

    public String getReleased() {
        return released;
    }

    public List<Character> getCharacters() {
        return characters;
    }
}

package com.hergomsoft.infogot.dominio;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Character {

    private int idc;
    private String name;
    private String gender;
    private String culture;
    private String born;
    private String died;
    private List<String> titles;
    private List<String> aliases;
    private List<House> allegiances;
    private Character spouse;
    private Character father;
    private Character mother;
    private List<Book> books;

    public Character(JSONObject json){
        try{
            String url = json.getString("url");
            idc = Integer.parseInt(url.substring(url.lastIndexOf("/") + 1));
            name = json.getString("name");
            gender = json.getString("gender");
            culture = json.getString("culture");
            born = json.getString("born");
            died = json.getString("diedOut");
            titles = new ArrayList<>();
            JSONArray jtitles=json.getJSONArray("titles");
            for (int i=0;i<jtitles.length();i++)
                titles.add(jtitles.get(i).toString());
            aliases = new ArrayList<>();
            JSONArray jaliases=json.getJSONArray("aliases");
            for (int i=0;i<jtitles.length();i++)
                aliases.add(jaliases.get(i).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setAllegiances(List<House> allegiances) {
        this.allegiances = allegiances;
    }

    public void setSpouse(Character spouse) {
        this.spouse = spouse;
    }

    public void setFather(Character father) {
        this.father = father;
    }

    public void setMother(Character mother) {
        this.mother = mother;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public int getIdc() {
        return idc;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getCulture() {
        return culture;
    }

    public String getBorn() {
        return born;
    }

    public String getDied() {
        return died;
    }

    public List<String> getTitles() {
        return titles;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public List<House> getAllegiances() {
        return allegiances;
    }

    public Character getSpouse() {
        return spouse;
    }

    public Character getFather() {
        return father;
    }

    public Character getMother() {
        return mother;
    }

    public List<Book> getBooks() {
        return books;
    }
}

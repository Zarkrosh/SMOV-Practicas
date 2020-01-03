package com.hergomsoft.infogot.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class House {

    private int idh;
    private String name;
    private String region;
    private String words;
    private String founded;
    private String died;
    private String coatOfArms;
    private Character lord;
    private Character heir;
    private Character founder;
    private House overlord;
    private List<String> titles;
    private List<String> seats;
    private List<String> ancestralWeapons;
    private List<House> cadetBranches;
    private List<Character> members;

    public House(JSONObject json){
        try {
            String url = json.getString("url");
            idh = Integer.parseInt(url.substring(url.lastIndexOf("/") + 1));
            name = json.getString("name");
            region = json.getString("region");
            words = json.getString("words");
            founded = json.getString("founded");
            died = json.getString("diedOut");
            coatOfArms = json.getString("coatOfArms");
            titles = new ArrayList<>();
            JSONArray jtitles=json.getJSONArray("titles");
            for (int i=0;i<jtitles.length();i++)
                titles.add(jtitles.get(i).toString());
            seats = new ArrayList<>();
            JSONArray jseats=json.getJSONArray("seats");
            for (int i=0;i<jseats.length();i++)
                seats.add(jseats.get(i).toString());
            ancestralWeapons = new ArrayList<>();
            JSONArray jweapons=json.getJSONArray("ancestralWeapons");
            for (int i=0;i<jweapons.length();i++)
                ancestralWeapons.add(jweapons.get(i).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setLord(Character lord) {
        this.lord = lord;
    }

    public void setHeir(Character heir) {
        this.heir = heir;
    }

    public void setFounder(Character founder) {
        this.founder = founder;
    }

    public void setOverlord(House overlord) {
        this.overlord = overlord;
    }

    public void setCadetBranches(List<House> cadetBranches) {
        this.cadetBranches = cadetBranches;
    }

    public void setMembers(List<Character> members) {
        this.members = members;
    }

    public int getIdh() {
        return idh;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getWords() {
        return words;
    }

    public String getFounded() {
        return founded;
    }

    public String getDied() {
        return died;
    }

    public String getCoatOfArms() {
        return coatOfArms;
    }

    public Character getLord() {
        return lord;
    }

    public Character getHeir() {
        return heir;
    }

    public Character getFounder() {
        return founder;
    }

    public House getOverlord() {
        return overlord;
    }

    public List<String> getTitles() {
        return titles;
    }

    public List<String> getSeats() {
        return seats;
    }

    public List<String> getAncestralWeapons() {
        return ancestralWeapons;
    }

    public List<House> getCadetBranches() {
        return cadetBranches;
    }

    public List<Character> getMembers() {
        return members;
    }
}

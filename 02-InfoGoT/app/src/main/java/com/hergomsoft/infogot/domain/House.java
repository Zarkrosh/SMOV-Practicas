package com.hergomsoft.infogot.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class House {

    private Integer id;
    private String name;
    private String region;
    private String words;
    private String founded;
    private String died;
    private String coatOfArms;
    private Integer lord;
    private Integer heir;
    private Integer founder;
    private Integer overlord;

    //TODO cosnstructor: vacio, estandar, basado en cursor o varios a la vez
    public House(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getFounded() {
        return founded;
    }

    public void setFounded(String founded) {
        this.founded = founded;
    }

    public String getDied() {
        return died;
    }

    public void setDied(String died) {
        this.died = died;
    }

    public String getCoatOfArms() {
        return coatOfArms;
    }

    public void setCoatOfArms(String coatOfArms) {
        this.coatOfArms = coatOfArms;
    }

    public Integer getLord() {
        return lord;
    }

    public void setLord(Integer lord) {
        this.lord = lord;
    }

    public Integer getHeir() {
        return heir;
    }

    public void setHeir(Integer heir) {
        this.heir = heir;
    }

    public Integer getFounder() {
        return founder;
    }

    public void setFounder(Integer founder) {
        this.founder = founder;
    }

    public Integer getOverlord() {
        return overlord;
    }

    public void setOverlord(Integer overlord) {
        this.overlord = overlord;
    }
}

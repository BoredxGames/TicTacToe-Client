/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.model;

/**
 *
 * @author Hazem
 */
public class AuthRequestEntity {
    private String id ;
    private String userName ;
    private int Score;

    public AuthRequestEntity(String id, String userName, int Score) {
        this.id = id;
        this.userName = userName;
        this.Score = Score;
    }

    public AuthRequestEntity() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setScore(int Score) {
        this.Score = Score;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public int getScore() {
        return Score;
    }

    @Override
    public String toString() {
        return "AuthRequestEntity{" + "id=" + id + ", userName=" + userName + ", Score=" + Score + '}';
    }
    
}

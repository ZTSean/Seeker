package com.gamedesign.seeker;

import java.util.List;

/**
 * Created by Zixiao on 11/2/2016.
 */

public class Game {
    private int id;
    private String game_name;

    public Game() {
    }

    public Game(String game_name) {
        this.game_name = game_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

}

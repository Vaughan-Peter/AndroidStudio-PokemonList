package com.codelab.basics;

public class DataModel {

    //"CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name VARCHAR(256), pokedex INTEGER, moves VARCHAR(256), level INTERGER, access_count INTERGER)";
    private long id;
    private String name;
    private int pokedex;
    private String moves;
    private int levels;
    private int access_count;
    private String link_col;
    private byte[] image_col;

    public DataModel(long id, String name, int pokedex, String moves, int levels, int access_count, String link, byte[] image_col) {
        this.id = id;
        this.name = name;
        this.pokedex = pokedex;
        this.moves = moves;
        this.levels = levels;
        this.access_count = access_count;
        this.link_col = link;
        this.image_col = image_col;
    }

    @Override
    public String toString() {

        return "\nSome Pokemon Info:" +
                "\n\nDatabase ID: " + getId() +
                "\nPokemon Name: " + getName() +
                "\nPokedex Number: " + getPokedex() +
                "\nFour Moves: " + getMoves() +
                "\nLevel: " + getLevels() +
                "\nAccess Count: " + getAccess_count() + "\n";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPokedex() {
        return pokedex;
    }

    public void setPokedex(int pokedex) {
        this.pokedex = pokedex;
    }

    public String getMoves() {
        return moves;
    }

    public void setMoves(String moves) {
        this.moves = moves;
    }

    public int getLevels() {
        return levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public int getAccess_count() {
        return access_count;
    }

    public void setAccess_count(int access_count) {
        this.access_count = access_count;
    }

    public void setGetLink(String link) {
        this.link_col = link;
    }

    public String getGetLink() {
        return link_col;
    }

    public void setImage_col(byte[] image_col){
        this.image_col = image_col;
    }

    public byte[] getImage_col(){
        return image_col;
    }
}
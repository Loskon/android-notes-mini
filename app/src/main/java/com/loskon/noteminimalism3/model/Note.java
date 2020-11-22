package com.loskon.noteminimalism3.model;

/**
 * Объект модели, который хранит данные и управляет ими
 */

public class Note {
    private final long id;
    private final String title;
    private final String date;
    private boolean favoritesItem;
    private boolean selectItemForDel;

    public Note(long id, String title, String date, boolean favoritesItem, boolean selectItemForDel){
        this.id = id;
        this.title = title;
        this.date = date;
        this.favoritesItem = favoritesItem;
        this.selectItemForDel = selectItemForDel;
    }
    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDate() {
        return date;
    }
    public boolean getFavoritesItem() {
        return favoritesItem;
    }
    public void setFavoritesItem(boolean favoritesItem) {
        this.favoritesItem = favoritesItem;
    }
    public boolean getSelectItemForDel() {
        return selectItemForDel;
    }
    public void setSelectItemForDel(boolean selectItemForDel) {
        this.selectItemForDel = selectItemForDel;
    }
}

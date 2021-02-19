package com.loskon.noteminimalism3.model;

import java.util.Date;

/**
 * Объект модели, который хранит данные и управляет ими
 */

public class Note {
    private final long id;
    private String title;
    private Date date;
    private final Date dateDelete;
    private boolean favoritesItem;
    private boolean selectItemForDel;

    public Note(long id, String title, Date date, Date dateDelete,
                boolean favoritesItem, boolean selectItemForDel) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.dateDelete = dateDelete;
        this.favoritesItem = favoritesItem;
        this.selectItemForDel = selectItemForDel;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDateDelete() {
        return dateDelete;
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

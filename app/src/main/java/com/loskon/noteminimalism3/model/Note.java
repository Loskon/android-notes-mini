package com.loskon.noteminimalism3.model;

import java.util.Date;

/**
 * A model class for a notes
 */

public class Note {
    private final long id;
    private String title;
    private Date date;
    private final Date dateMod;
    private final Date dateDelete;
    private boolean favoritesItem;
    private boolean selectItemForDel;

    public Note(long id, String title, Date date, Date dateMod, Date dateDelete,
                boolean favoritesItem) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.dateMod = dateMod;
        this.dateDelete = dateDelete;
        this.favoritesItem = favoritesItem;
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

    public Date getDateMod() {
        return dateMod;
    }
}

package com.cesantoni.produccion.productionreader.dao;

/**
 * Created by liel on 08/06/2017.
 */

public class ModeloListaArchivos {
    private int icon;
    private String title;

    private boolean isGroupHeader = false;

    public ModeloListaArchivos(String title) {
        this(-1,title);
        isGroupHeader = true;
    }
    public ModeloListaArchivos(int icon, String title) {
        super();
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isGroupHeader() {
        return isGroupHeader;
    }

    public void setGroupHeader(boolean groupHeader) {
        isGroupHeader = groupHeader;
    }
}

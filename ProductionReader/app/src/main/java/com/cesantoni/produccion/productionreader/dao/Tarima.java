package com.cesantoni.produccion.productionreader.dao;

import java.io.Serializable;

/**
 * Created by liel on 16/05/2017.
 */

public class Tarima implements Serializable {

    private String codigocompleto;
    private String modelo;
    private String color;
    private String calidad;
    private String tamaño;
    private String formato;
    private String dec;
    private String tono;
    private String calibre;
    private String cantCajas;
    private String lote;
    private String metrosPorTarima;
    private boolean tarima_completa;

    public Tarima() {
    }

    public String getCodigocompleto() {
        return codigocompleto;
    }

    public void setCodigocompleto(String codigocompleto) {
        this.codigocompleto = codigocompleto;
    }

    public String getCantCajas() {
        return cantCajas;
    }

    public void setCantCajas(String cantCajas) {
        this.cantCajas = cantCajas;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCalidad() {
        return calidad;
    }

    public void setCalidad(String calidad) {
        this.calidad = calidad;
    }

    public String getTamaño() {
        return tamaño;
    }

    public void setTamaño(String tamaño) {
        this.tamaño = tamaño;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public String getTono() {
        return tono;
    }

    public void setTono(String tono) {
        this.tono = tono;
    }

    public String getCalibre() {
        return calibre;
    }

    public void setCalibre(String calibre) {
        this.calibre = calibre;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getMetrosPorTarima() {
        return metrosPorTarima;
    }

    public void setMetrosPorTarima(String metrosPorTarima) {
        this.metrosPorTarima = metrosPorTarima;
    }

    public boolean isTarima_completa() {
        return tarima_completa;
    }

    public void setTarima_completa(boolean tarima_completa) {
        this.tarima_completa = tarima_completa;
    }



}

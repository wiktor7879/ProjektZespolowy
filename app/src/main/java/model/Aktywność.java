package model;

import android.widget.AdapterView;

public class Aktywność {

    private String Data;
    private String Odległość;
    private Integer Kalorie;
    private String Czas;

    public Aktywność(){}

    public Aktywność(String Data,String Odleglosc,Integer Kalorie,String Czas)
    {
        this.Data = Data;
        this.Odległość = Odleglosc;
        this.Kalorie = Kalorie;
        this.Czas = Czas;
    }

    public String getCzas() {
        return Czas;
    }

    public void setCzas(String czas) {
        Czas = czas;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public Integer getKalorie() {
        return Kalorie;
    }

    public void setKalorie(Integer kalorie) {
        Kalorie = kalorie;
    }

    public String getOdległość() {
        return Odległość;
    }

    public void setOdległość(String odległość) {
        Odległość = odległość;
    }
}

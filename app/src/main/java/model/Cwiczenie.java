package model;

import java.util.List;

public class Cwiczenie {
    private String Nazwa;
    private String PartiaCiala;
    private String Opis;
    private Integer Serie;
    private List<Seria> ListaSerii;

    public Cwiczenie(){}

    public Cwiczenie(String Nazwa,String PartiaCiala, String Opis, Integer Serie, List<Seria> ListaSerii){
        this.Nazwa = Nazwa;
        this.PartiaCiala = PartiaCiala;
        this.Opis = Opis;
        this.Serie = Serie;
        this.ListaSerii = ListaSerii;
    }

    public String getNazwa() {
        return Nazwa;
    }

    public void setNazwa(String Nazwa) {
        this.Nazwa = Nazwa;
    }

    public String getPatriaCiala() { return PartiaCiala; }

    public void setPartiaCiala(String Partia_Ciala) { this.PartiaCiala = PartiaCiala; }

    public String getOpis() {
        return Opis;
    }

    public void setOpis(String Opis) {
        this.Opis = Opis;
    }

    public Integer getSerie() { return Serie; }

    public void setSerie(Integer Serie) {
        this.Serie = Serie;
    }

    public List<Seria> getListaSerii() {return ListaSerii;}

    public void setListaSerii(List<Seria> ListaSerii) {this.ListaSerii = ListaSerii;}

}



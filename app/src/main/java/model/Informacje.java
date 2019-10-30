package model;

public class Informacje {
    private String Imie;
    private String DataUrodzenia;
    private String Plec;
    private Integer Wzrost;
    private Integer Waga;


    public Informacje() {
    }

    public Informacje(String Imie, String DataUrodzenia, String Plec, Integer Wzrost, Integer Waga) {
        this.Imie = Imie;
        this.DataUrodzenia = DataUrodzenia;
        this.Plec = Plec;
        this.Waga = Waga;
        this.Wzrost = Wzrost;
    }

    public String getImie() {
        return Imie;
    }

    public void setImie(String Imie) {
        this.Imie = Imie;
    }

    public String getDataUrodzenia() { return DataUrodzenia; }

    public void setDataUrodzenia(String DataUrodzenia) {
        this.DataUrodzenia = DataUrodzenia;
    }

    public String getPlec() {
        return Plec;
    }

    public void setPlec(String Plec) {
        this.Plec = Plec;
    }

    public Integer getWzrost() {
        return Wzrost;
    }

    public void setWzrost(Integer Wzrost) { this.Wzrost = Wzrost; }

    public Integer getWaga() { return Waga; }

    public void setWaga(Integer Waga) {
        this.Waga = Waga;
    }

}
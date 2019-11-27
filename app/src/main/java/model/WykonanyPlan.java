package model;

import java.util.List;

public class WykonanyPlan {

    private Integer idPlanu;
    private List<CwiczenieDoPlanu> listaCwiczen;

    public WykonanyPlan(){}

    public WykonanyPlan(Integer idPlanu,List<CwiczenieDoPlanu> listaCwiczen)
    {
        this.idPlanu=idPlanu;
        this.listaCwiczen=listaCwiczen;
    }

    public Integer getId() {
        return idPlanu;
    }

    public void setId(Integer Id) {
        this.idPlanu = idPlanu;
    }

    public List<CwiczenieDoPlanu> getListaCwiczen() {return listaCwiczen;}

    public void setListaCwiczen(List<CwiczenieDoPlanu> listaCwiczen) {this.listaCwiczen = listaCwiczen;}


}

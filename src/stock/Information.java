/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stock;

/**
 *
 * @author hfmlcode
 */
public class Information {

    private String intitule;
    private float prix;

    public Information() {
    }

    public Information(String intitule, float prix) {
        this.intitule = intitule;
        this.prix = prix;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }
}

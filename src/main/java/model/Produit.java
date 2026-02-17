package model;

public class Produit {
    private int id;
    private String nom;
    private float prix;
    private int stock;

    public Produit(int _id, String _nom, float _prix, int _stock) {
        id = _id;
        nom = _nom;
        prix = _prix;
        stock = _stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}

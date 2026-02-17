package model;

import java.util.Date;

public class Commande {
    private int id, idClient;
    private String date;
    private float total;

    public Commande(int _idClient, String _date, float _total) {
        idClient = _idClient;
        date = _date;
        total = _total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}

package model;

import java.util.Date;

public class Commande {
    private int id, idClient;
    private Date date;
    private float total;

    public Commande(int _id, int _idClient, Date _date, float _total) {
        id = _id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}

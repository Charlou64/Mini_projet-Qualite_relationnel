package model;

public class LigneCommande {
    private int idCommande, idProduit, quantite;

    public LigneCommande(int _idCommande, int _idProduit, int _quantite) {
        idCommande = _idCommande;
        idProduit = _idProduit;
        quantite = _quantite;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
}

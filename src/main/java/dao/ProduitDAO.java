package dao;

import model.Client;
import model.Produit;
import java.sql.*;

public class ProduitDAO {

    private static ProduitDAO instance;

    private ProduitDAO() {}

    /**
     * @return Le Singleton
     */
    public static ProduitDAO getInstance() {
        if (instance == null)
            instance = new ProduitDAO();
        return instance;
    }

    /**
     * Récupérer un produit par son nom
     * @param nom le nom du produit
     * @return le produit ayant le bon nom
     * @throws SQLException
     */
    public Produit findByName(String nom) throws SQLException {
        String query = "SELECT * FROM Produits WHERE nom = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, nom);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Produit p = new Produit(rs.getString("nom"), rs.getFloat("prix"), rs.getInt("quantite_stock"));
                p.setId(rs.getInt("id_produit"));
                return p;
            }
        }
        return null;
    }

    /**
     * Actualiser les stock d'un produit
     * @param idProduit l'id du produit
     * @param quantiteVendue la quantité vendue
     * @throws SQLException
     */
    public void updateStock(int idProduit, int quantiteVendue) throws SQLException {
        String query = "UPDATE Produits SET quantite_stock = quantite_stock - ? WHERE id_produit = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setInt(1, quantiteVendue);
            ps.setInt(2, idProduit);
            ps.executeUpdate();
        }
    }
    
    /**
     * Insérer un nouveau produit
     * @param produit le produit a insérer
     * @return l'id du client
     * @throws SQLException
     */
    public int insert(Produit produit) throws SQLException {
        String query = "INSERT INTO Produits (nom, prix, quantite_stock) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, produit.getNom());
            ps.setFloat(2, produit.getPrix());
            ps.setInt(3, produit.getStock());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }
}
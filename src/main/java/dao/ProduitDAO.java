package dao;

import model.Produit;
import java.sql.*;

public class ProduitDAO {

    /**
     * @param nom
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
     * @param idProduit
     * @param quantiteVendue
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
}
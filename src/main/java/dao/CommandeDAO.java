package dao;

import model.Commande;
import model.LigneCommande;
import java.sql.*;

public class CommandeDAO {

    /**
     * @param cmd
     * @return L'id de la commande
     * @throws SQLException
     */
    public int insertCommande(Commande cmd) throws SQLException {
        String query = "INSERT INTO Commandes (id_client, date_commande, total_commande) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, cmd.getIdClient());
            ps.setString(2, cmd.getDate());
            ps.setDouble(3, cmd.getTotal());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }

    public void insertLigne(LigneCommande ligne) throws SQLException {
        String query = "INSERT INTO Lignes_Commande (id_commande, id_produit, quantite_commandee, prix_unitaire_facture) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setInt(1, ligne.getIdCommande());
            ps.setInt(2, ligne.getIdProduit());
            ps.setInt(3, ligne.getQuantite());
            ps.setDouble(4, ligne.getPrixUnitaire());
            ps.executeUpdate();
        }
    }
}
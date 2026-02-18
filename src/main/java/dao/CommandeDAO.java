package dao;

import model.Commande;
import model.LigneCommande;
import org.jdom2.Element;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * @return Une liste les commandes (pour export depuis la base)
     * @throws SQLException
     */
    public List<Element> getCommandesPourExport() throws SQLException {
        List<Element> elementsCommandes = new ArrayList<>();

        String sql = "SELECT c.*, cl.nom_client FROM Commandes c JOIN Clients cl ON c.id_client = cl.id_client";

        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int idCmd = rs.getInt("id_commande");

                // les commandes
                Element cmdElem = new Element("commande");
                cmdElem.setAttribute("id", "C" + idCmd);

                cmdElem.addContent(new Element("nom-client").setText(rs.getString("nom_client")));
                cmdElem.addContent(new Element("date").setText(rs.getString("date_commande")));

                // les lignes
                List<Element> lignes = getLignesPourExport(idCmd);
                Element produitsElem = new Element("produits");
                produitsElem.setAttribute("nb-produit", String.valueOf(lignes.size()));
                produitsElem.addContent(lignes);

                cmdElem.addContent(produitsElem);
                cmdElem.addContent(new Element("total").setText(String.valueOf(rs.getDouble("total_commande"))));

                elementsCommandes.add(cmdElem);
            }
        }
        return elementsCommandes;
    }

    /**
     * @param idCommande
     * @return La listes des produits (pour l'export depuis la base)
     * @throws SQLException
     */
    private List<Element> getLignesPourExport(int idCommande) throws SQLException {
        List<Element> lignes = new ArrayList<>();
        String sql = "SELECT lc.*, p.nom FROM Lignes_Commande lc JOIN Produits p ON lc.id_produit = p.id_produit WHERE lc.id_commande = ?";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idCommande);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Element p = new Element("produit");
                p.addContent(new Element("nom").setText(rs.getString("nom")));
                p.addContent(new Element("prix").setText(String.valueOf(rs.getDouble("prix_unitaire_facture"))));
                p.addContent(new Element("quantité").setText(String.valueOf(rs.getInt("quantite_commandee"))));
                lignes.add(p);
            }
        }
        return lignes;
    }
}
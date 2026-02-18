package dao;

import model.Commande;
import model.LigneCommande;
import org.jdom2.Element;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAO {

    private static CommandeDAO instance;

    private CommandeDAO() {}

    /**
     * @return Le Singleton
     */
    public static CommandeDAO getInstance() {
        if (instance == null)
            instance = new CommandeDAO();
        return instance;
    }
    
    /**
     * Insère une nouvelle commande
     * @param cmd la commande a insérer
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

    /**
     * Insérer une ligne (lie 1 commande a 1 produit)
     * @param ligne la ligne a insérer
     * @throws SQLException
     */
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
     * Récupérer la listes des éléments correspondant aux commandes de la base
     * @return Une liste les commandes (pour export depuis la base)
     * @throws SQLException
     */
    public List<Element> getCommandesPourExport() throws SQLException {
        List<Element> elementsCommandes = new ArrayList<>();

        String sql = "SELECT c.*, cl.nom_client, cl.email, cl.ville FROM Commandes c JOIN Clients cl ON c.id_client = cl.id_client";

        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int idCmd = rs.getInt("id_commande");

                // les commandes
                Element cmdElem = new Element("commande");
                cmdElem.setAttribute("id", "C" + idCmd);

                cmdElem.addContent(new Element("nom-client").setText(rs.getString("nom_client")));
                cmdElem.addContent(new Element("email").setText(rs.getString("email")));
                cmdElem.addContent(new Element("ville").setText(rs.getString("ville")));
                cmdElem.addContent(new Element("date").setText(rs.getString("date_commande")));

                cmdElem.addContent(new Element("total").setText(String.valueOf(rs.getDouble("total_commande"))));

                // les lignes
                List<Element> lignes = getLignesPourExport(idCmd);
                Element produitsElem = new Element("produits");
                cmdElem.setAttribute("nb-produit", String.valueOf(lignes.size()));
                produitsElem.addContent(lignes);

                cmdElem.addContent(produitsElem);

                elementsCommandes.add(cmdElem);
            }
        }
        return elementsCommandes;
    }

    /**
     * Récupérer la liste des éléments correspondant aux lignes liant les commandes aux produit dans la base.
     * @param idCommande l'id de la commande
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
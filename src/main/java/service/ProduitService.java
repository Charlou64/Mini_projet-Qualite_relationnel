package service;

import dao.ProduitDAO;
import model.Produit;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class ProduitService {
    private static ProduitService instance;

    private ProduitDAO produitDAO = ProduitDAO.getInstance();

    private ProduitService() {}

    /**
     * @return Le Singleton
     */
    public static ProduitService getInstance() {
        if (instance == null)
            instance = new ProduitService();
        return instance;
    }

    /**
     * @param cheminFichier
     */
    public void importerProduitsFournisseur(String cheminFichier) {
        try {
            System.out.println("--- Début de l'importation fournisseur ---");

            SAXBuilder saxbuilder = new SAXBuilder();
            Document document = saxbuilder.build(new File(cheminFichier));
            Element racine = document.getRootElement();

            // On récupère tous les éléments <produit>
            List<Element> liste_produits = racine.getChildren("produit");

            for (Element produitXml : liste_produits) {
                String nom = produitXml.getChildText("nom");
                float prixFournisseur = Float.parseFloat(produitXml.getChildText("prix"));
                int stock = Integer.parseInt(produitXml.getChildText("quantité"));

                // Logique métier : On double le prix pour la revente
                float prixVente = prixFournisseur * 2;

                Produit p = new Produit(nom, prixVente, stock);

                try {
                    // On utilise le DAO déjà présent dans le Singleton
                    produitDAO.insert(p);
                    System.out.println("Produit [" + nom + "] inséré (Prix vente: " + prixVente + ")");
                } catch (SQLException e) {
                    System.err.println("Erreur BDD pour " + nom + " : " + e.getMessage());
                }
            }

            System.out.println("--- Importation terminée ---");

        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
    }

    /**
     * Méthode utilitaire pour afficher le contenu d'un XML en console (Pretty Print)
     */
    public void afficherContenuXML(String cheminFichier) {
        try {
            SAXBuilder saxbuilder = new SAXBuilder();
            Document document = saxbuilder.build(new File(cheminFichier));
            XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
            System.out.println("Contenu du fichier " + cheminFichier + " :");
            xmlOutput.output(document, System.out);
        } catch (Exception e) {
            System.err.println("Impossible d'afficher le XML : " + e.getMessage());
        }
    }
}

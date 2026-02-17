package main;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import dao.ProduitDAO;
import model.Produit;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class CommandeParser {
	
	public void lireEtAfficher(String cheminFichier) {
        try {
        	
        	//Affichage propre + récuếration des données
        	SAXBuilder saxbuilder = new SAXBuilder();
        	Document document = (Document) saxbuilder.build(new File(cheminFichier));
        	Element racine = document.getRootElement();
        	List<Element> liste_produits = racine.getChildren();
        	
        	ProduitDAO pDao = new ProduitDAO();
        	
        	for (Element produitXml : liste_produits) {
        	    String nom = produitXml.getChildText("nom");
        	    float prixFournisseur = Float.parseFloat(produitXml.getChildText("prix"));
        	    int stock = Integer.parseInt(produitXml.getChildText("quantité"));

        	    float prixVente = prixFournisseur * 2;

        	    Produit p = new Produit(nom, prixVente, stock);
        	    
        	    try {
        	        pDao.insert(p); // La méthode que tu dois ajouter dans ProduitDAO
        	        System.out.println("Produit " + nom + " inséré avec succès (Prix doublé: " + prixVente + ")");
        	    } catch (SQLException e) {
        	        System.err.println("Erreur insertion : " + e.getMessage());
        	    }
        	}
        	
        	//Affichage sous la forme XML
        	XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
        	System.out.println("Affichage des données sous forme XML :");
        	xmlOutput.output(document, System.out);
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public static void main(String[] args) {
	        CommandeParser parser = new CommandeParser();
	        String chemin = "Produits.xml";
	        System.out.println("--- Début de l'analyse JDOM ---");
	        parser.lireEtAfficher(chemin);
	        System.out.println("--- Analyse terminée ---");
	    }

	
}

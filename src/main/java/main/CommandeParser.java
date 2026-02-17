package main;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.util.List;

public class CommandeParser {
	
	public void lireEtAfficher(String cheminFichier) {
        try {
        	
        	//Affichage propre + récuếration des données
        	SAXBuilder saxbuilder = new SAXBuilder();
        	Document document = (Document) saxbuilder.build(new File(cheminFichier));
        	Element racine = document.getRootElement();
        	List<Element> liste_produits = racine.getChildren();
        	
        	for (Element produit : liste_produits) {
        	    String nom = produit.getChildText("nom");        
        	    String prixS = produit.getChildText("prix");     
        	    String qteS = produit.getChildText("quantité");  
        	    System.out.println("Produit : " + nom + " | Prix fournisseur : " + prixS + "€");
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

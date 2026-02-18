package main;

import service.CommandeService;
import service.ProduitService;

import java.io.File;

public class App 
{
    public static void main( String[] args )
    {
        ProduitService produitService = ProduitService.getInstance();
        CommandeService commandeService = CommandeService.getInstance();

        produitService.importerProduitsFournisseur("Produits.xml");
        produitService.importerProduitsFournisseur("Produits.xml");

        commandeService.traiterCommande("Commandes.xml");

        commandeService.exporterCommandes("un_export.xml");
    }
}

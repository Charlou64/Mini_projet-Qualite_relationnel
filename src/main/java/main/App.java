package main;

import service.CommandeService;
import service.ProduitService;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProduitService produitService = ProduitService.getInstance();
        CommandeService commandeService = CommandeService.getInstance();

        boolean quitter = false;

        System.out.println("" + Color.CYAN + Color.BOLD + "========================================");
        System.out.println("   GESTIONNAIRE DE COMMANDES XML   ");
        System.out.println("========================================" + Color.RESET);

        while (!quitter) {
            System.out.println("\n" + Color.BOLD + "--- MENU PRINCIPAL ---" + Color.RESET);
            System.out.println(Color.YELLOW + "1." + Color.RESET + " Importer le catalogue fournisseur");
            System.out.println(Color.YELLOW + "2." + Color.RESET + " Traiter une commande client");
            System.out.println(Color.YELLOW + "3." + Color.RESET + " Exporter les commandes en XML");
            System.out.println(Color.YELLOW + "4." + Color.RESET + " Afficher les commandes de la base");
            System.out.println(Color.RED + "0." + Color.RESET + " Quitter");
            System.out.print("\nVotre choix : ");

            String choix = scanner.nextLine();

            switch (choix) {
                case "1":
                    String fileImport = demanderFichier(scanner, "Fichier catalogue à importer", "Produits.xml");
                    System.out.println(Color.BLUE + "\n[Action] Importation de " + fileImport + "..." + Color.RESET);
                    produitService.importerProduitsFournisseur(fileImport);
                    break;

                case "2":
                    String fileCmd = demanderFichier(scanner, "Fichier de commande à traiter", "Commandes.xml");
                    System.out.println(Color.BLUE + "\n[Action] Traitement de " + fileCmd + "..." + Color.RESET);
                    commandeService.traiterCommande(fileCmd);
                    break;

                case "3":
                    String fileExport = demanderFichier(scanner, "Nom du fichier d'archive", "archive.xml");
                    commandeService.exporterCommandes(fileExport);
                    break;

                case "4":
                    System.out.println(Color.BLUE + "\n[Action] Lecture des commandes en base de données..." + Color.RESET);
                    commandeService.afficherCommandesBDD();
                    break;

                case "0":
                    System.out.println(Color.GREEN + "Fermeture de l'application. Au revoir !" + Color.RESET);
                    quitter = true;
                    break;

                default:
                    System.out.println(Color.RED + "Choix invalide, veuillez recommencer." + Color.RESET);
            }
        }
        scanner.close();
    }

    /**
     * Demande un nom de fichier à l'utilisateur.
     * @param message
     * @param defaut
     * @return Le nom du fichier choisi
     */
    private static String demanderFichier(Scanner scanner, String message, String defaut) {
        System.out.print(Color.BLUE + message + Color.RESET + " [" + Color.YELLOW + defaut + Color.RESET + "] : ");
        String saisie = scanner.nextLine().trim();
        return saisie.isEmpty() ? defaut : saisie;
    }
}
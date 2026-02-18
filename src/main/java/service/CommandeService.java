package service;

import dao.*;
import main.Color;
import model.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class CommandeService {
    private static CommandeService instance;

    private ClientDAO clientDAO = ClientDAO.getInstance();
    private ProduitDAO produitDAO = ProduitDAO.getInstance();
    private CommandeDAO commandeDAO = CommandeDAO.getInstance();

    private CommandeService() {}

    public void traiterCommande(String xmlPath) {
        try {
            // dtd
            SAXBuilder builder = new SAXBuilder(XMLReaders.DTDVALIDATING);
            Document doc = builder.build(new File(xmlPath));
            Element root = doc.getRootElement();

            // client
            Element clientElem = root.getChild("client");
            String email = clientElem.getChildText("email").replaceAll("\\s+", "");
            String nomClient = clientElem.getChildText("nom-client").trim();
            String villeClient = clientElem.getChildText("ville").trim();

            Client client = clientDAO.findByEmail(email);

            if (client == null) {
                client = new Client(nomClient, email, villeClient);
                int id = clientDAO.insert(client);
                client.setId(id);
            }

            // valide
            List<Element> produitsXML = root.getChild("produits").getChildren("produit");
            boolean commandeValide = true;
            float totalCommande = 0;

            for (Element prodElem : produitsXML) {
                String nom = prodElem.getChildText("nom");
                int qteDemandee = Integer.parseInt(prodElem.getChildText("quantité"));

                Produit p = produitDAO.findByName(nom);

                if (p == null || qteDemandee <= 0 || qteDemandee > p.getStock()) {
                    if (p == null)
                        System.err.println("ERREUR : Produit " + nom + " invalide.");
                    else
                        System.err.println("ERREUR : Produit " + nom + " stock insuffisant.");
                    commandeValide = false;
                    break; // stop
                }
                totalCommande += p.getPrix() * qteDemandee;
            }

            // insérer si ok
            if (commandeValide) {
                Commande cmd = new Commande(client.getId(), root.getChildText("date"), totalCommande);

                int idCmd = commandeDAO.insertCommande(cmd);

                for (Element prodElem : produitsXML) {
                    Produit p = produitDAO.findByName(prodElem.getChildText("nom"));
                    int qte = Integer.parseInt(prodElem.getChildText("quantité"));

                    // Insertion Ligne_Commande
                    LigneCommande ligne = new LigneCommande(idCmd, p.getId(), qte, p.getPrix());
                    commandeDAO.insertLigne(ligne);

                    produitDAO.updateStock(p.getId(), qte);
                }
                System.out.println("Succès : Commande insérée et stocks mis à jour.");
            } else {
                System.err.println("ÉCHEC : La commande ne sera pas insérée.");
            }

        } catch (Exception e) {
            System.err.println("Erreur technique : " + e.getMessage());
        }
    }

    /**
     * @param filename
     */
    public void exporterCommandes(String filename) {
        try {
            Element root = new Element("commandes");
            Document doc = new Document(root);

            List<Element> commandes = commandeDAO.getCommandesPourExport();
            root.addContent(commandes);

            XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
            xmlOutput.output(doc, new FileWriter(filename));

            System.out.println("Export réussi dans le fichier : " + filename);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'export : " + e.getMessage());
        }
    }

    /**
     * Affiche toutes les commandes présentes en base de données avec leurs détails.
     */
    public void afficherCommandesBDD() {
        try {
            System.out.println("\n" + Color.CYAN + Color.BOLD + "--- LISTE DES COMMANDES EN BASE ---" + Color.RESET);

            List<Element> listeCommandes = commandeDAO.getCommandesPourExport();

            if (listeCommandes.isEmpty()) {
                System.out.println(Color.YELLOW + "Aucune commande trouvée en base de données." + Color.RESET);
                return;
            }

            for (Element cmd : listeCommandes) {
                String id = cmd.getAttributeValue("id");
                String date = cmd.getChildText("date");
                String clientNom = cmd.getChildText("nom-client");
                String total = cmd.getChildText("total");

                System.out.println("" + Color.BLUE + Color.BOLD + "Commande #" + id + Color.RESET +
                        " du " + date + " | Client: " + Color.YELLOW + clientNom + Color.RESET +
                        " | Total: " + Color.GREEN + total + "€" + Color.RESET);

                // Les produits de cette commande
                List<Element> produits = cmd.getChild("produits").getChildren("produit");
                for (Element p : produits) {
                    System.out.println("   -> " + p.getChildText("nom") +
                            " (Qté: " + p.getChildText("quantité") +
                            ", Prix unit: " + p.getChildText("prix") + "€)");
                }
                System.out.println("------------------------------------");
            }
        } catch (Exception e) {
            System.err.println(Color.RED + "Erreur lors de l'affichage des commandes : " + e.getMessage() + Color.RESET);
        }
    }

    /**
     * @return L'instance du Singleton
     */
    public static CommandeService getInstance() {
        if (instance == null)
            instance = new CommandeService();
        return instance;
    }
}
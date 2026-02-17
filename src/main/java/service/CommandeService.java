package service;

import dao.*;
import model.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import java.io.File;
import java.util.List;

public class CommandeService {
    private static CommandeService instance;

    private ClientDAO clientDAO = new ClientDAO();
    private ProduitDAO produitDAO = new ProduitDAO();
    private CommandeDAO commandeDAO = new CommandeDAO();

    private CommandeService() {}

    public void traiterCommande(String xmlPath) {
        try {
            // dtd
            SAXBuilder builder = new SAXBuilder(XMLReaders.DTDVALIDATING);
            Document doc = builder.build(new File(xmlPath));
            Element root = doc.getRootElement();

            // client
            Element clientElem = root.getChild("client");
            String email = clientElem.getChildText("email").trim();
            Client client = clientDAO.findByEmail(email);

            if (client == null) {
                client = new Client(clientElem.getChildText("nom-client"), email, clientElem.getChildText("ville"));
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
                    System.err.println("ERREUR : Produit " + nom + " invalide ou stock insuffisant.");
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

    public static CommandeService getInstance() {
        if (instance == null)
            instance = new CommandeService();
        return instance;
    }
}
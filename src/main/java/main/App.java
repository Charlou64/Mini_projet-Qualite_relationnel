package main;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;

import java.io.File;

public class App 
{
    public static void main( String[] args )
    {
        SAXBuilder builder = new SAXBuilder(XMLReaders.DTDVALIDATING);

        try {
            Document doc = builder.build(new File("Commandes.xml"));
            System.out.println("Le fichier commande.xml est valide !");
        } catch (Exception e) {
            System.err.println("Erreur de validation : " + e.getMessage());
        }
    }
}

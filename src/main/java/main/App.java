package main;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import service.CommandeService;

import java.io.File;

public class App 
{
    public static void main( String[] args )
    {
        CommandeService service = CommandeService.getInstance();

        service.traiterCommande("Commandes.xml");
    }
}

package main;

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

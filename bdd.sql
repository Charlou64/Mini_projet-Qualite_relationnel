CREATE DATABASE IF NOT EXISTS gestion_xml;
USE gestion_xml;

-- Table Produits (Partie 1)
CREATE TABLE Produits (
      id_produit INT AUTO_INCREMENT PRIMARY KEY,
      nom VARCHAR(100) NOT NULL,
      prix DOUBLE NOT NULL, -- Prix de vente (2x prix fournisseur) [cite: 17]
      quantite_stock INT NOT NULL -- Utile pour la validation [cite: 26]
);

-- Table Clients (Partie 2)
CREATE TABLE Clients (
     id_client INT AUTO_INCREMENT PRIMARY KEY,
     nom_client VARCHAR(100) NOT NULL,
     email VARCHAR(150) UNIQUE NOT NULL, -- Email unique pour la recherche [cite: 24]
     ville VARCHAR(100)
);

-- Table Commandes (Partie 2 & 3)
CREATE TABLE Commandes (
       id_commande INT AUTO_INCREMENT PRIMARY KEY,
       id_client INT NOT NULL,
       date_commande VARCHAR(20), -- Format "25-5-2022" du XML [cite: 86]
       total_commande DOUBLE DEFAULT 0, -- Pour faciliter l'export [cite: 115]
       CONSTRAINT fk_client FOREIGN KEY (id_client) REFERENCES Clients(id_client)
);

-- Table Lignes_Commande (Partie 2)
-- Chaque ligne correspond à un produit commandé [cite: 25]
CREATE TABLE Lignes_Commande (
     id_commande INT NOT NULL,
     id_produit INT NOT NULL,
     quantite_commandee INT NOT NULL,
     prix_unitaire_facture DOUBLE NOT NULL, -- Le prix au moment de l'achat
     PRIMARY KEY (id_commande, id_produit),
     CONSTRAINT fk_commande FOREIGN KEY (id_commande) REFERENCES Commandes(id_commande),
     CONSTRAINT fk_produit FOREIGN KEY (id_produit) REFERENCES Produits(id_produit)
);
# Mini-Projet : Système de Gestion de Commandes XML

Ce projet Java permet d'automatiser l'intégration et l'exportation de données commerciales en utilisant des fichiers XML comme interface et une base de données MySQL pour le stockage permanent.

## 🛠 Technologies utilisées
* **Java 17+**
* **JDOM2** : Pour la lecture, la validation (DTD) et la génération de fichiers XML.
* **JDBC** : Pour la communication avec la base de données MySQL.
* **MySQL / XAMPP** : Système de gestion de base de données.
* **Maven** : Gestion des dépendances et du build.

## 📂 Architecture du Projet (Pattern DAO)
Le projet suit une architecture en couches pour séparer la logique métier de l'accès aux données :

* **`model`** : Contient les POJO (Client, Produit, Commande, LigneCommande).
* **`dao`** : Gère les interactions SQL (Connexion, Requêtes CRUD).
* **`service`** : Contient la logique métier et le parsing JDOM2 (Singleton).
* **`App`** : Point d'entrée de l'application.

## 🗄️ Structure de la Base de Données
Le script SQL crée quatre tables interconnectées :
1.  **Produits** : Stocke le catalogue et les quantités disponibles.
2.  **Clients** : Liste des clients avec contrainte d'unicité sur l'email.
3.  **Commandes** : En-tête de la transaction (date, client, total).
4.  **Lignes_Commande** : Détail technique de chaque produit acheté (quantité, prix appliqué).

## 🚀 Fonctionnalités principales

### Partie 1 : Importation du Catalogue
Lecture du fichier `produits.xml` et insertion en base de données.
* **Règle métier** : Le prix de vente en base est égal au double du prix fournisseur indiqué dans le XML.

### Partie 2 : Traitement des Commandes
Lecture et validation du fichier `commande.xml` par rapport à `commande.dtd`.
* **Vérification Client** : Si l'email n'existe pas, le client est créé. Sinon, on récupère le client existant.
* **Validation des Stocks** : La commande est rejetée si la quantité demandée est $\le 0$ ou supérieure au stock disponible en base.
* **Mises à jour** : Si la commande est valide, les stocks sont automatiquement décrémentés.

### Partie 3 : Exportation d'Archive
Extraction des données de la base pour générer un fichier `Export_commandes.xml` regroupant toutes les commandes passées, incluant les attributs calculés (total, nombre de produits).

## ⚙️ Installation et Configuration

1.  **Base de données** : Importer le script SQL fourni dans votre instance MySQL (XAMPP).
2.  **Configuration JDBC** : Vérifier les identifiants dans `src/main/java/dao/DBConnection.java`.
3.  **Dépendances** :
    ```bash
    mvn clean install
    ```
4.  **Exécution** : Lancer la classe `App.java`.

## 📝 Auteurs
* Charly Antoine && Victor Paris Lemperrière
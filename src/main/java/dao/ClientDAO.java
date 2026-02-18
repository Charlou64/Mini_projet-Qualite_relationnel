package dao;

import model.Client;
import java.sql.*;

public class ClientDAO {

    private static ClientDAO instance;

    private ClientDAO() {}

    /**
     * @return Le Singleton
     */
    public static ClientDAO getInstance() {
        if (instance == null)
            instance = new ClientDAO();
        return instance;
    }

    /**
     * @param email
     * @return le client qui correspond a l'email
     * @throws SQLException
     */
    public Client findByEmail(String email) throws SQLException {
        String query = "SELECT * FROM Clients WHERE email = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Client c = new Client(rs.getString("nom_client"), rs.getString("email"), rs.getString("ville"));
                c.setId(rs.getInt("id_client"));
                return c;
            }
        }
        return null;
    }

    /**
     * @param client
     * @return l'id du client
     * @throws SQLException
     */
    public int insert(Client client) throws SQLException {
        String query = "INSERT INTO Clients (nom_client, email, ville) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, client.getNom());
            ps.setString(2, client.getEmail());
            ps.setString(3, client.getVille());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }


}
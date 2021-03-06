package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import modelo.Client;
import modelo.Zone;
import persistence.Conexion;

public class ClientDAO extends Mapper {

	private static ClientDAO instancia;
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	Calendar cal = Calendar.getInstance();

	private ClientDAO() {

	}

	public static ClientDAO getInstancia() {
		if (instancia == null)
			instancia = new ClientDAO();
		return instancia;
	}

	// devuelvo todos los clientes guardados en la base de datos
	public ArrayList<Client> getClients() {
		ArrayList<Client> clientes = new ArrayList<Client>();

		try {

			Connection con = Conexion.connect();
			Client cli = null;

			PreparedStatement ps = con.prepareStatement("SELECT * FROM " + super.getDatabase() + ".dbo.client");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				cli = new Client(rs.getInt("id"), rs.getString("name"), rs.getString("homeAddress"),
						this.getZoneById(con, rs.getInt("zone_code")), rs.getString("phone"), rs.getString("mail"), rs.getString("dni"));

				clientes.add(cli);

			}

			con.close();
		}

		catch (SQLException e) {
			e.printStackTrace();
		}
		return clientes;
	}

	// busca un cliente determinado en la base de datos solo con el id y lo devuelvo
	public Client getClient(int id) {
		Client cli = null;
		try {
			Connection con = Conexion.connect();
			PreparedStatement ps = con
					.prepareStatement("SELECT * FROM " + super.getDatabase() + ".dbo.client WHERE id=?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				cli = new Client(rs.getInt("id"), rs.getString("name"), rs.getString("homeAddress"),
						this.getZoneById(con, rs.getInt("zone_code")), rs.getString("phone"), rs.getString("mail"),
						rs.getString("dni"));
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cli;
	}

	private Zone getZoneById(Connection con, int id) {
		int zoneCode = 0;
		String zoneName = null;
		try {
			PreparedStatement ps4 = con
					.prepareStatement("SELECT TOP 1 * FROM " + super.getDatabase() + ".dbo.Zone where id=?");
			ps4.setInt(1, id);
			ResultSet rs = ps4.executeQuery();

			while (rs.next()) {

				zoneCode = rs.getInt("zone_code");
				zoneName = rs.getString("zone_name");

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new Zone(zoneCode, zoneName);

	}

}

package turneros.BerkeleyDb.repository;


import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import turneros.BerkeleyDb.connection.SqlLiteConnection;
import turneros.Common.entidades.Reserva;
import turneros.Common.entidades.Servicio;
import turneros.Common.entidades.ServicioTaquilla;
import turneros.Common.entidades.Taquilla;
import turneros.Common.entidades.Turno;
import turneros.configuration.Configuration;


public class ServiciosRepository {
	private Dao<Servicio, Integer> servicioDao;
	
	public ServiciosRepository(String ruta) {
		JdbcConnectionSource con;
		try {
			con = SqlLiteConnection.getConnection(ruta);
			servicioDao = DaoManager.createDao(con,Servicio.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Servicio> listarServiciosDisponibles() throws SQLException {
		String sql ="select s.codigoServicio,s.nombre,s.label "
				+ " from servicio s";
		GenericRawResults<Servicio> servicioList =
		servicioDao.queryRaw(sql,
			new RawRowMapper<Servicio>() {
	            public Servicio mapRow(String[] columnNames,
	              String[] resultColumns) {
	            	Servicio servicio = new Servicio();
	            	servicio.setCodigoServicio(Integer.parseInt(resultColumns[0]));
	            	servicio.setNombre(resultColumns[1]);
		            return servicio;
	            }
		});
		return servicioList.getResults();
	}
	
	
}

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


public class ReservaRepository {
	private Dao<Servicio, Integer> servicioDao;
	private Dao<Reserva, Integer> reservaDao;
	private Dao<Taquilla, Integer> taquillaDao;
	private Dao<ServicioTaquilla, Integer> servicioTaquillaDao;
	
	public ReservaRepository(String ruta) {
		JdbcConnectionSource con;
		try {
			con = SqlLiteConnection.getConnection(ruta);
			servicioDao = DaoManager.createDao(con,Servicio.class);
			reservaDao = DaoManager.createDao(con,Reserva.class);
			servicioTaquillaDao = DaoManager.createDao(con,ServicioTaquilla.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public Reserva guardarReserva(Reserva reserva) throws SQLException {
		Reserva res = reservaDao.queryForId(reserva.getCodigoReserva());
		if(res!=null) {
			reservaDao.update(reserva);
		} else {
			reservaDao.create(reserva);
		}
		System.out.println("Se guarda la reserva del servicio:"+reserva.getCodigoServicio()+ " con estado:"+ reserva.getEstado());
		return reserva;
	}
	
	public Servicio obtenerServicio(Servicio servicio) throws SQLException {
		Servicio serv = servicioDao.queryForId(servicio.getCodigoServicio());
		return serv;
	}
	
	public void guardarServicio(Servicio servicio) throws SQLException {
		Servicio serv = this.obtenerServicio(servicio);
		if(serv!=null) {
			servicioDao.update(servicio);
		} else {
			servicioDao.create(servicio);
		}
	}
	
	public Reserva buscarReservaDisponibleTaquilla(Taquilla taquilla) throws SQLException {
		String sql ="select r.codigoReserva,r.turno,r.codigoServicio,r.estado,sr.nombre  "
				+ " from reserva r "+
				" join servicioTaquilla s on r.codigoServicio = s.codigoServicio "+
				" join servicio sr on sr.codigoServicio = s.codigoServicio "+
				" where r.estado = 'disponible' and s.codigoTaquilla ="+taquilla.getCodigoTaquilla();
		GenericRawResults<Reserva> reservaList =
		reservaDao.queryRaw(sql,
			new RawRowMapper<Reserva>() {
	            public Reserva mapRow(String[] columnNames,
	              String[] resultColumns) {
	            	Reserva reserva = new Reserva();
		            reserva.setCodigoReserva(Integer.parseInt(resultColumns[0]));
		            reserva.setTurno(resultColumns[1]);
		            reserva.setCodigoServicio(Integer.parseInt(resultColumns[2]));
		            reserva.setEstado(resultColumns[3]);
		            reserva.setServicio(resultColumns[4]);
		            return reserva;
	            }
		});
		List<Reserva> listaReservas = reservaList.getResults();
		if(listaReservas.size() > 0) {
			return listaReservas.get(0);
		}
		return null;
	}
	
	public Reserva obtenerInfoReserva(Reserva reserva) throws SQLException {
		String sql ="select r.codigoReserva,r.turno,r.codigoServicio,r.estado,sr.nombre  "
				+ " from reserva r "+
				" join servicioTaquilla s on r.codigoServicio = s.codigoServicio "+
				" join servicio sr on sr.codigoServicio = s.codigoServicio "+
				" where r.estado = 'disponible' and s.codigoReserva ="+reserva.getCodigoReserva();
		GenericRawResults<Reserva> reservaList =
		reservaDao.queryRaw(sql,
			new RawRowMapper<Reserva>() {
	            public Reserva mapRow(String[] columnNames,
	              String[] resultColumns) {
	            	Reserva reserva = new Reserva();
		            reserva.setCodigoReserva(Integer.parseInt(resultColumns[0]));
		            reserva.setTurno(resultColumns[1]);
		            reserva.setCodigoServicio(Integer.parseInt(resultColumns[2]));
		            reserva.setEstado(resultColumns[3]);
		            reserva.setServicio(resultColumns[4]);
		            return reserva;
	            }
		});
		List<Reserva> listaReservas = reservaList.getResults();
		if(listaReservas.size() > 0) {
			return listaReservas.get(0);
		}
		return null;
	}
	
	
}

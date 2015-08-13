package turneros.BerkeleyDb.connection;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.jdbc.JdbcConnectionSource;

public class SqlLiteConnection {
	private static JdbcConnectionSource connection = null;

	public static JdbcConnectionSource getConnection(String ruta) throws SQLException {
		if (connection == null) {
			// Thread Safe. Might be costly operation in some case
			synchronized (JdbcConnectionSource.class) {
				if (connection == null) {
					connection = new JdbcConnectionSource(ruta);
				}
			}
		}
		return connection;
	}
}

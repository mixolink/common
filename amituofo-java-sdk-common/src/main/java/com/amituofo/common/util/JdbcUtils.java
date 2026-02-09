package com.amituofo.common.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtils {

//	public static Connection getH2Connection(String dblocation) throws ModelException {
//		try {
//			Connection conn = DriverManager.getConnection("jdbc:h2:" + dblocation, "sa", "");
//			conn.setAutoCommit(false);
//			return conn;
//		} catch (Exception e) {
//			e.printStackTrace();
//
//			// try {
//			// final Logger log = LogUtils.persistenceLogger;
//			// log.error("Failed to open database " + dblocation + ", trying to recreate. ");
//			// File dbfile = new File(dblocation);
//			// if (dbfile.exists()) {
//			// File errdb = new File(dblocation + ".errdb");
//			// errdb.delete();
//			// if (!dbfile.renameTo(errdb)) {
//			// dbfile.delete();
//			// }
//			// }
//			//
//			// Connection conn = DriverManager.getConnection("jdbc:h2:" + dblocation, "sa", "");
//			// log.error("Database " + dblocation + " recreated");
//			// return conn;
//			// } catch (Exception e1) {
//			// e1.printStackTrace();
//			// throw new PersistencerException(e1);
//			// }
//			throw new ModelException(e);
//		}
//	}

	public static void commit(Connection conn, Statement stat) {
		try {
			if (stat != null) {
				stat.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.commit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			// unlock();
		}
	}

	public static void close(Statement stat) {
		try {
			if (stat != null) {
				stat.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// } finally {
			// unlock();
		}
	}

	public static void close(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// } finally {
			// unlock();
		}
	}
	

	public static void close(ResultSet result, Statement stat) {
		if (result != null)
			try {
				result.close();
			} catch (SQLException e) {
			}
		if (stat != null)
			try {
				stat.close();
			} catch (SQLException e) {
			}
	}

}

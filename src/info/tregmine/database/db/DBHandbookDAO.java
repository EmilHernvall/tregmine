package info.tregmine.database.db;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import info.tregmine.database.DAOException;
import info.tregmine.database.IHandbookDAO;

public class DBHandbookDAO implements IHandbookDAO{
	Connection c;
	public DBHandbookDAO(Connection connection){
		this.c = connection;
	}
	@Override
	public List<String[]> getHandbook() throws DAOException {
		String sql = "SELECT * FROM staff_handbook";
		/*
		 * Table Name: staff_handbook
		 * rulenum VARCHAR 4 NOT NULL NO AI
		 * rule VARCHAR 128 NOT NULL NO AI
		 */
		try (PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.execute();
            List<String[]> handbook = new ArrayList<String[]>();
            try (ResultSet rs = stmt.getResultSet()) {
            	while(rs.next()){
                    String[] rule = new String[2];
                    rule[0] = rs.getString("rulenum");
                    rule[1] = rs.getString("rule");
                    handbook.add(rule);
                }
            	return handbook;
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
	}

}

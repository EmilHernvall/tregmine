package info.tregmine.database;

import java.util.List;

public interface IHandbookDAO{
	public List<String[]> getHandbook() throws DAOException;
}

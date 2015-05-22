package assign3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * Table model for Metropolis viewer table view. This class uses list of list of compile time Object to store table values.
 * For every "Add" or "Search" operation, this model class will access the underlining database to update or
 * retrieve data. The 2-d table stored locally is just used to hold the database query result or newly added
 * entry.
 * 
 * @author yotta2
 *
 */
public final class MetropolisTableModel extends AbstractTableModel {

	public static final String[] POPULATION_CRITERIA = {"Population larger than", "Population smaller than", "Population equal to"};
	private static final String[] POPULATION_COMPARE_CONDITION_FMTS = {"population > %d", "population < %d", "population = %d",};
	public static final String[] MATCH_TYPES = {"Exact Match", "Partial Match"};
	private static final String[] MATCH_CONDITION_FMTS = {"%s = \"%s\"", "%s LIKE \"%%%s%%\""};
	private static final String[] COL_NAMES = {"Metropolis", "Continent", "Population"};
	private static final String ADD_DATA_CMD_FMT = "INSERT INTO metropolises VALUES(\"%s\",\"%s\",%d)"; // should make table configurable, but we only have one table here
	private static final String DB_URL_FMT = "jdbc:mysql://%s";
	private static final String USE_DB_CMD_FMT = "USE %s";
	private static final String SELECT_CMD_ALL = "SELECT * FROM metropolises";
	private static final String SELECT_CMD_RESTRICTED = "SELECT * FROM metropolises WHERE ";

	/**
	 * Construct and initialize local 2-d table. This local 2-d table is just used to hold the database query result or newly added entry.
	 */
	public MetropolisTableModel() {
		data = new ArrayList<List>();
	}

	/**
	 * Returns the number of columns in the model. A JTable uses this method to determine how many columns it should create and display by default.
	 * @return the number of columns in the model
	 */
	@Override
	public int getColumnCount() {
		return COL_NAMES.length;
	}

	/**
	 * Returns the number of rows in the model. A JTable uses this method to determine how many rows it should display. This method should be quick, as it is called frequently during rendering.
	 * @return the number of rows in the model
	 */
	@Override
	public int getRowCount() {
		return data.size();
	}

	/**
	 * Returns the value for the cell at columnIndex and rowIndex.
	 * @param rowIndex the row whose value is to be queried
	 * @param columnIndex the column whose value is to be queried
	 * @return the value Object at the specified cell
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex).get(columnIndex);
	}

	/**
	 * Returns the name of the column at columnIndex. This is used to initialize the table's column header name. Note: this name does not need to be unique; two columns in a table can have the same name.
	 * @param columnIndex the index of the column
	 * @return the name of the column
	 */
	@Override
	public String getColumnName(int columnIndex) {
		return COL_NAMES[columnIndex];
	}

	/**
	 * Add a row into metropolis table in database, and reset the central table to display the newly added entry only 
	 * @param metropolis name of the newly added metropolis
	 * @param continent continent of the newly added metropolis
	 * @param population population of the newly added metropolis
	 */
	public void addData(String metropolis, String continent, Integer population) {
		String addCmd = String.format(ADD_DATA_CMD_FMT, metropolis, continent, population);
		System.out.println("Add command: " + addCmd);
		updateDB(addCmd);
		data.clear();
		data.add(makeRow(metropolis, continent, population));
		fireTableDataChanged();
	}

	/**
	 * Search all metropolis records which satisfy the given criteria. And fire table data changed event so that registered table view
	 * will update themselves to show the query result.
	 * @param metropolis name of the searching metropolis, could be partial or exact
	 * @param continent continent of the searching metropolis, could be partial or exact
	 * @param populationTxt population text in the search textview of the searching metropolis
	 * @param populationCriterionIndex index of the population criteria pulldown combo box
	 * @param matchTypeIndex index of the match type criteria pulldown combo box
	 */
	public void search(String metropolis, String continent, String populationTxt, int populationCriterionIndex, int matchTypeIndex) {
		int population = populationTxt.isEmpty() ? 0 : Integer.valueOf(populationTxt);
		String queryCmd;
		if (metropolis.isEmpty() && continent.isEmpty() && populationTxt.isEmpty()) {
			queryCmd = SELECT_CMD_ALL;
		} else {
			queryCmd = SELECT_CMD_RESTRICTED;
			if (!metropolis.isEmpty()) {
				queryCmd += " " + String.format(MATCH_CONDITION_FMTS[matchTypeIndex], COL_NAMES[0], metropolis);
				if (!continent.isEmpty() || !populationTxt.isEmpty())
					queryCmd += " AND ";
			}
			if (!continent.isEmpty()) {
				queryCmd += " " + String.format(MATCH_CONDITION_FMTS[matchTypeIndex], COL_NAMES[1], continent);
				if (!populationTxt.isEmpty())
					queryCmd += " AND ";
			}
			if (population != 0) {
				queryCmd += " " + String.format(POPULATION_COMPARE_CONDITION_FMTS[populationCriterionIndex], population);
			}
		}
		System.out.println("Query command: " + queryCmd);
		queryDB(queryCmd);
		fireTableDataChanged();
	}

	/**
	 * Connect to and query database, for a given sql command.
	 * @param sqlCmd SQL command
	 */
	private void queryDB(String sqlCmd) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection conn = DriverManager.getConnection(String.format(DB_URL_FMT, MyDBInfo.MYSQL_DATABASE_SERVER), MyDBInfo.MYSQL_USERNAME, MyDBInfo.MYSQL_PASSWORD);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(String.format(USE_DB_CMD_FMT, MyDBInfo.MYSQL_DATABASE_NAME));
			ResultSet rs = stmt.executeQuery(sqlCmd);
			updateData(rs);
			conn.close();
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
			System.exit(1);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Update local 2-d table data using result set.
	 * @param rs A table of data representing a database result set
	 */
	private void updateData(ResultSet rs) {
		data.clear();
		try {
			while (rs.next()) {
					String metropolis = rs.getString(COL_NAMES[0]);
					String continent = rs.getString(COL_NAMES[1]);
					int population = rs.getInt(COL_NAMES[2]);
					data.add(makeRow(metropolis, continent, population));
				}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Connect to and update database, using the given sql command.
	 * @param sqlCmd SQL command
	 */
	private void updateDB(String sqlCmd) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			Connection conn = DriverManager.getConnection(String.format(DB_URL_FMT, MyDBInfo.MYSQL_DATABASE_SERVER), MyDBInfo.MYSQL_USERNAME, MyDBInfo.MYSQL_PASSWORD);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(String.format(USE_DB_CMD_FMT, MyDBInfo.MYSQL_DATABASE_NAME));
			stmt.executeUpdate(sqlCmd);
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Construct a list of compile-time objects, given an array of arbitrary amount of complie-time objects.
	 * @param elems an array of arbitrary amount of complie-time objects
	 * @return A list of compile-time objects
	 */
	private List<Object> makeRow(Object... elems) {
		List<Object> row = new ArrayList<Object>();
		for (Object elem : elems)
			row.add(elem);

		return row;
	}

	private List<List> data; // table data, one list for each row, this will be updated for every add or search operation
}

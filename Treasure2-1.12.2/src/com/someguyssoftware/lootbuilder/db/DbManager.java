/**
 * 
 */
package com.someguyssoftware.lootbuilder.db;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.RunScript;
import org.h2.tools.Server;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.someguyssoftware.lootbuilder.exception.DatabaseInitializationException;
import com.someguyssoftware.lootbuilder.model.LootContainer;
import com.someguyssoftware.lootbuilder.model.LootContainerHasGroup;
import com.someguyssoftware.lootbuilder.model.LootGroupHasItem;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;

/**
 * 
 * @author Mark Gottschling on Jan 18, 2018
 *
 */
public class DbManager {
	// logger
	public static Logger logger = LogManager.getLogger(Treasure.instance.getName());

	private static DbManager instance;
	// JPA
	private Connection connection;
	private Server server;
	
	/*
	 * In order to use ORM Lite, you must use a proprietary Connection object, hence there will be two connections to use the
	 * treasure H2 database.
	 */
	// ORM Lite
	private JdbcConnectionSource connSource;
	private Dao<LootContainer, String> containerDao; 
	private Dao<LootContainerHasGroup, String> containerGroupDao;
	private Dao<LootGroupHasItem, String> groupItemDao;
	
	/**
	 * TODO this creates a physical db file on the file system. only need to construct if the db doesn't exist.
	 * Private constructor
	 */
	private DbManager(TreasureConfig config) throws ClassNotFoundException, SQLException {
		logger.info("Initializing DbManager...");
		Connection conn = null;
		JdbcConnectionSource connSource = null;

		// load the driver class
		Class.forName("org.h2.Driver");

		// start h2 tcp server to allow connections from 3rd party clients while game is running.
		Server server = Server.createTcpServer().start();
		setServer(server);
		// TODO need to shut the server when mod exists

		// connect to the db via TCP
		//		Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/mem:plans;USER=sa;PASSWORD=sa;DB_CLOSE_DELAY=-1;");
		//		conn = DriverManager.getConnection("jdbc:h2:mem:plans;DB_CLOSE_DELAY=-1");

		// TODO - need absolute path to Treasure.MODID
		// get the path to the default style sheet
		Path treasureDbPath = Paths.get(TreasureConfig.treasureFolder, "treasure").toAbsolutePath();
		// create the connection
		String databaseUrl = "jdbc:h2:tcp://localhost:9092/" +treasureDbPath.toString() + ";USER=sa;PASSWORD=sa;";
		conn = DriverManager.getConnection(databaseUrl);

		if (conn == null) {
			logger.warn("Unable to connect JPA to h2 treasure database.");
			return;
		}

		// set the connection
		setConnection(conn);

		// TODO only need to run the scripts if the db is not found.
		// look for file --> treasure.mv.db
		Path dbFilePath = Paths.get(treasureDbPath.toString() + ".mv.db");
		boolean pathExists =
		        Files.exists(dbFilePath,
		            new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
		
		if (!pathExists) {
			// execute the script
			logger.info("Creating tables...");
			try {
				// open a stream to the sql file
				InputStream is = getClass().getResourceAsStream("/resources/treasure.sql");
				// TODO expand of this error and throw error
				if (is == null) System.out.println("Inputstream is null.");
				Reader reader = new InputStreamReader(is);
				// run the script (creates and populates tables)
				RunScript.execute(conn, reader);
	
				//		Statement stat = conn.createStatement();
				//		stat.execute("runscript from 'classpath:/com/someguyssoftware/lootbuilder/db/treasure.sql'");
				//		stat.execute("runscript from 'classpath:/treasure.sql'");
			}
			catch(SQLException e) {
				logger.error("Error running sql script:", e);
			}
		}
		
		// close the JPA connection
		getConnection().close();
		
		/*
		 * open the ORM Lite connection and setup the daos
		 */
		connSource = new JdbcConnectionSource(databaseUrl);
		setConnSource(connSource);
		
		// setup the daos;
		containerDao = DaoManager.createDao(connSource, LootContainer.class);
		containerGroupDao = DaoManager.createDao(connSource, LootContainerHasGroup.class);
		groupItemDao = DaoManager.createDao(connSource, LootGroupHasItem.class);
		
		logger.info("...Complete.");
	}

	/**
	 * @param config
	 */
	public static void start(TreasureConfig config) throws DatabaseInitializationException {		
		if (instance == null) {
			logger.debug("Creating new instance of DbManager");
			try {
				instance = new DbManager(config);
			} catch (ClassNotFoundException | SQLException e) {
				throw new DatabaseInitializationException("Unable to create an instance of DbManager.");
			}			
		}		
	}

	/**
	 * 
	 */
	public static void shutdown() {
		getInstance().getConnSource().closeQuietly();
		getInstance().getServer().shutdown();
	}

	/**
	 * 
	 * @param rarity
	 * @return
	 */
	public List<LootContainer> getContainersByRarity(Rarity rarity) {
		
		// query for all accounts that have that password
		List<LootContainer> list = null;
		try {
//			list = containerDao.queryForAll();
			list = containerDao.queryBuilder().where()
			         .eq("rarity", rarity.ordinal())
			         .query();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	public List<LootContainerHasGroup> getGroupsByContainer(Integer id) {
		// inner join to get groups
		List<LootContainerHasGroup> containerGroups = null;
		try {
			containerGroups = containerGroupDao.queryBuilder()
					.selectColumns(LootContainerHasGroup.GROUP_ID_FIELD_NAME, "group_weight", "min_items", "max_items", "ordering")
					.where()
					.eq(LootContainerHasGroup.CONTAINER_ID_FIELD_NAME, id)
					.query();
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}		
		return containerGroups;
	}
	
	/**
	 * 
	 * @param cg
	 * @return
	 */
	public List<LootGroupHasItem> getItemsByContainer(LootContainerHasGroup cg) {
		// inner join to get groups
		List<LootGroupHasItem> groupItems = null;
		try {
			QueryBuilder<LootContainerHasGroup, String> qb = containerGroupDao.queryBuilder();
			
			qb.selectColumns(LootContainerHasGroup.GROUP_ID_FIELD_NAME)
			.where()
			.eq(LootContainerHasGroup.CONTAINER_ID_FIELD_NAME, cg);

			// outer join to groups-items
			QueryBuilder<LootGroupHasItem, String> qb2 = groupItemDao.queryBuilder();
			groupItems = qb2
					.selectColumns(LootGroupHasItem.ITEM_ID_FIELD_NAME)
					.where()
					.in(LootGroupHasItem.GROUP_ID_FIELD_NAME, qb)
					.query();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}		
		return groupItems;
	}
	
	/**
	 * 
	 * @param cg
	 * @return
	 */
	public List<LootGroupHasItem> getItemsByGroup(LootContainerHasGroup cg) {
		// inner join to get groups
		List<LootGroupHasItem> groupItems = null;
		try {

			// outer join to groups-items
			QueryBuilder<LootGroupHasItem, String> qb2 = groupItemDao.queryBuilder();
			groupItems = qb2
//					.selectColumns(LootGroupHasItem.ITEM_ID_FIELD_NAME)
					.where()
					.eq(LootGroupHasItem.GROUP_ID_FIELD_NAME, cg.getGroup().getId())
					.query();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}		
		return groupItems;
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized DbManager getInstance() {
		return instance;
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(Server server) {
		this.server = server;
	}

	/**
	 * @return the connSource
	 */
	public JdbcConnectionSource getConnSource() {
		return connSource;
	}

	/**
	 * @param connSource the connSource to set
	 */
	public void setConnSource(JdbcConnectionSource connSource) {
		this.connSource = connSource;
	}
}

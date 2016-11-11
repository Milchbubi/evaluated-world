package eWorld.database.impl;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

/**
 * DB for DataBase
 * initializes the database, checks if all tables are correct, creates them if they don't exist
 * closes the connection to the database cluster
 * @author michael
 *
 */
public class DB {

	// static finals
	
	/** keyspace in which users are stored */
	public static final String USER_KEYSPACE = "e_users";
	
	/** keyspace in which the rest is stored */
	public static final String GENERAL_KEYSPACE = "e_general";
	
	/** table in which users are stored */
	public static final String USER_SCHEMA = "users";
	
	/** tables in which entries are stored */
	public static final String ENTRY_REGISTER_SCHEMA = "entries_register";
	public static final String ENTRY_DATA_SCHEMA = "entries_data";
	public static final String ENTRY_COUNTER_SCHEMA = "entries_counter";
	public static final String ENTRY_VOTE_SCHEMA = "entries_vote";
	
	/** tables in which attributes are stored */
	public static final String ATTRIBUTE_DATA_SCHEMA = "attributes_data";
	public static final String ATTRIBUTE_COUNTER_SCHEMA = "attributes_counter";
	public static final String ATTRIBUTE_VOTE_SCHEMA = "attributes_vote";
	
	/** tables in which attributeValues are stored */
	public static final String ATTRIBUTE_TOP_VALUE_SCHEMA = "attribute_top_values_data";
	public static final String ATTRIBUTE_VALUE_DATA_SCHEMA = "attribute_values_data";
	public static final String ATTRIBUTE_VALUE_COUNTER_SCHEMA = "attribute_values_counter";
	public static final String ATTRIBUTE_VALUE_VOTE_SCHEMA = "attribute_values_vote";
	
	/** tables in which media like images are stored */
	public static final String MEDIUM_DATA_SCHEMA = "media_data";
	public static final String MEDIUM_COUNTER_SCHEMA = "media_counter";
	public static final String MEDIUM_VOTE_SCHEMA = "media_vote";
	
	/** tables in which criteria are stored */
	public static final String CRITERION_DATA_SCHEMA = "criteria_data";
	public static final String CRITERION_COUNTER_SCHEMA = "criteria_counter";
	public static final String CRITERION_VOTE_SCHEMA = "criteria_vote";
	
	/** tables in which criteriaValues are stored */
	public static final String CRITERION_VALUE_DATA_SCHEMA = "criterion_values_data";
	public static final String CRITERION_VALUE_COUNTER_SCHEMA = "criterion_values_counter";
	public static final String CRITERION_VALUE_VOTE_SCHEMA = "criterion_values_vote";
	
	/** tables in which comments are stored */
	public static final String COMMENT_DATA_SCHEMA = "comments_data";
	public static final String COMMENT_COUNTER_SCHEMA = "comments_counter";
	public static final String COMMENT_VOTE_SCHEMA = "comments_vote";
	
	
	// final attributes
	
	/** IP-address of the connected node */
	private final String nodeAddress;
	
	/** connected cluster */
	private final Cluster cluster;
	
	/** session to access (only!) user_keyspace */
	private final Session user_keyspace_session;
	
	/** session to access (only!) general_keyspace */
	private final Session general_keyspace_session;
	
	// methods
	
	/**
	 * initializes the database, checks if keyspaces exist, creates them if not
	 * @param nodeAddress IP-address of the node to connect to
	 */
	public DB(final String nodeAddress) {
		assert null != nodeAddress;
		
		this.nodeAddress = nodeAddress;
		
		// connect & print out clusterMetadata
		cluster = Cluster.builder()
				.addContactPoint(nodeAddress)
				.build();
		Metadata metadata = cluster.getMetadata();
		System.out.printf("Connected to cluster: %s\n", 
				metadata.getClusterName());
		for ( Host host : metadata.getAllHosts() ) {
			System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
					host.getDatacenter(), host.getAddress(), host.getRack());
		}
		
		user_keyspace_session = cluster.connect();
		init_user_keyspace();
		
		general_keyspace_session = cluster.connect();
		init_general_keyspace();
	}
	
	/**
	 * initializes the user_keyspace
	 */
	private void init_user_keyspace() {
		assert null != user_keyspace_session;
		
		user_keyspace_session.execute("CREATE KEYSPACE IF NOT EXISTS " +
				USER_KEYSPACE +
				" WITH replication = {'class':'SimpleStrategy', 'replication_factor':5};");
		
		System.out.println("success: Keyspace '" + USER_KEYSPACE + "' initialized");	// TODO-note vary if created or already existed
		
		user_keyspace_session.execute("USE " + USER_KEYSPACE);	// keyspace of this session should never be changed
	}
	
	/**
	 * initializes the general_keyspace
	 */
	private void init_general_keyspace() {
		assert null != general_keyspace_session;
		
		general_keyspace_session.execute("CREATE KEYSPACE IF NOT EXISTS " +
				GENERAL_KEYSPACE +
				" WITH replication = {'class':'SimpleStrategy', 'replication_factor':3};");
		
		System.out.println("success: Keyspace '" + GENERAL_KEYSPACE + "' initialized");	// TODO-note vary if created or already existed
		
		general_keyspace_session.execute("USE " + GENERAL_KEYSPACE);	// keyspace of this session should never be changed
	}
	
	/**
	 * closes the connection to the database cluster
	 */
	public void close() {
		assert null != cluster;
		
		cluster.close();
		
		System.out.println("success: connection to database cluster closed");
	}
	
	/**
	 * @return session to access (only!) user_keyspace
	 */
	public Session getUserKeyspaceSession() {
		assert null != user_keyspace_session;
		
		return user_keyspace_session;
	}
	
	/**
	 * @return session to access (only!) general_keyspace
	 */
	public Session getGeneralKeyspaceSession() {
		assert null != general_keyspace_session;
		
		return general_keyspace_session;
	}
}

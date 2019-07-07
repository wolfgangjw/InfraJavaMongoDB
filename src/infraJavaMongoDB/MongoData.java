package infraJavaMongoDB;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public abstract class MongoData {
	private static final Map<Type, MongoDataMeta> Meta;
	static {
		Meta = new HashMap<Type, MongoDataMeta>();
	}

	private static MongoDatabase MongoDatabase;

	protected static void SetMongoDatabase(String host, int port, String database) {
		if (MongoDatabase != null) {
			return;
		}
		@SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient(host, port);
		MongoDatabase = mongoClient.getDatabase(database);
	}

	protected MongoData() {
		this(null);
	}

	protected MongoData(String involved) {
		Involved = involved;
	}

	private String Involved;

	public static <T extends MongoData> void Create(T data) {

	}

	public void Create() {
		Create(this);
	}
}

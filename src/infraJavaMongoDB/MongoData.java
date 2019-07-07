package infraJavaMongoDB;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public abstract class MongoData {
	private static final Map<Class<?>, MongoDataMeta> Meta;
	static {
		Meta = new HashMap<Class<?>, MongoDataMeta>();
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

	protected String Involved;

	public static <T extends MongoData> void Create(T data) throws Exception {
		MongoDataMeta mdm = Meta.get(data.getClass());
		Set<Field> involvedKeyFieldSet = mdm.GetInvolvedKeyFieldMap().get(data.Involved);
		Map<Field, String> fieldColumnMap = mdm.GetFieldColumnMap();
		Document document = new Document();
		for (Iterator<Field> iterator = involvedKeyFieldSet.iterator(); iterator.hasNext();) {
			Field field = iterator.next();
			document.append(fieldColumnMap.get(field), field.get(data));
		}
		MongoDatabase.getCollection(mdm.GetCollectionName()).insertOne(document);
	}

	public void Create() throws Exception {
		Create(this);
	}
}

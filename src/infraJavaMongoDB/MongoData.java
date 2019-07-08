package infraJavaMongoDB;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public abstract class MongoData {
	private static final Map<Class, MongoDataMeta> Meta;
	static {
		Meta = new HashMap<Class, MongoDataMeta>();
	}

	private static MongoDatabase MongoDatabase;

	protected static void Initialize(String host, int port, String database, Class c) throws Exception {
		if (MongoDatabase != null) {
			return;
		}
		@SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient(host, port);
		MongoDatabase = mongoClient.getDatabase(database);
		Meta.put(c, new MongoDataMeta(c));
	}

	protected MongoData(boolean isChangable) {
		this(null, isChangable);
	}

	protected MongoData(String involved, boolean isChangable) {
		Involved = involved;
		IsChangable = isChangable;
	}

	protected String Involved;
	protected boolean IsChangable;

	public static <T extends MongoData> void Create(T data) throws Exception {
		MongoDataMeta mdm = Meta.get(data.getClass());
		Set<Field> involvedKeyFieldSet = mdm.GetInvolvedKeyFieldMap().get(data.Involved);
		Map<Field, String> fieldColumnMap = mdm.GetFieldColumnMap();
		Document document = new Document();
		for (Iterator<Field> iterator = involvedKeyFieldSet.iterator(); iterator.hasNext();) {
			Field field = iterator.next();
			document.append(fieldColumnMap.get(field), ((MongoDataField<?>) field.get(data)).Get());
		}
		MongoDatabase.getCollection(mdm.GetCollectionName()).insertOne(document);
	}

	public void Create() throws Exception {
		Create(this);
	}
}

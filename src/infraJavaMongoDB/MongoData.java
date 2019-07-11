package infraJavaMongoDB;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public abstract class MongoData {
	private static final Map<Class<?>, MongoDataMeta> Meta;
	static {
		Meta = new HashMap<Class<?>, MongoDataMeta>();
	}

	private static MongoDatabase MongoDatabase;

	public static void Initialize(String host, int port, String database) {
		if (MongoDatabase != null) {
			return;
		}
		@SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient(host, port);
		MongoDatabase = mongoClient.getDatabase(database);
	}

	protected static void Initialize(Class<?> c) {
		if (Meta.containsKey(c)) {
			return;
		}
		try {
			Meta.put(c, new MongoDataMeta(c));
		} catch (Exception e) {
			// TODO: log and consider if the server should be stop.
		}
	}

	protected MongoData(boolean isChangable) {
		this("", isChangable);
	}

	protected MongoData(String involved, boolean isChangable) {
		involved = involved.trim();
		Involved = involved.isEmpty() ? null : involved.toUpperCase();
		IsChangable = isChangable;
	}

	protected boolean IsInvolvedField(String involved, String fieldName) {
		involved = involved == null ? null : involved.trim().toUpperCase();
		MongoDataMeta mdm = Meta.get(this.getClass());
		return mdm.GetInvolvedKeyFieldNameMap().get(involved).contains(fieldName);
	}

	protected ObjectId _id;
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
		data._id = (ObjectId) document.get("_id");
	}

	public void Create() throws Exception {
		Create(this);
	}

	public ObjectId getId() {
		return _id;
	}
}

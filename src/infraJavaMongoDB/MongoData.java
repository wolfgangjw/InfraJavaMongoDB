package infraJavaMongoDB;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public abstract class MongoData {
	private static final Map<Type, MongoDataMeta> Meta;
	static {
		Meta = new HashMap<Type, MongoDataMeta>();
	}

	protected MongoData(String involved) {
		Involved = involved;
	}

	private String Involved;
}

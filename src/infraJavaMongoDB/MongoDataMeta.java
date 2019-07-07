package infraJavaMongoDB;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import infraJavaMongoDB.annotation.MongoDataAnnotation;
import infraJavaMongoDB.annotation.MongoDataFieldAnnotation;

final class MongoDataMeta {
	private static final Type MongoDataType;
	static {
		MongoDataType = MongoData.class;
	}

	private final String CollectionName;
	private final Type Type;

	private final Map<String, Set<Field>> InvolvedKeyFieldMap;
	private final Map<Field, String> FieldColumnMap;

	MongoDataMeta(Type type) throws Exception {
		Class<?> c = type.getClass();
		if (!c.isAssignableFrom(MongoDataType.getClass())) {
			throw new Exception(String.format("The class %s is not assignable from %s", type.getTypeName(),
					MongoDataType.getTypeName()));
		}
		CollectionName = c.getAnnotation(MongoDataAnnotation.class).CollectionName();
		Type = type;
		InvolvedKeyFieldMap = new HashMap<String, Set<Field>>();
		FieldColumnMap = new HashMap<Field, String>();
		SetMeta();
	}

	protected String GetCollectionName() {
		return CollectionName;
	}

	protected Map<String, Set<Field>> GetInvolvedKeyFieldMap() {
		return InvolvedKeyFieldMap;
	}

	protected Map<Field, String> GetFieldColumnMap() {
		return FieldColumnMap;
	}

	private void SetMeta() {
		Field[] fields = Type.getClass().getFields();
		for (int i = 0; i < fields.length; i++) {
			AddFieldMeta(fields[i]);
		}
	}

	private void AddFieldMeta(Field field) {
		Class<?> c = field.getClass();
		if (c != MongoDataField.class) {
			return;
		}
		String column = c.getAnnotation(MongoDataFieldAnnotation.class).ColumnName();
		String[] involved = c.getAnnotation(MongoDataFieldAnnotation.class).Involved().split(",");
		AddFieldIntoInvolvedKeyFieldMap(field, null);
		for (int i = 0; i < involved.length; i++) {
			String key = involved[i].trim().toUpperCase();
			if (key.isEmpty()) {
				continue;
			}
		}
		FieldColumnMap.put(field, column);
	}

	private void AddFieldIntoInvolvedKeyFieldMap(Field field, String key) {
		if (!InvolvedKeyFieldMap.containsKey(key)) {
			InvolvedKeyFieldMap.put(key, new HashSet<Field>());
		}
		InvolvedKeyFieldMap.get(key).add(field);
	}
}

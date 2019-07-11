package infraJavaMongoDB;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import infraJavaMongoDB.annotation.MongoDataAnnotation;
import infraJavaMongoDB.annotation.MongoDataFieldAnnotation;

final class MongoDataMeta {
	private static final Class<?> MongoDataType;
	static {
		MongoDataType = MongoData.class;
	}

	private final String CollectionName;
	private final Class<?> ThisClass;

	private final Map<String, Set<Field>> InvolvedKeyFieldMap;
	private final Map<String, Set<String>> InvolvedKeyFieldNameMap;
	private final Map<Field, String> FieldColumnMap;

	MongoDataMeta(Class<?> c) throws Exception {
		if (!MongoDataType.isAssignableFrom(c)) {
			throw new Exception(String.format("The class %s is not assignable from %s", c.getTypeName(),
					MongoDataType.getTypeName()));
		}
		ThisClass = c;
		CollectionName = c.getAnnotation(MongoDataAnnotation.class).CollectionName();
		InvolvedKeyFieldMap = new HashMap<String, Set<Field>>();
		InvolvedKeyFieldNameMap = new HashMap<String, Set<String>>();
		FieldColumnMap = new HashMap<Field, String>();
		SetMeta();
	}

	protected String GetCollectionName() {
		return CollectionName;
	}

	protected Map<String, Set<Field>> GetInvolvedKeyFieldMap() {
		return InvolvedKeyFieldMap;
	}

	protected Map<String, Set<String>> GetInvolvedKeyFieldNameMap() {
		return InvolvedKeyFieldNameMap;
	}

	protected Map<Field, String> GetFieldColumnMap() {
		return FieldColumnMap;
	}

	private void SetMeta() {
		Field[] fields = ThisClass.getFields();
		for (int i = 0; i < fields.length; i++) {
			AddFieldMeta(fields[i]);
		}
	}

	private void AddFieldMeta(Field field) {
		Class<?> c = field.getType();
		if (c != MongoDataField.class) {
			return;
		}
		String column = field.getAnnotation(MongoDataFieldAnnotation.class).ColumnName();
		String[] involved = field.getAnnotation(MongoDataFieldAnnotation.class).Involved().split(",");
		AddFieldIntoInvolvedKeyFieldMap(field, null);
		for (int i = 0; i < involved.length; i++) {
			String key = involved[i].trim().toUpperCase();
			if (key.isEmpty()) {
				continue;
			}
			AddFieldIntoInvolvedKeyFieldMap(field, key);
		}
		FieldColumnMap.put(field, column);
	}

	private void AddFieldIntoInvolvedKeyFieldMap(Field field, String key) {
		if (!InvolvedKeyFieldMap.containsKey(key)) {
			InvolvedKeyFieldMap.put(key, new HashSet<Field>());
			InvolvedKeyFieldNameMap.put(key, new HashSet<String>());
		}
		InvolvedKeyFieldMap.get(key).add(field);
		InvolvedKeyFieldNameMap.get(key).add(field.getName());
	}
}

package infraJavaMongoDB;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import infraJavaMongoDB.annotation.MongoDataAnnotation;
import infraJavaMongoDB.annotation.MongoDataFieldAnnotation;

final class MongoDataMeta {
	private static final Type MongoDataType;
	static {
		MongoDataType = MongoData.class;
	}

	private final String CollectionName;
	private final Type Type;

	MongoDataMeta(Type type) throws Exception {
		Class<?> c = type.getClass();
		if (!c.isAssignableFrom(MongoDataType.getClass())) {
			throw new Exception(String.format("The class %s is not assignable from %s", type.getTypeName(),
					MongoDataType.getTypeName()));
		}
		CollectionName = c.getAnnotation(MongoDataAnnotation.class).CollectionName();
		Type = type;
		SetMeta();
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
		String fieldName = c.getAnnotation(MongoDataFieldAnnotation.class).FieldName();
		String[] involved = c.getAnnotation(MongoDataFieldAnnotation.class).Involved().split(",");
		for (int i = 0; i < involved.length; i++) {

		}
	}

	public String getCollectionName() {
		return CollectionName;
	}
}

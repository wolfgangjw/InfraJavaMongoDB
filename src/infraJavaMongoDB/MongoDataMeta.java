package infraJavaMongoDB;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import infraJavaMongoDB.annotation.MongoDataAnnotation;

final class MongoDataMeta {
	private static final Type MongoDataType;
	static {
		MongoDataType = MongoData.class;
	}
	final String CollectionName;
	private final Type Type;

	MongoDataMeta(Type type) throws Exception {
		if (!type.getClass().isAssignableFrom(MongoDataType.getClass())) {
			throw new Exception(String.format("The class %s is not assignable from %s", type.getTypeName(),
					MongoDataType.getTypeName()));
		}
		CollectionName = type.getClass().getAnnotation(MongoDataAnnotation.class).CollectionName();
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
	}
}

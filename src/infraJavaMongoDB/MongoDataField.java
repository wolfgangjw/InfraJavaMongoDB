package infraJavaMongoDB;

public final class MongoDataField<T> {
	private T T;
	private boolean IsChanged;
	private boolean IsChangable;

	public MongoDataField(T t, boolean isChangable) {
		T = t;
		IsChangable = isChangable;
		IsChanged = false;
	}

	public T Get() {
		return T;
	}

	public void Set(T t) throws Exception {
		if (!IsChangable) {
			throw new Exception("The field is not changable.");
		}
		T = t;
		IsChanged = true;
	}

	public boolean IsChanged() {
		return IsChanged;
	}
}

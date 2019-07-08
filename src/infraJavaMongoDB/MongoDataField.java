package infraJavaMongoDB;

public final class MongoDataField<T> {
	private T T;
	private Boolean IsChanged;
	private boolean IsChangable;

	public MongoDataField(boolean isChangable) {
		IsChangable = isChangable;
	}

	public MongoDataField(T t, boolean isChangable) {
		T = t;
		IsChangable = isChangable;
		IsChanged = false;
	}

	public T Get() {
		return T;
	}

	public void Set(T t) throws Exception {
		if (!IsChangable && IsChanged != null) {
			throw new Exception("The field is not changable.");
		}
		T = t;
		IsChanged = IsChanged == null ? false : true;
	}

	public boolean IsChanged() {
		return IsChanged;
	}
}

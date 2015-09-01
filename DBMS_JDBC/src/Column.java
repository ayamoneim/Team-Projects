

public class Column {
	private String name, dataType;
	private int size;

	public Column(String name, String dataType, int size) {
		this.name = name;
		this.dataType = dataType;
		this.size = size;
	}

	public String getColName() {
		return name;
	}

	public String getdataType() {
		return dataType;
	}

	public int getSize() {
		return size;
	}
}

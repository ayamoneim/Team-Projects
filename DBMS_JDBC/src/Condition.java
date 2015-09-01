

public class Condition {
	private String name, operator, value;

	public Condition(String name, String operator, String value) {
		this.name = name;
		this.operator = operator;
		this.value = value;
	}

	public String getColName() {
		return name;
	}

	public String getOperator() {
		return operator;
	}

	public String getValue() {
		return value;
	}
}

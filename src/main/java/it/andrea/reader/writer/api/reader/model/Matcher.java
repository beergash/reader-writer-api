package it.andrea.reader.writer.api.reader.model;

/**
 * 
 * @author Andrea Aresta
 *
 */
public class Matcher {

	private String position;
	private MatchingOperator operator;
	private String targetValue;

	public Matcher(String position, MatchingOperator operator, String targetValue) {
		super();
		this.position = position;
		this.operator = operator;
		this.targetValue = targetValue;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public MatchingOperator getOperator() {
		return operator;
	}

	public void setOperator(MatchingOperator operator) {
		this.operator = operator;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

}

package farto.cleva.guilherme.totvs.vo;

import java.io.Serializable;

public class PestVO implements Serializable {

	private String id;
	private String name;
	private int weight;

	public PestVO() {
		super();
	}

	public PestVO(String id, String name, int weight) {
		super();
		this.id = id;
		this.name = name;
		this.weight = weight;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

}

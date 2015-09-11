package farto.cleva.guilherme.totvs.vo;

import java.io.Serializable;

public class EmployeeVO implements Serializable {

	private String id;
	private String name;
	private String lastname;

	public EmployeeVO() {
		super();
	}

	public EmployeeVO(String id, String name, String lastname) {
		super();
		this.id = id;
		this.name = name;
		this.lastname = lastname;
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

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

}

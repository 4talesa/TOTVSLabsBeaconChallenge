package farto.cleva.guilherme.totvs.vo;

import java.io.Serializable;

public class EmployeeRegistrationVO implements Serializable {

	private String id;
	private String employeeid;
	private String farmofficeid;
	private String farmid;
	private String date;

	public EmployeeRegistrationVO() {
		super();
	}

	public EmployeeRegistrationVO(String id, String employeeid, String farmofficeid, String farmid, String date) {
		super();
		this.id = id;
		this.employeeid = employeeid;
		this.farmofficeid = farmofficeid;
		this.farmid = farmid;
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmployeeid() {
		return employeeid;
	}

	public void setEmployeeid(String employeeid) {
		this.employeeid = employeeid;
	}

	public String getFarmofficeid() {
		return farmofficeid;
	}

	public void setFarmofficeid(String farmofficeid) {
		this.farmofficeid = farmofficeid;
	}

	public String getFarmid() {
		return farmid;
	}

	public void setFarmid(String farmid) {
		this.farmid = farmid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}

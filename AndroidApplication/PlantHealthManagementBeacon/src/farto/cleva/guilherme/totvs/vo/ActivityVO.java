package farto.cleva.guilherme.totvs.vo;

import java.io.Serializable;

public class ActivityVO implements Serializable {

	private String employeeid;
	private String farmid;

	public ActivityVO() {
		super();
	}

	public ActivityVO(String employeeid, String farmid) {
		super();
		this.employeeid = employeeid;
		this.farmid = farmid;
	}

	public String getEmployeeid() {
		return employeeid;
	}

	public void setEmployeeid(String employeeid) {
		this.employeeid = employeeid;
	}

	public String getFarmid() {
		return farmid;
	}

	public void setFarmid(String farmid) {
		this.farmid = farmid;
	}

}

package farto.cleva.guilherme.totvs.vo;

import java.io.Serializable;
import java.util.Map;
import farto.cleva.guilherme.totvs.vo.framework.GenericVO;

public class PhytosanitaryInspectionVO extends GenericVO implements Serializable {

	private String id;
	private String employeeid;
	private String farmid;
	private String date;
	private String latitude;
	private String longitude;
	private int phytosanitaryPunctuation;
	private Map<String, Boolean> pests;
	private Map<String, Boolean> diseases;

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

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public int getPhytosanitaryPunctuation() {
		return phytosanitaryPunctuation;
	}

	public void setPhytosanitaryPunctuation(int phytosanitaryPunctuation) {
		this.phytosanitaryPunctuation = phytosanitaryPunctuation;
	}

	public Map<String, Boolean> getPests() {
		return pests;
	}

	public void setPests(Map<String, Boolean> pests) {
		this.pests = pests;
	}

	public Map<String, Boolean> getDiseases() {
		return diseases;
	}

	public void setDiseases(Map<String, Boolean> diseases) {
		this.diseases = diseases;
	}

}

package farto.cleva.guilherme.totvs.vo;

import java.io.Serializable;

public class FarmOfficeVO implements Serializable {

	private String id;
	private String beaconuuid;
	private String beaconminormajor;

	public FarmOfficeVO() {
		super();
	}

	public FarmOfficeVO(String id, String beaconuuid, String beaconminormajor) {
		super();
		this.id = id;
		this.beaconuuid = beaconuuid;
		this.beaconminormajor = beaconminormajor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBeaconuuid() {
		return beaconuuid;
	}

	public void setBeaconuuid(String beaconuuid) {
		this.beaconuuid = beaconuuid;
	}

	public String getBeaconminormajor() {
		return beaconminormajor;
	}

	public void setBeaconminormajor(String beaconminormajor) {
		this.beaconminormajor = beaconminormajor;
	}

}

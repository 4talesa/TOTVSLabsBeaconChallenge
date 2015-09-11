package farto.cleva.guilherme.totvs.vo;

import java.io.Serializable;

public class FarmVO implements Serializable {

	private String id;
	private String beaconuuid;
	private String beaconminormajor;
	private String name;
	private String owner;
	private String thumbnailid;

	public FarmVO() {
		super();
	}

	public FarmVO(String id, String beaconuuid, String beaconminormajor, String name, String owner, String thumbnailid) {
		super();
		this.id = id;
		this.beaconuuid = beaconuuid;
		this.beaconminormajor = beaconminormajor;
		this.name = name;
		this.owner = owner;
		this.thumbnailid = thumbnailid;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getThumbnailid() {
		return thumbnailid;
	}

	public void setThumbnailid(String thumbnailid) {
		this.thumbnailid = thumbnailid;
	}

}

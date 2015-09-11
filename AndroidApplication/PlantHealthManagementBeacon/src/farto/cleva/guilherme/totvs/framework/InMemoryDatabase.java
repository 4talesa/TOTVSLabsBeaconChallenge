package farto.cleva.guilherme.totvs.framework;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import farto.cleva.guilherme.totvs.vo.ActivityVO;
import farto.cleva.guilherme.totvs.vo.DiseaseVO;
import farto.cleva.guilherme.totvs.vo.EmployeeVO;
import farto.cleva.guilherme.totvs.vo.FarmOfficeVO;
import farto.cleva.guilherme.totvs.vo.FarmVO;
import farto.cleva.guilherme.totvs.vo.PestVO;
import farto.cleva.guilherme.totvs.vo.PhytosanitaryInspectionVO;

public class InMemoryDatabase implements Serializable {

	private EmployeeVO employee = null;
	private ActivityVO activity = null;
	private FarmOfficeVO farmOffice = null;
	private FarmVO farm = null;
	private List<PhytosanitaryInspectionVO> phytosanitaryInspections = null;
	private List<FarmOfficeVO> farmsOffices = null;
	private List<FarmVO> farms = null;
	private List<PestVO> pests = null;
	private List<DiseaseVO> diseases = null;

	private Map<String, String> mqttSensorsValues = null;

	private static InMemoryDatabase instance = null;

	public static InMemoryDatabase getInstance() {
		if (instance == null) {
			instance = new InMemoryDatabase();
		}

		return instance;
	}

	private InMemoryDatabase() {
		super();

		this.farmsOffices = new LinkedList<FarmOfficeVO>();
		this.farms = new LinkedList<FarmVO>();
		this.pests = new LinkedList<PestVO>();
		this.diseases = new LinkedList<DiseaseVO>();
	}

	public boolean updateEmployee(EmployeeVO employee) {
		this.employee = employee;

		return true;
	}

	public EmployeeVO getEmployee() {
		return employee;
	}

	public boolean updateActivy(ActivityVO activity) {
		this.activity = activity;

		return true;
	}

	public ActivityVO getActivity() {
		return activity;
	}

	public boolean hasActivity() {
		return InMemoryDatabase.getInstance().getActivity() != null;
	}

	public boolean checkFarmFromActivity(FarmVO farm) {
		if (hasActivity()) {
			return InMemoryDatabase.getInstance().getActivity().equals(farm);
		}

		return false;
	}

	public boolean updateFarmOffice(FarmOfficeVO farmOffice) {
		if (this.farmOffice == null || !this.farmOffice.getId().equalsIgnoreCase(farmOffice.getId())) {
			this.farmOffice = farmOffice;
		}

		return true;
	}

	public FarmOfficeVO getFarmOffice() {
		return farmOffice;
	}

	public boolean updateFarm(FarmVO farm) {
		if (this.farm == null || !this.farm.getId().equalsIgnoreCase(farm.getId())) {
			this.farm = farm;
		}

		return true;
	}

	public FarmVO getFarm() {
		return farm;
	}

	public List<PhytosanitaryInspectionVO> getPhytosanitaryInspections() {
		if (phytosanitaryInspections == null) {
			phytosanitaryInspections = new LinkedList<PhytosanitaryInspectionVO>();
		}

		return phytosanitaryInspections;
	}

	public void clearPhytosanitaryInspections() {
		getPhytosanitaryInspections().clear();
	}

	public boolean updateFarmsOffices(List<FarmOfficeVO> farmsOffices) {
		this.farmsOffices.clear();
		this.farmsOffices.addAll(farmsOffices);

		return true;
	}

	public boolean updateFarms(List<FarmVO> farms) {
		this.farms.clear();
		this.farms.addAll(farms);

		return true;
	}

	public boolean updatePests(List<PestVO> pests) {
		this.pests.clear();
		this.pests.addAll(pests);

		return true;
	}

	public boolean updateDiseases(List<DiseaseVO> diseases) {
		this.diseases.clear();
		this.diseases.addAll(diseases);

		return true;
	}

	public List<FarmOfficeVO> getFarmsOffices() {
		return farmsOffices;
	}

	public FarmOfficeVO getFarmOfficeById(String id) {
		if (this.farmsOffices != null && !this.farmsOffices.isEmpty()) {
			for (FarmOfficeVO farmOffice : this.farmsOffices) {
				if (id.equalsIgnoreCase(farmOffice.getId())) {
					return farmOffice;
				}
			}
		}

		return null;
	}

	public List<FarmVO> getFarms() {
		return farms;
	}

	public FarmVO getFarmById(String id) {
		if (this.farms != null && !this.farms.isEmpty()) {
			for (FarmVO farm : this.farms) {
				if (id.equalsIgnoreCase(farm.getId())) {
					return farm;
				}
			}
		}

		return null;
	}

	public List<PestVO> getPests() {
		return pests;
	}

	public List<DiseaseVO> getDiseases() {
		return diseases;
	}

	public boolean isDataSynchronized() {
		return employee != null && !farms.isEmpty() && !pests.isEmpty() && !diseases.isEmpty();
	}

	public Map<String, String> getMqttSensorsValues() {
		if (mqttSensorsValues == null) {
			mqttSensorsValues = new LinkedHashMap<String, String>();
		}

		return mqttSensorsValues;
	}

	public void saveMqttSensorValue(String key, String value) {
		getMqttSensorsValues().put(key, value);
	}

	public String getMqttSensorValue(String key, String defaultValue) {
		if (getMqttSensorsValues().containsKey(key)) {
			return getMqttSensorsValues().get(key);
		}

		return defaultValue;
	}

}

package farto.cleva.guilherme.totvs.view;

import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.firebase.client.Firebase;
import farto.cleva.guilherme.totvs.R;
import farto.cleva.guilherme.totvs.dao.ActivityDAO;
import farto.cleva.guilherme.totvs.dao.DiseaseDAO;
import farto.cleva.guilherme.totvs.dao.EmployeeDAO;
import farto.cleva.guilherme.totvs.dao.FarmDAO;
import farto.cleva.guilherme.totvs.dao.FarmOfficeDAO;
import farto.cleva.guilherme.totvs.dao.PestDAO;
import farto.cleva.guilherme.totvs.dao.PhytosanitaryInspectionDAO;
import farto.cleva.guilherme.totvs.dao.framework.GenericDAO;
import farto.cleva.guilherme.totvs.db.PreferencesHelper;
import farto.cleva.guilherme.totvs.framework.InMemoryDatabase;
import farto.cleva.guilherme.totvs.vo.ActivityVO;
import farto.cleva.guilherme.totvs.vo.DiseaseVO;
import farto.cleva.guilherme.totvs.vo.EmployeeVO;
import farto.cleva.guilherme.totvs.vo.FarmOfficeVO;
import farto.cleva.guilherme.totvs.vo.FarmVO;
import farto.cleva.guilherme.totvs.vo.PestVO;
import farto.cleva.guilherme.totvs.vo.PhytosanitaryInspectionVO;

public class DeviceConfigurationActivity extends Activity {

	private PreferencesHelper preferencesHelper = null;

	private EmployeeDAO employeeDAO = new EmployeeDAO();
	private FarmOfficeDAO farmOfficeDAO = new FarmOfficeDAO();
	private FarmDAO farmDAO = new FarmDAO();
	private PestDAO pestDAO = new PestDAO();
	private DiseaseDAO diseaseDAO = new DiseaseDAO();
	private ActivityDAO activityDAO = new ActivityDAO();
	private PhytosanitaryInspectionDAO phytosanitaryInspectionDAO = new PhytosanitaryInspectionDAO();

	private EditText txtEmployeeId = null;
	private EditText txtEmployeeName = null;
	private ImageButton btnCheckEmployee = null;

	private CheckBox ckFarmsOffices = null;
	private CheckBox ckFarms = null;
	private CheckBox ckPests = null;
	private CheckBox ckDiseases = null;
	private CheckBox ckMyActivities = null;
	private CheckBox ckPhytosanitaryInspections = null;
	private Button btnSynchronize = null;

	private TextView lblFarmsOffices = null;
	private TextView lblFarms = null;
	private TextView lblPests = null;
	private TextView lblDiseases = null;
	private TextView lblMyActivities = null;
	private TextView lblPhytosanitaryInspections = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_configuration_view);

		Firebase.setAndroidContext(this);

		preferencesHelper = PreferencesHelper.getInstance(this);

		txtEmployeeId = (EditText) findViewById(R.id.txtEmployeeId);
		txtEmployeeId.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					btnCheckEmployee.performClick();

					return true;
				}

				return false;
			}
		});

		txtEmployeeName = (EditText) findViewById(R.id.txtEmployeeName);
		btnCheckEmployee = (ImageButton) findViewById(R.id.btnCheckEmployee);
		btnCheckEmployee.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				checkEmployee();
			}
		});

		ckFarmsOffices = (CheckBox) findViewById(R.id.ckFarmsOffices);
		ckFarms = (CheckBox) findViewById(R.id.ckFarms);
		ckPests = (CheckBox) findViewById(R.id.ckPests);
		ckDiseases = (CheckBox) findViewById(R.id.ckDiseases);
		ckMyActivities = (CheckBox) findViewById(R.id.ckMyActivities);
		ckMyActivities.setEnabled(false);
		ckPhytosanitaryInspections = (CheckBox) findViewById(R.id.ckPhytosanitaryInspections);
		ckPhytosanitaryInspections.setEnabled(false);
		btnSynchronize = (Button) findViewById(R.id.btnSynchronize);
		btnSynchronize.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				synchronize();
			}
		});

		lblFarmsOffices = (TextView) findViewById(R.id.lblFarmsOffices);
		lblFarms = (TextView) findViewById(R.id.lblFarms);
		lblPests = (TextView) findViewById(R.id.lblPests);
		lblDiseases = (TextView) findViewById(R.id.lblDiseases);
		lblMyActivities = (TextView) findViewById(R.id.lblMyActivities);
		lblPhytosanitaryInspections = (TextView) findViewById(R.id.lblPhytosanitaryInspections);

		lblFarmsOffices.setText("");
		lblFarms.setText("");
		lblPests.setText("");
		lblDiseases.setText("");
		lblMyActivities.setText("");
		lblPhytosanitaryInspections.setText("");

		loadEmployee();

		updatePhytosanitaryInspectionStatus();
	}

	private void loadEmployee() {
		String employeeId = preferencesHelper.getStringValue("employee-id", null);
		String employeeName = preferencesHelper.getStringValue("employee-name", null);

		if (employeeId != null) {
			txtEmployeeId.setText(employeeId);
		}

		if (employeeName != null) {
			txtEmployeeName.setText(employeeName);
		}
	}

	int synchronizedServices = 0;

	private void countSynchronizedService() {
		synchronizedServices++;
	}

	private void checkSynchronizedServices() {
		synchronizedServices--;

		if (synchronizedServices < 0) {
			synchronizedServices = 0;
		}

		btnSynchronize.setEnabled(synchronizedServices == 0);
	}

	private void synchronize() {
		btnSynchronize.setEnabled(false);

		boolean synchronizeFarmsOffices = ckFarmsOffices.isChecked();
		boolean synchronizeFarms = ckFarms.isChecked();
		boolean synchronizePests = ckPests.isChecked();
		boolean synchronizeDiseases = ckDiseases.isChecked();
		boolean synchronizeMyActivities = ckMyActivities.isChecked();
		boolean synchronizePhytosanitaryInspections = ckPhytosanitaryInspections.isChecked();

		if (synchronizeFarmsOffices) {
			countSynchronizedService();

			lblFarmsOffices.setText("");

			farmOfficeDAO.getFarmsOffices(new GenericDAO.OnPostExecute() {

				@Override
				public void execute(Object o) {
					List<FarmOfficeVO> farmsOffices = (List<FarmOfficeVO>) o;

					InMemoryDatabase.getInstance().updateFarmsOffices(farmsOffices);

					lblFarmsOffices.setText(farmsOffices.size() + " " + getResources().getString(R.string.lblSynchronizedRecords));

					checkSynchronizedServices();
				}
			});
		}

		if (synchronizeFarms) {
			countSynchronizedService();

			lblFarms.setText("");

			farmDAO.getFarms(new GenericDAO.OnPostExecute() {

				@Override
				public void execute(Object o) {
					List<FarmVO> farms = (List<FarmVO>) o;

					InMemoryDatabase.getInstance().updateFarms(farms);

					lblFarms.setText(farms.size() + " " + getResources().getString(R.string.lblSynchronizedRecords));

					checkSynchronizedServices();
				}
			});
		}

		if (synchronizePests) {
			countSynchronizedService();

			lblPests.setText("");

			pestDAO.getPests(new GenericDAO.OnPostExecute() {

				@Override
				public void execute(Object o) {
					List<PestVO> pests = (List<PestVO>) o;

					InMemoryDatabase.getInstance().updatePests(pests);

					lblPests.setText(pests.size() + " " + getResources().getString(R.string.lblSynchronizedRecords));

					checkSynchronizedServices();
				}
			});
		}

		if (synchronizeDiseases) {
			countSynchronizedService();

			lblDiseases.setText("");

			diseaseDAO.getDiseases(new GenericDAO.OnPostExecute() {

				@Override
				public void execute(Object o) {
					List<DiseaseVO> diseases = (List<DiseaseVO>) o;

					InMemoryDatabase.getInstance().updateDiseases(diseases);

					lblDiseases.setText(diseases.size() + " " + getResources().getString(R.string.lblSynchronizedRecords));

					checkSynchronizedServices();
				}
			});
		}

		if (synchronizeMyActivities) {
			countSynchronizedService();

			lblMyActivities.setText("");

			activityDAO.getActivity(InMemoryDatabase.getInstance().getEmployee().getId(), new GenericDAO.OnPostExecute() {

				@Override
				public void execute(Object o) {
					ActivityVO activity = (ActivityVO) o;

					InMemoryDatabase.getInstance().updateActivy(activity);

					lblMyActivities.setText(getResources().getString(R.string.lblActivitiesSynchronized));

					checkSynchronizedServices();
				}
			});
		}

		if (synchronizePhytosanitaryInspections) {
			countSynchronizedService();

			lblPhytosanitaryInspections.setText("");

			phytosanitaryInspectionDAO.savePhytosanitaryInspections(InMemoryDatabase.getInstance().getPhytosanitaryInspections(), new GenericDAO.OnPostExecute() {

				@Override
				public void execute(Object o) {
					List<PhytosanitaryInspectionVO> phytosanitaryInspections = InMemoryDatabase.getInstance().getPhytosanitaryInspections();

					lblPhytosanitaryInspections.setText(phytosanitaryInspections.size() + " " + getResources().getString(R.string.lblSynchronizedRecords));

					InMemoryDatabase.getInstance().clearPhytosanitaryInspections();

					checkSynchronizedServices();
				}
			});
		}
	}

	private void updatePhytosanitaryInspectionStatus() {
		ckPhytosanitaryInspections.setEnabled(!InMemoryDatabase.getInstance().getPhytosanitaryInspections().isEmpty());
		ckPhytosanitaryInspections.setChecked(!InMemoryDatabase.getInstance().getPhytosanitaryInspections().isEmpty());

		ckFarmsOffices.setEnabled(InMemoryDatabase.getInstance().getPhytosanitaryInspections().isEmpty());
		ckFarms.setEnabled(InMemoryDatabase.getInstance().getPhytosanitaryInspections().isEmpty());
		ckPests.setEnabled(InMemoryDatabase.getInstance().getPhytosanitaryInspections().isEmpty());
		ckDiseases.setEnabled(InMemoryDatabase.getInstance().getPhytosanitaryInspections().isEmpty());
		ckFarmsOffices.setChecked(InMemoryDatabase.getInstance().getPhytosanitaryInspections().isEmpty());
		ckFarms.setChecked(InMemoryDatabase.getInstance().getPhytosanitaryInspections().isEmpty());
		ckPests.setChecked(InMemoryDatabase.getInstance().getPhytosanitaryInspections().isEmpty());
		ckDiseases.setChecked(InMemoryDatabase.getInstance().getPhytosanitaryInspections().isEmpty());
	}

	private void checkEmployee() {
		txtEmployeeName.setText("");
		ckMyActivities.setEnabled(false);

		String employeeId = txtEmployeeId.getText().toString();

		employeeDAO.getEmployee(employeeId, new GenericDAO.OnPostExecute() {

			@Override
			public void execute(Object o) {
				EmployeeVO employee = (EmployeeVO) o;

				String fullname = employee.getName() + " " + employee.getLastname();

				txtEmployeeName.setText(fullname);

				preferencesHelper.setStringValue("employee-id", employee.getId());
				preferencesHelper.setStringValue("employee-name", fullname);

				InMemoryDatabase.getInstance().updateEmployee(employee);

				ckMyActivities.setEnabled(true);
			}
		});
	}

}

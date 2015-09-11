package farto.cleva.guilherme.totvs.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils.Proximity;
import com.firebase.client.Firebase;
import farto.cleva.guilherme.totvs.R;
import farto.cleva.guilherme.totvs.dao.EmployeeRegistrationDAO;
import farto.cleva.guilherme.totvs.dao.framework.GenericDAO;
import farto.cleva.guilherme.totvs.framework.InMemoryDatabase;
import farto.cleva.guilherme.totvs.framework.beacons.BeaconHelper;
import farto.cleva.guilherme.totvs.vo.EmployeeRegistrationVO;
import farto.cleva.guilherme.totvs.vo.FarmOfficeVO;
import farto.cleva.guilherme.totvs.vo.framework.GenericVO;

public class MainActivity extends Activity {

	private static final DateFormat fmt = new SimpleDateFormat("MMddyyyy HH:mm:ss");

	private EmployeeRegistrationDAO employeeRegistrationDAO = new EmployeeRegistrationDAO();

	private Button btnPhytosanitaryInspection = null;
	private Button btnDeviceConfiguration = null;
	private Button btnInformation = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);

		Firebase.setAndroidContext(this);

		btnPhytosanitaryInspection = (Button) findViewById(R.id.btnPhytosanitaryInspection);
		btnPhytosanitaryInspection.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				displayPhytosanitaryInspection();
			}
		});

		btnDeviceConfiguration = (Button) findViewById(R.id.btnDeviceConfiguration);
		btnDeviceConfiguration.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				displayDeviceConfiguration();
			}
		});

		btnInformation = (Button) findViewById(R.id.btnInformation);
		btnInformation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				displayInformation();
			}
		});

		updatePhytosanitaryInspectionStatus();

		initializeBeaconManager();
	}

	private void initializeBeaconManager() {
		BeaconHelper.getInstance().initialize(this, 30L, Proximity.NEAR, new BeaconHelper.OnBeaconsDiscovered() {

			@Override
			public void execute(Region region, Beacon beacon) {
				FarmOfficeVO selectedFarmOffice = InMemoryDatabase.getInstance().getFarmOfficeById(region.getIdentifier());

				if (selectedFarmOffice != null && (InMemoryDatabase.getInstance().getFarmOffice() == null || !InMemoryDatabase.getInstance().getFarmOffice().equals(selectedFarmOffice))) {
					InMemoryDatabase.getInstance().updateFarmOffice(selectedFarmOffice);

					registrateEmployee(selectedFarmOffice.getId());

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(MainActivity.this, getResources().getString(R.string.lblFarmOfficeDetected), Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		BeaconHelper.getInstance().startRanging(new BeaconHelper.OnConnect() {

			@Override
			public void execute(List<Region> regions) {
				List<FarmOfficeVO> farmsOffices = InMemoryDatabase.getInstance().getFarmsOffices();

				if (farmsOffices != null && !farmsOffices.isEmpty()) {
					for (FarmOfficeVO farmOffice : farmsOffices) {
						Region region = new Region(farmOffice.getId(), farmOffice.getBeaconuuid(), Integer.parseInt(farmOffice.getBeaconminormajor().split(";")[1]), Integer.parseInt(farmOffice.getBeaconminormajor().split(";")[0]));

						regions.add(region);
					}
				}
			}
		});
	}

	@Override
	protected void onRestart() {
		super.onRestart();

		updatePhytosanitaryInspectionStatus();
	}

	@Override
	protected void onPause() {
		BeaconHelper.getInstance().stopRanging();

		super.onPause();
	}

	@Override
	protected void onStop() {
		BeaconHelper.getInstance().stopRanging();

		super.onStop();
	}

	private void displayPhytosanitaryInspection() {
		Intent iPhytosanitaryInspection = new Intent(MainActivity.this, PhytosanitaryInspectionActivity.class);
		startActivity(iPhytosanitaryInspection);
	}

	private void displayDeviceConfiguration() {
		Intent iDeviceConfiguration = new Intent(MainActivity.this, DeviceConfigurationActivity.class);
		startActivity(iDeviceConfiguration);
	}

	private void displayInformation() {
		Intent iInformation = new Intent(MainActivity.this, InformationActivity.class);
		startActivity(iInformation);
	}

	private void updatePhytosanitaryInspectionStatus() {
		btnPhytosanitaryInspection.setEnabled(InMemoryDatabase.getInstance().isDataSynchronized());
	}

	private void registrateEmployee(String farmOfficeId) {
		EmployeeRegistrationVO employeeRegistration = new EmployeeRegistrationVO();
		employeeRegistration.setId(GenericVO.generateUUID());
		employeeRegistration.setEmployeeid(InMemoryDatabase.getInstance().getEmployee().getId());
		employeeRegistration.setFarmofficeid(farmOfficeId);
		employeeRegistration.setFarmid(null);
		employeeRegistration.setDate(fmt.format(new Date()));

		employeeRegistrationDAO.saveEmployeeRegistration(employeeRegistration, new GenericDAO.OnPostExecute() {

			@Override
			public void execute(Object o) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(MainActivity.this, getResources().getString(R.string.lblEmployeeSuccessfullyRegistered), Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

}

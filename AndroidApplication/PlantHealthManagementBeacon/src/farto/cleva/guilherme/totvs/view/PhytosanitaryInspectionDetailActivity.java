package farto.cleva.guilherme.totvs.view;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import farto.cleva.guilherme.totvs.R;
import farto.cleva.guilherme.totvs.framework.InMemoryDatabase;
import farto.cleva.guilherme.totvs.framework.listeners.GNSSListener;
import farto.cleva.guilherme.totvs.framework.services.MQTTService;
import farto.cleva.guilherme.totvs.vo.DiseaseVO;
import farto.cleva.guilherme.totvs.vo.FarmVO;
import farto.cleva.guilherme.totvs.vo.PestVO;
import farto.cleva.guilherme.totvs.vo.PhytosanitaryInspectionVO;
import farto.cleva.guilherme.totvs.vo.framework.GenericVO;

public class PhytosanitaryInspectionDetailActivity extends Activity {

	private static final DateFormat fmt = new SimpleDateFormat("MMddyyyy HH:mm:ss");

	private FarmVO selectedFarm = null;

	private int phytosanitaryPunctuation = 0;

	private Map<String, Boolean> pests = new LinkedHashMap<String, Boolean>();
	private Map<String, Boolean> diseases = new LinkedHashMap<String, Boolean>();

	private TextView lblFarm = null;
	private TextView lblPhytosanitaryPunctuation = null;
	private EditText txtLatitude = null;
	private EditText txtLongitude = null;
	private ImageButton btnRetrieveCoordinates = null;

	private ImageButton btnUvSensor = null;
	private ImageButton btnTemperatureSensor = null;
	private ImageButton btnHumiditySensor = null;

	private LinearLayout lytPests = null;
	private LinearLayout lytDiseases = null;

	private LocationManager locationManager = null;

	private Intent iMQTTService = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phytosanitary_inspection_detail_view);

		lblFarm = (TextView) findViewById(R.id.lblFarm);
		lblPhytosanitaryPunctuation = (TextView) findViewById(R.id.lblPhytosanitaryPunctuation);
		txtLatitude = (EditText) findViewById(R.id.txtLatitude);
		txtLongitude = (EditText) findViewById(R.id.txtLongitude);
		btnRetrieveCoordinates = (ImageButton) findViewById(R.id.btnRetrieveCoordinates);
		btnRetrieveCoordinates.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				retrieveCoordinates();
			}
		});

		btnUvSensor = (ImageButton) findViewById(R.id.btnUvSensor);
		btnTemperatureSensor = (ImageButton) findViewById(R.id.btnTemperatureSensor);
		btnHumiditySensor = (ImageButton) findViewById(R.id.btnHumiditySensor);

		btnUvSensor.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				displayMqttSensorValue("uv");
			}
		});

		btnTemperatureSensor.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				displayMqttSensorValue("temperature");
			}
		});

		btnHumiditySensor.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				displayMqttSensorValue("humidity");
			}
		});

		lytPests = (LinearLayout) findViewById(R.id.lytPests);
		lytDiseases = (LinearLayout) findViewById(R.id.lytDiseases);

		Bundle bundle = getIntent().getExtras();

		if (bundle != null && bundle.containsKey("selectedFarm")) {
			selectedFarm = (FarmVO) bundle.getSerializable("selectedFarm");
		}

		lblFarm.setText(selectedFarm.getName());

		updatePhytosanitaryPunctuation();

		configurePests();
		configureDiseases();
	}

	private void displayMqttSensorValue(String sensorName) {
		String value = InMemoryDatabase.getInstance().getMqttSensorValue(new MessageFormat(MQTTService.SENSOR_URL).format(new Object[] { selectedFarm.getId(), sensorName }), null);

		String messageLabel = "";

		if ("uv".equalsIgnoreCase(sensorName)) {
			messageLabel = getResources().getString(R.string.lblUltraviolet);
		} else if ("temperature".equalsIgnoreCase(sensorName)) {
			messageLabel = getResources().getString(R.string.lblTemperature);
		} else if ("humidity".equalsIgnoreCase(sensorName)) {
			messageLabel = getResources().getString(R.string.lblHumidity);
		}

		if (value != null && !"".equalsIgnoreCase(value)) {
			Toast.makeText(PhytosanitaryInspectionDetailActivity.this, new MessageFormat(messageLabel).format(new Object[] { value }), Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(PhytosanitaryInspectionDetailActivity.this, getResources().getString(R.string.lblNodataSelectedSensor), Toast.LENGTH_LONG).show();
		}
	}

	private void configureMQTT() {
		if (iMQTTService == null) {
			iMQTTService = new Intent(PhytosanitaryInspectionDetailActivity.this, MQTTService.class);
		}

		startService(iMQTTService);
	}

	@Override
	protected void onResume() {
		super.onResume();

		configureMQTT();
	}

	@Override
	protected void onPause() {
		super.onPause();

		stopService(iMQTTService);
	}

	private void configurePests() {
		lytPests.removeAllViewsInLayout();

		pests.clear();

		for (final PestVO pest : InMemoryDatabase.getInstance().getPests()) {
			pests.put(pest.getId(), false);

			CheckBox ckPest = new CheckBox(PhytosanitaryInspectionDetailActivity.this);
			ckPest.setText(pest.getName());
			ckPest.setChecked(false);

			ckPest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
					if (checked) {
						phytosanitaryPunctuation += pest.getWeight();
					} else {
						phytosanitaryPunctuation -= pest.getWeight();
					}

					pests.put(pest.getId(), checked);

					updatePhytosanitaryPunctuation();
				}
			});

			lytPests.addView(ckPest);
		}
	}

	private void configureDiseases() {
		lytDiseases.removeAllViewsInLayout();

		diseases.clear();

		for (final DiseaseVO disease : InMemoryDatabase.getInstance().getDiseases()) {
			diseases.put(disease.getId(), false);

			CheckBox ckDisease = new CheckBox(PhytosanitaryInspectionDetailActivity.this);
			ckDisease.setText(disease.getName());
			ckDisease.setChecked(false);

			ckDisease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
					if (checked) {
						phytosanitaryPunctuation += disease.getWeight();
					} else {
						phytosanitaryPunctuation -= disease.getWeight();
					}

					diseases.put(disease.getId(), checked);

					updatePhytosanitaryPunctuation();
				}
			});

			lytDiseases.addView(ckDisease);
		}
	}

	private void retrieveCoordinates() {
		if (locationManager == null) {
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new GNSSListener(new GNSSListener.OnLocationChanged() {

				@Override
				public void execute(String latitude, String longitude) {
					txtLatitude.setText(latitude);
					txtLongitude.setText(longitude);
				}
			}));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.phytosanitary_inspection_detail_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemSave:
			save();
			break;
		case R.id.itemCancel:
			cancel();
			break;
		}

		return true;
	}

	private void updatePhytosanitaryPunctuation() {
		lblPhytosanitaryPunctuation.setText(Integer.toString(phytosanitaryPunctuation));
	}

	private void save() {
		String latitude = txtLatitude.getText().toString();
		String longitude = txtLongitude.getText().toString();

		if (latitude == null || "".equalsIgnoreCase(latitude)) {
			latitude = "-";
		}

		if (longitude == null || "".equalsIgnoreCase(longitude)) {
			longitude = "-";
		}

		PhytosanitaryInspectionVO phytosanitaryInspection = new PhytosanitaryInspectionVO();
		phytosanitaryInspection.setId(GenericVO.generateUUID());
		phytosanitaryInspection.setDate(fmt.format(new Date()));
		phytosanitaryInspection.setEmployeeid(InMemoryDatabase.getInstance().getEmployee().getId());
		phytosanitaryInspection.setFarmid(selectedFarm.getId());
		phytosanitaryInspection.setLatitude(latitude);
		phytosanitaryInspection.setLongitude(longitude);
		phytosanitaryInspection.setPhytosanitaryPunctuation(phytosanitaryPunctuation);
		phytosanitaryInspection.setPests(pests);
		phytosanitaryInspection.setDiseases(diseases);

		InMemoryDatabase.getInstance().getPhytosanitaryInspections().add(phytosanitaryInspection);

		finish();
	}

	private void cancel() {
		finish();
	}

}

package farto.cleva.guilherme.totvs.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
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
import farto.cleva.guilherme.totvs.view.adapters.FarmsGalleryAdapter;
import farto.cleva.guilherme.totvs.vo.EmployeeRegistrationVO;
import farto.cleva.guilherme.totvs.vo.FarmVO;
import farto.cleva.guilherme.totvs.vo.framework.GenericVO;

public class PhytosanitaryInspectionActivity extends Activity {

	private static final DateFormat fmt = new SimpleDateFormat("MMddyyyy HH:mm:ss");

	private EmployeeRegistrationDAO employeeRegistrationDAO = new EmployeeRegistrationDAO();

	private FarmVO selectedFarm = null;

	private EditText txtFarmId = null;
	private ImageButton btnCheckFarm = null;
	private Gallery farmsGallery = null;
	private TextView farmName = null;
	private Button btnStart = null;
	private CheckBox ckCorrectFarm = null;

	private AlertDialog confirmationDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phytosanitary_inspection_view);

		Firebase.setAndroidContext(this);

		txtFarmId = (EditText) findViewById(R.id.txtFarmId);
		txtFarmId.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					btnCheckFarm.performClick();

					return true;
				}

				return false;
			}
		});

		btnCheckFarm = (ImageButton) findViewById(R.id.btnCheckFarm);
		btnCheckFarm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				checkFarm();
			}
		});

		farmsGallery = (Gallery) findViewById(R.id.farmsGallery);
		farmsGallery.setSpacing(1);
		farmsGallery.setAdapter(new FarmsGalleryAdapter(this, InMemoryDatabase.getInstance().getFarms()));
		farmsGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				selectFarm(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
			}
		});

		farmName = (TextView) findViewById(R.id.lblFarmName);

		btnStart = (Button) findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startPhytosanitaryInspection();
			}
		});

		ckCorrectFarm = (CheckBox) findViewById(R.id.ckCorrectFarm);
		ckCorrectFarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
				btnStart.setEnabled(checked);
			}
		});

		createConfirmationDialog();

		checkFarmByActivity();

		initializeBeaconManager();
	}

	private void createConfirmationDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getResources().getString(R.string.lblConfirmPhytosanitaryInspection));
		builder.setTitle(getResources().getString(R.string.lblAttention));
		builder.setIcon(R.drawable.attention);
		builder.setCancelable(false);
		builder.setPositiveButton(getResources().getString(R.string.lblYes), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				displayPhytosanitaryInspectionDetail();
			}
		});
		builder.setNegativeButton(getResources().getString(R.string.lblCancel), null);

		confirmationDialog = builder.create();
	}

	private void initializeBeaconManager() {
		BeaconHelper.getInstance().initialize(this, 30L, Proximity.NEAR, new BeaconHelper.OnBeaconsDiscovered() {

			@Override
			public void execute(Region region, Beacon beacon) {
				FarmVO selectedFarm = InMemoryDatabase.getInstance().getFarmById(region.getIdentifier());

				if (selectedFarm != null && (InMemoryDatabase.getInstance().getFarm() == null || !InMemoryDatabase.getInstance().getFarm().equals(selectedFarm))) {
					InMemoryDatabase.getInstance().updateFarm(selectedFarm);

					registrateEmployee(selectedFarm.getId());

					setSelectedFarm(selectedFarm.getId());

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(PhytosanitaryInspectionActivity.this, getResources().getString(R.string.lblFarmDetected), Toast.LENGTH_SHORT).show();
						}
					});
				}

				if (InMemoryDatabase.getInstance().hasActivity()) {
					boolean isCorrectFarm = InMemoryDatabase.getInstance().checkFarmFromActivity(selectedFarm);

					btnStart.setEnabled(isCorrectFarm);
					ckCorrectFarm.setChecked(isCorrectFarm);

					if (isCorrectFarm) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(PhytosanitaryInspectionActivity.this, getResources().getString(R.string.lblYouAreCorrectFarm), Toast.LENGTH_SHORT).show();
							}
						});
					} else {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(PhytosanitaryInspectionActivity.this, getResources().getString(R.string.lblYouAreIncorrectFarm), Toast.LENGTH_SHORT).show();
							}
						});
					}
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
				List<FarmVO> farms = InMemoryDatabase.getInstance().getFarms();

				if (farms != null && !farms.isEmpty()) {
					for (FarmVO farm : farms) {
						Region region = new Region(farm.getId(), farm.getBeaconuuid(), Integer.parseInt(farm.getBeaconminormajor().split(";")[1]), Integer.parseInt(farm.getBeaconminormajor().split(";")[0]));

						regions.add(region);
					}
				}
			}
		});
	}

	@Override
	protected void onRestart() {
		super.onRestart();
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

	private void displayPhytosanitaryInspectionDetail() {
		Bundle bundle = new Bundle();
		bundle.putSerializable("selectedFarm", selectedFarm);

		Intent iPhytosanitaryInspectionDetail = new Intent(PhytosanitaryInspectionActivity.this, PhytosanitaryInspectionDetailActivity.class);

		iPhytosanitaryInspectionDetail.putExtras(bundle);

		startActivity(iPhytosanitaryInspectionDetail);
	}

	private void startPhytosanitaryInspection() {
		confirmationDialog.show();
	}

	private void selectFarm(int position) {
		selectedFarm = InMemoryDatabase.getInstance().getFarms().get(position);

		farmName.setText(selectedFarm.getName());

		checkFarmByActivity();
	}

	private void checkFarmByActivity() {
		if (InMemoryDatabase.getInstance().hasActivity()) {
			setSelectedFarm(InMemoryDatabase.getInstance().getActivity().getFarmid());

			txtFarmId.setFocusable(false);
			btnCheckFarm.setEnabled(false);
		}
	}

	private void checkFarm() {
		String farmId = txtFarmId.getText().toString();

		setSelectedFarm(farmId);
	}

	private void setSelectedFarm(String farmId) {
		if (farmId != null && !"".equalsIgnoreCase(farmId)) {
			FarmVO farm = InMemoryDatabase.getInstance().getFarmById(farmId);

			if (farm != null) {
				selectedFarm = farm;

				farmsGallery.setSelection(InMemoryDatabase.getInstance().getFarms().indexOf(farm));
			} else {
				selectedFarm = InMemoryDatabase.getInstance().getFarms().get(0);

				farmsGallery.setSelection(0);
			}
		}
	}

	private void registrateEmployee(String farmId) {
		EmployeeRegistrationVO employeeRegistration = new EmployeeRegistrationVO();
		employeeRegistration.setId(GenericVO.generateUUID());
		employeeRegistration.setEmployeeid(InMemoryDatabase.getInstance().getEmployee().getId());
		employeeRegistration.setFarmofficeid(null);
		employeeRegistration.setFarmid(farmId);
		employeeRegistration.setDate(fmt.format(new Date()));

		employeeRegistrationDAO.saveEmployeeRegistration(employeeRegistration, new GenericDAO.OnPostExecute() {

			@Override
			public void execute(Object o) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(PhytosanitaryInspectionActivity.this, getResources().getString(R.string.lblEmployeeSuccessfullyRegistered), Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

}

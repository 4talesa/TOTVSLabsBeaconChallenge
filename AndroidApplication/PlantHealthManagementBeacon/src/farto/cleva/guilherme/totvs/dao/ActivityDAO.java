package farto.cleva.guilherme.totvs.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import farto.cleva.guilherme.totvs.dao.framework.GenericDAO;
import farto.cleva.guilherme.totvs.db.FirebaseHelper;
import farto.cleva.guilherme.totvs.vo.ActivityVO;

public class ActivityDAO extends GenericDAO {

	private static final DateFormat fmt = new SimpleDateFormat("MMddyyyy");

	private FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();

	private ActivityVO activity = new ActivityVO();

	public ActivityVO getActivity(String employeeId, final OnPostExecute onPostExecute) {
		firebaseHelper.getFirebase().child("activities").child(fmt.format(new Date())).child(employeeId).addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot != null && dataSnapshot.exists()) {
					activity = dataSnapshot.getValue(ActivityVO.class);

					if (onPostExecute != null) {
						onPostExecute.execute(activity);
					}
				}
			}

			@Override
			public void onCancelled(FirebaseError firebaseError) {
			}
		});

		return activity;
	}

}

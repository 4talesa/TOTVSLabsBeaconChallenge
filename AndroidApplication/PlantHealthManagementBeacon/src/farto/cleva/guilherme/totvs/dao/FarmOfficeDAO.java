package farto.cleva.guilherme.totvs.dao;

import java.util.LinkedList;
import java.util.List;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import farto.cleva.guilherme.totvs.dao.framework.GenericDAO;
import farto.cleva.guilherme.totvs.db.FirebaseHelper;
import farto.cleva.guilherme.totvs.vo.FarmOfficeVO;

public class FarmOfficeDAO extends GenericDAO {

	private FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();

	private List<FarmOfficeVO> farmsOffices = new LinkedList<FarmOfficeVO>();

	public void getFarmsOffices(final OnPostExecute onPostExecute) {
		farmsOffices.clear();

		firebaseHelper.getFirebase().child("farms-offices").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot != null && dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
					for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
						FarmOfficeVO farmOffice = snapshot.getValue(FarmOfficeVO.class);

						farmsOffices.add(farmOffice);
					}

					if (onPostExecute != null) {
						onPostExecute.execute(farmsOffices);
					}
				}
			}

			@Override
			public void onCancelled(FirebaseError firebaseError) {
			}
		});
	}

}

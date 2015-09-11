package farto.cleva.guilherme.totvs.dao;

import java.util.LinkedList;
import java.util.List;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import farto.cleva.guilherme.totvs.dao.framework.GenericDAO;
import farto.cleva.guilherme.totvs.db.FirebaseHelper;
import farto.cleva.guilherme.totvs.vo.DiseaseVO;

public class DiseaseDAO extends GenericDAO {

	private FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();

	private List<DiseaseVO> diseases = new LinkedList<DiseaseVO>();

	public void getDiseases(final OnPostExecute onPostExecute) {
		diseases.clear();

		firebaseHelper.getFirebase().child("diseases").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot != null && dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
					for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
						DiseaseVO disease = snapshot.getValue(DiseaseVO.class);

						diseases.add(disease);
					}

					if (onPostExecute != null) {
						onPostExecute.execute(diseases);
					}
				}
			}

			@Override
			public void onCancelled(FirebaseError firebaseError) {
			}
		});
	}

}

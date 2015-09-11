package farto.cleva.guilherme.totvs.dao;

import java.util.LinkedList;
import java.util.List;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import farto.cleva.guilherme.totvs.dao.framework.GenericDAO;
import farto.cleva.guilherme.totvs.db.FirebaseHelper;
import farto.cleva.guilherme.totvs.vo.FarmVO;

public class FarmDAO extends GenericDAO {

	private FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();

	private List<FarmVO> farms = new LinkedList<FarmVO>();

	public void getFarms(final OnPostExecute onPostExecute) {
		farms.clear();

		firebaseHelper.getFirebase().child("farms").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot != null && dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
					for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
						FarmVO farm = snapshot.getValue(FarmVO.class);

						farms.add(farm);
					}

					if (onPostExecute != null) {
						onPostExecute.execute(farms);
					}
				}
			}

			@Override
			public void onCancelled(FirebaseError firebaseError) {
			}
		});
	}

}

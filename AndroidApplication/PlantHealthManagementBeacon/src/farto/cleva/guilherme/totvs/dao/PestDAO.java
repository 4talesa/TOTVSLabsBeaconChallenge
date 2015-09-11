package farto.cleva.guilherme.totvs.dao;

import java.util.LinkedList;
import java.util.List;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import farto.cleva.guilherme.totvs.dao.framework.GenericDAO;
import farto.cleva.guilherme.totvs.db.FirebaseHelper;
import farto.cleva.guilherme.totvs.vo.PestVO;

public class PestDAO extends GenericDAO {

	private FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();

	private List<PestVO> pests = new LinkedList<PestVO>();

	public void getPests(final OnPostExecute onPostExecute) {
		pests.clear();

		firebaseHelper.getFirebase().child("pests").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot != null && dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
					for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
						PestVO pest = snapshot.getValue(PestVO.class);

						pests.add(pest);
					}

					if (onPostExecute != null) {
						onPostExecute.execute(pests);
					}
				}
			}

			@Override
			public void onCancelled(FirebaseError firebaseError) {
			}
		});
	}

}

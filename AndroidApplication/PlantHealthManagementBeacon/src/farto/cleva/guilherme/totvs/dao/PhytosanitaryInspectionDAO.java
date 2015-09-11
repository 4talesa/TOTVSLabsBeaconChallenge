package farto.cleva.guilherme.totvs.dao;

import java.util.List;
import farto.cleva.guilherme.totvs.dao.framework.GenericDAO;
import farto.cleva.guilherme.totvs.db.FirebaseHelper;
import farto.cleva.guilherme.totvs.vo.PhytosanitaryInspectionVO;

public class PhytosanitaryInspectionDAO extends GenericDAO {

	private FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();

	public void savePhytosanitaryInspections(List<PhytosanitaryInspectionVO> phytosanitaryInspections, final OnPostExecute onPostExecute) {
		for (PhytosanitaryInspectionVO phytosanitaryInspection : phytosanitaryInspections) {
			firebaseHelper.getFirebase().child("phytosanitary-inspections").child(phytosanitaryInspection.getId()).setValue(phytosanitaryInspection);
		}

		if (onPostExecute != null) {
			onPostExecute.execute(null);
		}
	}

}

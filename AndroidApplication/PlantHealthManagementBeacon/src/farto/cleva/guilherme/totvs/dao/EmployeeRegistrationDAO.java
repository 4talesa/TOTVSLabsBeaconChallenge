package farto.cleva.guilherme.totvs.dao;

import farto.cleva.guilherme.totvs.dao.framework.GenericDAO;
import farto.cleva.guilherme.totvs.db.FirebaseHelper;
import farto.cleva.guilherme.totvs.vo.EmployeeRegistrationVO;

public class EmployeeRegistrationDAO extends GenericDAO {

	private FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();

	public void saveEmployeeRegistration(EmployeeRegistrationVO employeeRegistration, final OnPostExecute onPostExecute) {
		firebaseHelper.getFirebase().child("employees-registrations").child(employeeRegistration.getId()).setValue(employeeRegistration);

		if (onPostExecute != null) {
			onPostExecute.execute(null);
		}
	}

}

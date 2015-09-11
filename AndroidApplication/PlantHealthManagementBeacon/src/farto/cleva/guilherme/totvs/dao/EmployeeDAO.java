package farto.cleva.guilherme.totvs.dao;

import java.util.LinkedList;
import java.util.List;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import farto.cleva.guilherme.totvs.dao.framework.GenericDAO;
import farto.cleva.guilherme.totvs.db.FirebaseHelper;
import farto.cleva.guilherme.totvs.vo.EmployeeVO;

public class EmployeeDAO extends GenericDAO {

	private FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();

	private List<EmployeeVO> employees = new LinkedList<EmployeeVO>();

	private EmployeeVO employee = null;

	public void saveEmployee(EmployeeVO employeeVO, final OnPostExecute onPostExecute) {
		firebaseHelper.getFirebase().child("employees").child(employeeVO.getId()).setValue(employeeVO);

		if (onPostExecute != null) {
			onPostExecute.execute(null);
		}
	}

	public void removeEmployee(String id, final OnPostExecute onPostExecute) {
		firebaseHelper.getFirebase().child("employees").child(id).removeValue();

		if (onPostExecute != null) {
			onPostExecute.execute(null);
		}
	}

	public EmployeeVO getEmployee(String id, final OnPostExecute onPostExecute) {
		firebaseHelper.getFirebase().child("employees").child(id).addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot != null && dataSnapshot.exists()) {
					employee = dataSnapshot.getValue(EmployeeVO.class);

					if (onPostExecute != null) {
						onPostExecute.execute(employee);
					}
				}
			}

			@Override
			public void onCancelled(FirebaseError firebaseError) {
			}
		});

		return employee;
	}

	public void getEmployees(final OnPostExecute onPostExecute) {
		employees.clear();

		firebaseHelper.getFirebase().child("employees").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot != null && dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
					for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
						EmployeeVO employee = snapshot.getValue(EmployeeVO.class);

						employees.add(employee);
					}

					if (onPostExecute != null) {
						onPostExecute.execute(employees);
					}
				}
			}

			@Override
			public void onCancelled(FirebaseError firebaseError) {
			}
		});
	}

}

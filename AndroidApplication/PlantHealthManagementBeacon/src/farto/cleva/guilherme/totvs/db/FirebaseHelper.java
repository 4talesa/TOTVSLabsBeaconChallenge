package farto.cleva.guilherme.totvs.db;

import com.firebase.client.Firebase;

public class FirebaseHelper {

	public static final String APP_NAME = "plant-health-mng-bcn";

	private Firebase firebase = null;

	private static FirebaseHelper helper = null;

	public static FirebaseHelper getInstance() {
		if (helper == null) {
			helper = new FirebaseHelper();
		}

		return helper;
	}

	private FirebaseHelper() {
		super();
	}

	public Firebase getFirebase() {
		firebase = new Firebase("https://" + APP_NAME + ".firebaseio.com");

		return firebase;
	}

}

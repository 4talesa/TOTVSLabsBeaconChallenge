package farto.cleva.guilherme.totvs.framework.services;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import farto.cleva.guilherme.totvs.framework.InMemoryDatabase;

public class MQTTService extends Service implements Runnable {

	public static final String SENSOR_URL = "farm/{0}/sensors/{1}";

	private Handler handler;

	private final String server = "m11.cloudmqtt.com";
	private final String port = "11288";
	private final String user = "totvslabs";
	private final String password = "totvs@123";
	private final String topic = "farm/#";

	private MqttClient mqttClient = null;
	private MqttConnectOptions mqttOptions = null;
	public static final int qos = 1;

	@Override
	public void onCreate() {
		super.onCreate();

		handler = new Handler();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(this).start();

		return Service.START_STICKY;
	}

	@Override
	public void run() {
		try {
			if (mqttClient == null) {
				mqttClient = new MqttClient("tcp://" + server + ":" + port, MqttClient.generateClientId(), new MemoryPersistence());

				mqttClient.setCallback(new MqttCallback() {

					@Override
					public void messageArrived(final String topic, final MqttMessage message) throws Exception {
						handler.post(new Runnable() {
							public void run() {
								InMemoryDatabase.getInstance().saveMqttSensorValue(topic, new String(message.getPayload()));
							}
						});
					}

					@Override
					public void deliveryComplete(IMqttDeliveryToken arg0) {
					}

					@Override
					public void connectionLost(Throwable arg0) {
					}
				});
			}

			if (mqttOptions == null) {
				mqttOptions = new MqttConnectOptions();
				mqttOptions.setKeepAliveInterval(0);
				mqttOptions.setCleanSession(true);
				mqttOptions.setUserName(user);
				mqttOptions.setPassword(password.toCharArray());
			}

			mqttClient.connect(mqttOptions);

			mqttClient.subscribe(topic, qos);

			handler.post(new Runnable() {
				public void run() {
					Toast.makeText(MQTTService.this, "Device connected to MQTT server.", Toast.LENGTH_SHORT).show();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		try {
			mqttClient.disconnect();

			handler.post(new Runnable() {
				public void run() {
					Toast.makeText(MQTTService.this, "Device disconnected from MQTT server.", Toast.LENGTH_SHORT).show();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}

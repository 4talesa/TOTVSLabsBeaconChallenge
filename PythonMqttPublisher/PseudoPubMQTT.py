import paho.mqtt.client as mqtt
import random

from random import randint
from time import sleep

# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc))

    # Subscribing in on_connect() means that if we lose the connection and
    # reconnect then subscriptions will be renewed.
    client.subscribe("#")

def simulate_sensors(client):
    client.publish("farm/" + str(randint(1,5)) + "/sensors/uv", str(randint(3,7)), 0)

    sleep(random.uniform(0.1, 2.0))

    client.publish("farm/" + str(randint(1,5)) + "/sensors/temperature", str(randint(15,42)), 0)

    sleep(random.uniform(0.1, 2.0))

    client.publish("farm/" + str(randint(1,5)) + "/sensors/humidity", str(randint(10,85)), 0)

    sleep(random.uniform(0.1, 2.0))

# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    print(msg.topic+" "+str(msg.payload))

client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message

client.username_pw_set("totvslabs", "totvs@123")
client.connect("m11.cloudmqtt.com", 11288, 5)

for x in range(0, 100):
    simulate_sensors(client)

# Blocking call that processes network traffic, dispatches callbacks and
# handles reconnecting.
# Other loop*() functions are available that give a threaded interface and a
# manual interface.
# client.loop_forever()
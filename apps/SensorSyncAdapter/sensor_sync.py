import json
import time
import secrets
import requests
from confluent_kafka import Consumer

# === Конфигурация ===
KAFKA_BOOTSTRAP_SERVERS = "kafka:9092"
KAFKA_TOPIC = "smarthome-db.public.sensors"
KAFKA_GROUP_ID = "sensor-sync-simple"

LOCATION_SERVICE_URL = "http://location-service-app:8092/api/v1"
DEVICE_SERVICE_URL = "http://device-service-app:8090/api/v1"

USER_ID = 1
DEVICE_TYPE_ID = 1


def create_kafka_consumer():
    conf = {
        'bootstrap.servers': KAFKA_BOOTSTRAP_SERVERS,
        'group.id': KAFKA_GROUP_ID,
        'auto.offset.reset': 'earliest',
        'enable.auto.commit': True,
    }
    consumer = Consumer(conf)
    consumer.subscribe([KAFKA_TOPIC])
    return consumer


def create_location(location_name: str) -> int:
    payload = {
        "userId": USER_ID,
        "name": location_name
    }
    resp = requests.post(
        f"{LOCATION_SERVICE_URL}/locations",
        json=payload,
        verify=False,
        timeout=10
    )
    resp.raise_for_status()
    data = resp.json()
    return data["locationId"]


def create_device(name: str, location_id: int, serial_number: str):
    payload = {
        "name": name,
        "locationId": location_id,
        "deviceTypeId": DEVICE_TYPE_ID,
        "serialNumber": serial_number
    }
    resp = requests.post(
        f"{DEVICE_SERVICE_URL}/devices",
        json=payload,
        verify=False,
        timeout=10
    )
    if resp.status_code == 409:
        print(f"⚠️  Device with serial '{serial_number}' already exists. Skipping.")
        return
    resp.raise_for_status()
    print(f"✅ Created device: {name} (serial: {serial_number}) in location {location_id}")


def process_message(msg):
    try:
        data = json.loads(msg.value().decode('utf-8'))
        payload = data.get("payload", {})
        op = payload.get("op")

        if op != "c":
            return  # Обрабатываем только создание

        after = payload.get("after")
        if not after:
            return

        sensor_name = after.get("name")
        sensor_location = after.get("location")

        if not sensor_name or not sensor_location:
            print("❌ Missing 'name' or 'location' in sensor event")
            return

        print(f"🆕 New sensor: name='{sensor_name}', location='{sensor_location}'")

        # Шаг 1: Создать локацию
        try:
            location_id = create_location(sensor_location)
            print(f"🏠 Created location '{sensor_location}' → ID: {location_id}")
        except requests.HTTPError as e:
            print(f"❌ Failed to create location '{sensor_location}': {e}")
            return

        # Шаг 2: Создать устройство
        serial = secrets.token_hex(5)  # 10 hex символов
        try:
            create_device(sensor_name, location_id, serial)
        except requests.HTTPError as e:
            print(f"❌ Failed to create device '{sensor_name}': {e}")

    except Exception as e:
        print(f"💥 Error processing message: {e}")


def main():
    print("🚀 Starting Sensor Sync Service...")
    consumer = create_kafka_consumer()

    try:
        while True:
            msg = consumer.poll(timeout=1.0)
            if msg is None:
                continue
            if msg.error():
                print(f"❗ Kafka error: {msg.error()}")
                continue

            process_message(msg)

    except KeyboardInterrupt:
        print("\n🛑 Shutting down...")
    finally:
        consumer.close()


if __name__ == "__main__":
    main()
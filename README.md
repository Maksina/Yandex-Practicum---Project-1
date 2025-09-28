# Project_template

# Задание 1. Анализ и планирование

### 1. Описание функциональности монолитного приложения

**Управление отоплением:**

- Пользователи могут удалённо включать/выключать отопление в своих домах.
- Текущая реализация системы не поддерживает прямое включение/отключение отопления, но поддерживает ручное (с помощью REST API) обновление температуры на датчиках. Учитывая описание системы и ее фактическую реализацию, можно предположить, что есть еще одна система, которая подключена к текущей базе данных (или настроены механизмы CDC, которые могут отслеживать изменения в БД и быть триггером для включения/отключения отопления).


**Мониторинг температуры:**

- Пользователи могут через веб-интерфейс получить наглядную информацию о текущем состоянии температурных датчиков, установленных в различных зонах дома. Для каждого датчика отображается его точное местоположение (например, "кухня" или "гостинная"), актуальное значение температуры, а так же статус.
- Система поддерживает полный жизненный цикл датчиков через REST API (CRUD): реализована возможность добавлять новые устройства, удалять устаревшие или вышедшие из строя, получать данные(статус, температура, местоположение) в режиме реального времени, обновлять данные датчиков для управления отоплением ("см. пункт управление отоплением"). 
	Система поддерживает гибкие запросы: можно получить данные как по конкретному идентификатору датчика, так и по его логическому местоположению(например, "кухня" или "гостинная").

### 2. Анализ архитектуры монолитного приложения

- **Язык программирования**: Go. Версия 1.22
- **База данных**: PostgreSQL. Версия: 16
- **Архитектура**: Монолитная. Вся логика системы и БД реализованы в рамках одного приложения.
- **Взаиомодействия**:  
	- **Входящие**: система предоставляет синхронное API (REST)  
	- **Исходящие**: система обращается к API датчиков для получения текущего состояния - синхронно, через REST. 
- **Масштабируемость**: Ограничена архитектурой, можно масштабировать как вертикально, так и горизонтально, но только все приложение, а не отдельные части.
- **Информационная безопасность**:
	- **Чувствительные данные**: отсутствуют
	- **Авторизация и аутентификация**: отсутствует или реализована внешним сервисом
- **Развертывание**: Требует остановки всего приложения, описано в файле docker-compose.yml и выполняется с помощью Docker Compose.

### 3. Определение доменов и границы контекстов

1. **Домент**: Управление IoT-устройствами
	- **Контекст**: Взаимодействие(управление, мониторинг) и администрирование IoT-устройств
2. **Домен**: Управление пользователями
	- **Контекст**: Информация о клиенте: профиль, контактные данные
3. **Домен**: Управление помещением
	- **Контекст**: Разделение помещения на комнаты/зоны, управление привязкой датчика к команте/зоне
4. **Домен**: Управление доступом
	- **Контекст**: Управление ролевой моделью и политиками доступа
	- **Примечание**: Домен выделен на основе описания системы, с технической точки зрения такая реализация отсутствует.

### 4. Визуализация контекста системы — диаграмма С4

[Диаграмма контекста](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/context/context.plantuml)


![Image Диаграмма контекста](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/context/context.png)

# Задание 2. Проектирование микросервисной архитектуры

**Диаграмма контейнеров (Containers)**

[Диаграмма контейнеров](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/containers/containers.plantuml)


![Image Диаграмма контейнеров](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/containers/containers.png)

**Диаграмма компонентов (Components)**

1. Сервис телеметрии (TelemetryService)
[Диаграмма компонентов](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/components/telemetry_service/components_telemetryservice.plantuml)


![Image Диаграмма компонентов](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/components/telemetry_service/components_telemetryservice.png)

2. Сервис управления устройствами (DeviceService)
[Диаграмма компонентов](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/components/device_service/components_deviceservice.plantuml)


![Image Диаграмма компонентов](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/components/device_service/components_deviceservice.png)

3. Сервис управления пользователями (UserService)
[Диаграмма компонентов](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/components/user_service/components_userservice.plantuml)


![Image Диаграмма компонентов](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/components/user_service/components_userservice.png)

4. Сервис управления автоматизациями (AutomationService)
[Диаграмма компонентов](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/components/automation_service/components_automationservice.plantuml)


![Image Диаграмма компонентов](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/components/automation_service/components_automationservice.png)

5. Сервис управления помещениями (LocationService)
[Диаграмма компонентов](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/components/location_service/components_locationservice.plantuml)


![Image Диаграмма компонентов](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/components/location_service/components_locationservice.png)

6. Адаптер HTTP (HTTPAdapter)
[Диаграмма компонентов](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/components/http_adapter/components_httpadapter.plantuml)


![Image Диаграмма компонентов](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/components/http_adapter/components_httpadapter.png)

7. Адаптер MQTT (MQTTAdapter)
[Диаграмма компонентов](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/components/mqtt_adapter/components_mqttadapter.plantuml)


![Image Диаграмма компонентов](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/components/mqtt_adapter/components_mqttadapter.png)

**Диаграмма кода (Code)**

*DeviceService*
[Диаграмма кода](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/code/device_service/code_deviceservice.plantuml)


![Image Диаграмма кода](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/code/device_service/code_deviceservice.png)

# Задание 3. Разработка ER-диаграммы

Примечание: TimeSeriesDB (InfluxDB), которая используется для хранения телеметрии, отображена на схеме как "note", т.к. не является реляционной и не укладывается в описываемый формат ERD.

[ER-диаграмма](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/erd/erd.plantuml)


![Image ER-диаграмма](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/erd/erd.png)

# Задание 4. Создание и документирование API

### 1. Тип API

1. Между UI и микросервисами (AutomationService, DeviceService, LocationService, TelemetryService, UserService) - Синхронное взаимодействие HTTP/REST  
Тезисы, которые привели к такому выбору:
	- Синхронность, которая позволяет пользователю быстро получать ответ
	- Производительность, которую можно увеличивать не только количеством реплик, но и быстрым добавлением кэша
	- Легкость внесения изменений, поддержки, отладки


2. Между адаптерами(HTTPAdapter и MQTTAdapter) и микросервисами (AutomationService, DeviceService, LocationService, TelemetryService, UserService) - Асинхронное взаимодействие через Kafka  
Тезисы, которые привели к такому выбору:
	- Надежность и упорядоченность, которая важна для сбора телеметрии и отправки событий
	- Масштабируемость
	- Отказоустойчивость и независимость между микросервисами


### 2. Документация API

1. *AutomationService*
[openapi](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/api/automation_service.yaml)
2. *DeviceService*
[openapi](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/api/device_service.yaml)
3. *LocationService*
[openapi](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/api/location_service.yaml)
4. *TelemetryService*
[openapi](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/api/telemetry_service.yaml)
5. *UserService*
[openapi](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/api/user_service.yaml)

# Задание 5. Работа с docker и docker-compose

*Выполнено*
Реализован [DeviceTempApi](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/apps/DeviceTempApi)

# **Задание 6. Разработка MVP**

План плавного перехода:

1. Переносим монолит за новый API Gateway
2. Обеспечиваем миграцию данных из БД Монолита в БД микросервисов:
	- Подключаем CDC (Debezium), который "слушает" все изменения в БД и публикует их в Kafka
	- Реализовываем и подключаем сервис-адаптер, который "слушает" топик Kafka и вызывает реализованные микросервисы
3. Применяем паттерн "Душитель": по мере реализации микросервисов и доработки UI, переводим запросы через API Gateway к реализованным микросервисам

Таким образом переходная схема контейнеров (C4):

[Диаграмма контейнеров (переходный процесс)](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/containers/migration/migration_containers.plantuml)

![Image Диаграмма контейнеров(переходный процесс)](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/schemas/containers/migration/migration_containers.png)


В данном задании:
1. Настроена и запущена Kafka с топиком "smarthome-db.public.sensors", в котором записываются все изменения таблицы БД Sensors из монолита.
2. Настроен и запущен механизм CDC(Debezium), который подключается к БД Монолита и реплицирует данные в Kafka "smarthome-db.public.sensors"
3. Реализован DeviceService для управления устройствами. ЯП: Java
4. Реализован LocationService для управления помещениями. ЯП: Java
5. Реализован TelemetryService для управления телеметрией. ЯП: Java
6. Реализован SensorSyncAdapter, сервис-адаптер, который будет получает изменения в таблице БД монолита через Kafka и отправляет соответствующие запросы в DeviceService и LocationService. ЯП: Python

Примечания и допущения: 
1. В качестве MVP выбран процесс заведения нового датчика через монолит и миграция этих данных в микросервисы DeviceService и LocationService.
2. Учитывая, что в текущем примере монолита нет привязки устройства к пользователю, то в новой реализации будем считать, что все устройства автоматически привязываются к пользователю с user_id = 1, а дальше их вручную перепривязывают специалисты.
3. Учитывая, что в текущем примере монолита все устройства - температурные датчики, то в новой реализации будем сохранять все устройства с device_type = 1 (temperature sensor)
4. Учитывая текущую реализацию монолита, интеграция с ним сервиса телеметрии лишена смысла. Поэтому сервис телеметрии будет реализовывать последним, как завершающий этап перевода монолита на микросервисы.


Инструкция по запуску:
1. Запуск приложений: docker-compose up --build
2. Запуск CDC: curl.exe -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" -d "@sensors-connector.json" http://localhost:8083/connectors/

План проверки:

1. Создаем новый датчик в монолитном приложении: POST http://localhost:8080/api/v1/sensors
2. Проверяем, что новое помещение в микросервисе LocationService создано успешно: http://localhost:8092/api/v1/locations?userId=1
3. Проверяем, что новый датчик в микросервисе DeviceService успешно создан и привязан к новому помещению: http://localhost:8090/api/v1/devices?locationId=1

Для удобства проверки:
[Postman collection](https://github.com/Maksina/Yandex-Practicum---Project-1/blob/warmhouse/apps/check-mvp-smarthome.postman_collection.json)

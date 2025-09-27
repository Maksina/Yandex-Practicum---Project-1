# Project_template

Это шаблон для решения проектной работы. Структура этого файла повторяет структуру заданий. Заполняйте его по мере работы над решением.

# Задание 1. Анализ и планирование

<aside>

Чтобы составить документ с описанием текущей архитектуры приложения, можно часть информации взять из описания компании и условия задания. Это нормально.

</aside

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

Укажите, какой тип API вы будете использовать для взаимодействия микросервисов. Объясните своё решение.

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

Необходимо создать новые микросервисы и обеспечить их интеграции с существующим монолитом для плавного перехода к микросервисной архитектуре. 

### **Что нужно сделать**

1. Создайте новые микросервисы для управления телеметрией и устройствами (с простейшей логикой), которые будут интегрированы с существующим монолитным приложением. Каждый микросервис на своем ООП языке.
2. Обеспечьте взаимодействие между микросервисами и монолитом (при желании с помощью брокера сообщений), чтобы постепенно перенести функциональность из монолита в микросервисы. 

В результате у вас должны быть созданы Dockerfiles и docker-compose для запуска микросервисов. 
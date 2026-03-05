# RepairDesk

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.3-brightgreen?style=flat-square)
![Spring Security](https://img.shields.io/badge/Spring_Security-✓-brightgreen?style=flat-square)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1-green?style=flat-square)
![H2](https://img.shields.io/badge/H2_Database-✓-blue?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)

**RepairDesk** — веб-приложение для управления заказами на ремонт электроники.  
Помогает сервисным инженерам вести клиентскую базу, каталог устройств и заказы на ремонт в едином интерфейсе.

---

## 🌐 Demo

**https://repairdesk-production-160c.up.railway.app/**

---

## 📸 Screenshots
| Welcome | Login |
|-------|-----------|
| ![Welcome](screenshots/welcome.png) | ![Login](screenshots/login.png) |

| Dashboard | Orders |
|-------|-----------|
| ![Dashboard](screenshots/dashboard.png) | ![Orders](screenshots/orders.png) |

| Clients | Devices |
|---------|--------|
| ![Clients](screenshots/clients.png) | ![Devices](screenshots/devices.png) |

| Parts Search | |
|-------------|--|
| ![Parts](screenshots/parts.png) | |

---

## ✨ Features

- **Аутентификация** — регистрация и авторизация через Spring Security с BCrypt
- **Клиенты** — создание, редактирование, удаление клиентской базы
- **Устройства** — каталог техники по бренду, модели и типу
- **Заказы** — полный цикл ремонта: диагностика, согласование, стоимость, статус
- **Дашборд** — сводная статистика с последними заказами
- **Курс валют** — актуальный курс USD/EUR с API ПриватБанка, обновляется автоматически
- **Двойная валюта** — цены в заказах отображаются в гривнах и долларах одновременно
- **Поиск запчастей** — парсинг актуальных цен и наличия с [aks.ua](https://www.aks.ua) (Jsoup)
- **Мультиязычность** — интерфейс на украинском, русском и английском (i18n, cookie)

---

## 🛠 Tech Stack

### Backend
| Технология | Версия | Описание |
|-----------|--------|----------|
| Java | 21 | Основной язык |
| Spring Boot | 4.0.3 | Фреймворк приложения |
| Spring MVC | — | Обработка HTTP запросов |
| Spring Security | 7.x | Аутентификация и авторизация |
| Spring Data JPA | — | Работа с базой данных |
| Hibernate | — | ORM |
| Jsoup | 1.17.2 | HTML парсинг (aks.ua) |
| Lombok | — | Снижение boilerplate-кода |
| Jackson | 3.x | JSON сериализация |

### Frontend
| Технология | Описание |
|-----------|----------|
| Thymeleaf 3.1 | Серверный шаблонизатор |
| HTML5 / CSS3 | Вёрстка |
| IBM Plex Sans / Mono | Основная типографика |
| Rajdhani | Заголовки и числа |

### Database & Tools
| Инструмент | Описание |
|-----------|----------|
| H2 Database | Встроенная in-memory БД |
| Maven | Сборка проекта |

---

## 🏗 Architecture

Классическая трёхслойная архитектура Spring Boot:

```
HTTP Request
     ↓
Controller  (ViewController, AuthController)
     ↓
Service     (ClientService, DeviceService, RepairOrderService,
             ExchangeRateService, AksParserService ...)
     ↓
Repository  (JpaRepository)
     ↓
H2 Database
```

### Структура проекта

```
src/main/java/com/Vlad/RepairDesk/
├── config/
│   ├── SecurityConfig.java          # Spring Security
│   └── LocaleConfig.java            # i18n + CookieLocaleResolver
├── controller/
│   ├── ViewController.java          # dashboard, clients, orders, parts...
│   └── AuthController.java          # регистрация / логин
├── service/
│   ├── ClientService.java
│   ├── DeviceService.java
│   ├── RepairOrderService.java
│   ├── CurrentUserService.java
│   ├── ExchangeRateService.java     # API ПриватБанка
│   └── AksParserService.java        # парсинг aks.ua
├── repository/
│   ├── ClientRepository.java
│   ├── DeviceRepository.java
│   └── RepairOrderRepository.java
├── model/
│   ├── User.java
│   ├── Client.java
│   ├── Device.java
│   └── RepairOrder.java
└── dto/
    ├── RepairOrderRequestDTO.java
    ├── RepairOrderResponseDTO.java
    └── AksProductDTO.java

src/main/resources/
├── templates/
│   ├── layout.html                  # базовый layout с сайдбаром
│   ├── dashboard.html
│   ├── clients.html
│   ├── devices.html
│   ├── orders.html
│   ├── parts_search.html            # поиск запчастей
│   ├── login.html
│   ├── index.html
│   └── create_*/edit_* ...
├── messages.properties              # English
├── messages_uk.properties           # Українська
├── messages_ru.properties           # Русский
└── application.properties
```

---

## 🚀 Getting Started

### 1. Клонировать репозиторий

```bash
git clone https://github.com/username/RepairDesk.git
cd RepairDesk
```

### 2. Запустить приложение

```bash
mvn spring-boot:run
```

Приложение запустится на **http://localhost:8080**

### 3. Зарегистрироваться

Перейди на `/login`, переключись на вкладку **Register** и создай аккаунт.

---

## 🗄 Database

Используется встроенная **H2 in-memory** БД — ничего устанавливать не нужно.

Консоль доступна по адресу: **http://localhost:8080/h2-console**

```
JDBC URL:  jdbc:h2:mem:repairdesk
User:      sa
Password:  (пусто)
```

> ⚠️ Данные сбрасываются при перезапуске. Для постоянного хранения необходимо заменить H2.

---

## 🔌 External Integrations

### ПриватБанк API — курс валют
Автоматически подтягивает актуальный курс USD/EUR на дашборд и в страницу заказов.
```
GET https://api.privatbank.ua/p24api/pubinfo?exchange&coursid=5
```
Если API недоступен — используются fallback-значения, приложение не падает.

### aks.ua — парсинг запчастей (Jsoup)
Страница поиска запчастей парсит название, цену (текущую и старую) и статус наличия товара в реальном времени.
```
GET https://www.aks.ua/uk/search?for={query}
```

---

## 🔒 Security

- Все страницы защищены — неавторизованные пользователи редиректятся на `/login`
- Пароли хешируются через **BCrypt**
- Каждый пользователь видит только **свои** данные (multi-tenant через `user_id`)
- CSRF-защита включена (отключена для H2 Console в dev-режиме)

---

## 🌍 Internationalization

Язык интерфейса сохраняется в cookie и меняется одним кликом прямо в сайдбаре.

| Код | Язык |
|-----|------|
| `en` | English |
| `uk` | Українська |
| `ru` | Русский |

---

## 📦 Deployment

```bash
# Сборка исполняемого JAR
mvn clean package -DskipTests

# Запуск
java -jar target/RepairDesk-0.0.1-SNAPSHOT.jar
```

Поддерживаемые платформы: **Railway**, **Render**, **VPS**, **Docker**

---

## 🗺 Roadmap

- [ ] Роли пользователей (admin / technician)
- [ ] Поиск и фильтрация по клиентам и заказам
- [ ] Экспорт заказов в PDF / Excel
- [ ] Email-уведомления о смене статуса ремонта
- [ ] История изменений заказа
- [ ] PostgreSQL для production-деплоя

---

## 👤 Author

**Vlad Saienko** — Electronics repair technician & Java developer

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

# Проект по автоматизации тестирования MAX

UI-автотесты публичных страниц [MAX](https://max.ru), страницы загрузки и веб-формы входа.

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.java.com/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5.14-green)](https://junit.org/junit5/)
[![Selenide](https://img.shields.io/badge/Selenide-7.14-blue)](https://selenide.org/)
[![Gradle](https://img.shields.io/badge/Gradle-9.2-02303A)](https://gradle.org/)
[![Allure](https://img.shields.io/badge/Allure-Report-yellow)](https://allurereport.org/)
[![Jenkins](https://img.shields.io/badge/Jenkins-CI-red)](https://www.jenkins.io/)

## Ссылки

- [репозиторий GitHub](https://github.com/olegdunyushkin/max-ui-autotests);
- [задача Jenkins](https://jenkins.autotests.cloud/job/C40-Oleg_Dunyushkin-MAX_UI_Tests/);
- [успешная сборка Jenkins](https://jenkins.autotests.cloud/job/C40-Oleg_Dunyushkin-MAX_UI_Tests/1/);
- [Allure Report](https://jenkins.autotests.cloud/job/C40-Oleg_Dunyushkin-MAX_UI_Tests/1/allure/);
- [задача Jira](https://jira.autotests.cloud/browse/HOMEWORK-1624).

## Что проверяется

- основная информация на главной странице;
- ссылки на магазины приложений, служебные и юридические страницы;
- варианты приложения для Android, iPhone и компьютера;
- начальная форма входа по QR-коду;
- переключение между входом по QR-коду и номеру телефона;
- состояние кнопки входа для номеров разной длины;
- выбор страны и телефонного кода;
- позитивный и негативный поиск страны;
- русская и английская локализации формы входа.

В проекте 10 тестовых методов. За счёт параметризации полный запуск содержит 17 независимых
сценариев. Матрица автоматизированных и ручных проверок находится в
[документе по тест-дизайну](docs/test-design.md).

## Тест-дизайн

При выборе проверок использованы классы эквивалентности, граничные значения, таблица решений,
переходы состояний и риск-ориентированный подход. Автоматизация не отправляет СМС, не использует
реальные учётные записи и не выполняет вход в профиль.

## Структура проекта

```text
src/test/java
├── config       # общая конфигурация браузера
├── helpers      # вложения для Allure
├── pages        # Page Object
└── tests        # тестовые классы

docs
├── images       # материалы для README
└── test-design.md
```

## Запуск тестов

Для запуска требуется Java 17.

Полный локальный запуск в headless-режиме:

```bash
./gradlew clean test -Dheadless=true
```

Только критичные smoke-тесты:

```bash
./gradlew clean test -Dheadless=true -Dtags=smoke
```

Удалённый запуск в Selenoid:

```bash
./gradlew clean test \
  -DremoteUrl="https://${SELENOID_USER}:${SELENOID_PASSWORD}@selenoid.autotests.cloud/wd/hub" \
  -Dbrowser=chrome \
  -DbrowserVersion=149.0 \
  -DbrowserSize=1920x1080
```

Основные параметры:

| Параметр | Значение по умолчанию | Назначение |
|---|---|---|
| `baseUrl` | `https://max.ru` | адрес главной страницы |
| `webUrl` | `https://web.max.ru` | адрес веб-версии |
| `remoteUrl` | пусто | адрес удалённого браузера |
| `browser` | `chrome` | браузер |
| `browserVersion` | пусто | версия удалённого браузера |
| `browserSize` | `1920x1080` | разрешение окна |
| `headless` | `false` | запуск без отображения браузера |
| `tags` | пусто | группа запускаемых тестов |

Для локального просмотра Allure Report:

```bash
./gradlew allureServe
```

## Jenkins

Jenkins получает код из ветки `main`, запускает тесты в Selenoid и строит Allure Report.

![Успешная сборка Jenkins](docs/images/jenkins-build.png)

## Allure Report

В отчёте отображаются 17 успешных сценариев, группировка по функциональности, русские шаги,
теги, приоритет и владелец тестов.

![Обзор Allure Report](docs/images/allure-overview.png)

Для каждого теста прикладываются скриншот, Page Source, логи браузера и видео Selenoid.

![Шаги и вложения теста](docs/images/allure-test.png)

## Пример запуска

![Запуск теста в Selenoid](docs/images/max-test-run.gif)

## Telegram

После сборки бот отправляет в учебный чат результат запуска и ссылку на Allure Report.

<img src="docs/images/telegram-notification.png" alt="Уведомление Telegram" width="500">

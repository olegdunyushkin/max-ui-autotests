<p align="center">
  <img src="docs/images/max-logo.svg" alt="Логотип MAX" width="120">
</p>

<h1 align="center">Проект по автоматизации тестирования MAX</h1>

<p align="center">
  UI-автотесты публичных страниц MAX, страницы загрузки и веб-формы входа.
</p>

[![UI Tests](https://github.com/olegdunyushkin/max-ui-autotests/actions/workflows/ui-tests.yml/badge.svg?branch=portfolio-version)](https://github.com/olegdunyushkin/max-ui-autotests/actions/workflows/ui-tests.yml?query=branch%3Aportfolio-version)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.java.com/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5.14-green)](https://junit.org/junit5/)
[![Selenide](https://img.shields.io/badge/Selenide-7.14-blue)](https://selenide.org/)
[![Gradle](https://img.shields.io/badge/Gradle-9.2-02303A)](https://gradle.org/)
[![Allure](https://img.shields.io/badge/Allure-Report-yellow)](https://allurereport.org/)
[![Jenkins](https://img.shields.io/badge/Jenkins-CI-red)](https://www.jenkins.io/)

## Ссылки

- [проект на GitHub](https://github.com/olegdunyushkin/max-ui-autotests);
- [публичные запуски GitHub Actions](https://github.com/olegdunyushkin/max-ui-autotests/actions/workflows/ui-tests.yml);
- [задача Jenkins](https://jenkins.autotests.cloud/job/C40-Oleg_Dunyushkin-MAX_UI_Tests/);
- [успешная учебная сборка Jenkins](https://jenkins.autotests.cloud/job/C40-Oleg_Dunyushkin-MAX_UI_Tests/1/);
- [Allure Report учебной сборки](https://jenkins.autotests.cloud/job/C40-Oleg_Dunyushkin-MAX_UI_Tests/1/allure/);
- [задача Jira](https://jira.autotests.cloud/browse/HOMEWORK-1624).

## Покрытие

Проект содержит **16 тестовых методов и 54 независимых сценария**:

- контент, метаданные и семантическая структура главной страницы;
- 14 навигационных, служебных и юридических ссылок;
- адаптивность на мобильном, планшетном и десктопном разрешениях;
- варианты загрузки и вариативные ссылки для Android, iPhone и компьютера;
- состояния входа по QR-коду и номеру телефона;
- маска номера, фильтрация символов и граничные значения длины;
- выбор и поиск стран по названию, части названия и телефонному коду;
- русская, английская, испанская и португальская локализации.

Подробная матрица, техники тест-дизайна, риски и ручные проверки описаны в
[документе по тест-дизайну](docs/test-design.md).

## Стек

| Инструмент | Назначение |
|---|---|
| Java 17 | язык и версия выполнения |
| Selenide | управление браузером и ожидания |
| JUnit 5 | запуск, параметризация и теги |
| Gradle | сборка и управление зависимостями |
| Allure | шаги, категории, окружение и вложения |
| Selenoid | удалённые браузеры, VNC и видео |
| Jenkins | учебный CI и отправка уведомлений |
| GitHub Actions | публичный CI с выбором набора и браузера |

## Архитектура

```mermaid
flowchart LR
    T[JUnit 5 tests] --> D[Test data records]
    T --> P[Page Objects]
    P --> S[Selenide]
    S --> B[Local browser or Selenoid]
    T --> A[Allure results]
    B --> A
    A --> R[Allure Report]
```

```text
src/test/java
├── config       # параметры браузера и окружение Allure
├── helpers      # скриншот, Page Source, URL, логи и видео
├── pages        # действия и проверки Page Object
├── testdata     # типизированные наборы тестовых данных
└── tests        # независимые тестовые сценарии

src/test/resources
├── allure.properties
└── categories.json

.github
├── workflows    # публичный запуск UI-тестов
└── dependabot.yml
```

## Запуск

Для запуска требуется Java 17. Файл `.java-version` и Gradle Toolchain фиксируют нужную версию.

Критичный smoke-набор:

```bash
./gradlew clean smokeTest -Dheadless=true
```

Расширенный regression-набор:

```bash
./gradlew clean regressionTest -Dheadless=true
```

Проверки адаптивности:

```bash
./gradlew clean responsiveTest -Dheadless=true
```

Полный локальный запуск:

```bash
./gradlew clean test -Dheadless=true
```

Удалённый параллельный запуск в Selenoid:

```bash
./gradlew clean test \
  -DremoteUrl="https://${SELENOID_USER}:${SELENOID_PASSWORD}@selenoid.autotests.cloud/wd/hub" \
  -Dbrowser=chrome \
  -DbrowserVersion=149.0 \
  -DbrowserSize=1920x1080 \
  -Dparallel=true \
  -Dthreads=3
```

### Параметры

| Параметр | По умолчанию | Назначение |
|---|---|---|
| `baseUrl` | `https://max.ru` | главная страница |
| `webUrl` | `https://web.max.ru` | веб-версия |
| `downloadUrl` | `https://download.max.ru` | страница загрузки |
| `remoteUrl` | пусто | адрес удалённого браузера |
| `browser` | `chrome` | браузер |
| `browserVersion` | пусто | версия удалённого браузера |
| `browserSize` | `1920x1080` | размер окна |
| `headless` | `false` | запуск без интерфейса браузера |
| `timeout` | `15000` | ожидание Selenide в миллисекундах |
| `requestInterval` | `5000` | минимальный интервал между открытиями страниц |
| `pageLoadStrategy` | `normal` | стратегия ожидания загрузки страницы |
| `pageLoadTimeout` | `30000` | максимальное ожидание загрузки страницы |
| `tags` | пусто | произвольный набор JUnit-тегов |
| `parallel` | `false` | параллельный запуск |
| `threads` | `3` | число параллельных потоков |

Проект ограничивает частоту открытия страниц, чтобы не перегружать внешний production-сайт.
Если MAX всё же включает антибот, оставшиеся тесты текущего запуска пропускаются без новых запросов.
Полный regression предназначен для тестового окружения или разрешённого IP; публичный CI запускает
только короткий smoke. Параллельность следует увеличивать лишь при известных допустимых лимитах.

Локальный просмотр Allure Report:

```bash
./gradlew allureServe
```

## Отчётность

Allure группирует тесты по `Epic → Feature → Story` и показывает параметры, шаги и окружение.
Для каждого браузерного сценария сохраняются:

- скриншот;
- HTML-код страницы;
- текущий URL;
- фактическая версия браузера, платформа и размер окна;
- Navigation Timing страницы в JSON;
- логи консоли браузера;
- видео при удалённом запуске.

Пример отчёта принятой учебной сборки:

![Обзор Allure Report](docs/images/allure-overview.png)

![Шаги и вложения теста](docs/images/allure-test.png)

## CI

GitHub Actions автоматически запускает smoke-набор из трёх критичных сценариев при push и pull
request. Ручной запуск позволяет выбрать `smoke`, `regression`, `responsive` или полный набор,
Chrome или Firefox, адреса окружения, интервал запросов и до трёх параллельных потоков.
JUnit-результаты, HTML-отчёт Gradle, Allure Results и готовый Allure Report сохраняются как
артефакты.

Jenkins запускает тесты в Selenoid, строит Allure Report и отправляет результат в Telegram.

![Успешная сборка Jenkins](docs/images/jenkins-build.png)

<img src="docs/images/telegram-notification.png" alt="Уведомление Telegram" width="500">

## Демонстрация

![Запуск UI-теста MAX](docs/images/max-test-run.gif)

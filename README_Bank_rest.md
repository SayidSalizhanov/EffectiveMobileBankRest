# Логика контроллеров

- **AuthenticationController**: Обрабатывает аутентификацию пользователей через POST `/api/auth/login`, принимает `AuthenticationRequest`, возвращает `AuthenticationResponse`.

- **CardController**: Управляет банковскими картами:
    - GET `/api/cards` — список карт (USER, ADMIN).
    - GET `/api/cards/{number}` — данные карты по номеру (USER, ADMIN).
    - POST `/api/cards` — создание карты (ADMIN).
    - POST `/api/cards/{number}/block` — блокировка карты (ADMIN).
    - POST `/api/cards/{number}/activate` — активация карты (ADMIN).
    - DELETE `/api/cards/{number}` — удаление карты (ADMIN).
    - GET `/api/cards/{number}/balance` — баланс карты (USER, ADMIN).
    - POST `/api/cards/{number}/block-request` — запрос на блокировку (USER).
    - GET `/api/cards/block-requests` — список запросов на блокировку (ADMIN).
    - GET `/api/cards/block-requests/status/{status}` — запросы по статусу (ADMIN).
    - POST `/api/cards/block-requests/{requestId}/approve` — одобрение запроса (ADMIN).
    - POST `/api/cards/block-requests/{requestId}/reject` — отклонение запроса (ADMIN).

- **TransferController**: Обрабатывает переводы между картами:
    - POST `/api/transfers` — выполнение перевода (USER, ADMIN).

- **UserController**: Управляет пользователями:
    - GET `/api/users` — список пользователей (ADMIN).
    - GET `/api/users/{userId}` — данные пользователя по ID (ADMIN).
    - POST `/api/users` — создание пользователя (ADMIN).
    - PUT `/api/users/{userId}` — обновление данных пользователя (ADMIN).
    - DELETE `/api/users/{userId}` — удаление пользователя (ADMIN).

# Логика DTO request классов

- **UserPasswordUpdateRequest**: DTO для обновления пароля пользователя:
    - `oldPassword`: старый пароль (обязателен).
    - `newPassword`: новый пароль (6–255 символов, обязателен).

- **UserCreateRequest**: DTO для создания пользователя:
    - `login`: логин (3–50 символов, обязателен).
    - `password`: пароль (6–255 символов, обязателен).

- **CardCreateRequest**: DTO для создания банковской карты:
    - `number`: номер карты (16 цифр, обязателен).
    - `expirationDate`: дата истечения (будущая, обязательна).
    - `ownerId`: ID владельца (обязателен).

- **TransferRequest**: DTO для перевода между картами:
    - `numberFrom`: номер карты-источника (16 цифр, обязателен).
    - `numberTo`: номер карты-получателя (16 цифр, обязателен).
    - `amount`: сумма перевода (положительная, минимум 0.01, обязательна).

- **AuthenticationRequest**: DTO для аутентификации:
    - `login`: логин пользователя.
    - `password`: пароль пользователя.

# Логика DTO response классов

- **AuthenticationResponse**: DTO для ответа на аутентификацию:
    - `token`: токен доступа.

- **UserResponse**: DTO для данных пользователя:
    - `login`: логин пользователя.
    - `cardsNumbers`: список номеров карт.
    - `rolesNames`: список ролей пользователя.
    - `blockRequestStatuses`: список статусов запросов на блокировку.

- **BalanceResponse**: DTO для баланса карты:
    - `cardNumber`: номер карты.
    - `balance`: текущий баланс.

- **ExceptionMessage**: DTO для сообщений об ошибках:
    - `message`: общее сообщение об ошибке.
    - `errors`: карта с перечнем ошибок по полям.
    - `exceptionName`: имя исключения.

- **CardResponse**: DTO для данных карты:
    - `number`: номер карты.
    - `expirationDate`: дата истечения.
    - `status`: статус карты.
    - `balance`: баланс карты.
    - `ownerId`: ID владельца.

- **BlockRequestResponse**: DTO для данных о запросе на блокировку:
    - `requestId`: ID запроса.
    - `cardNumber`: номер карты.
    - `requesterLogin`: логин запрашивающего.
    - `status`: статус запроса.
    - `requestDate`: дата создания запроса.
    - `processedDate`: дата обработки.
    - `reason`: причина запроса.

# Логика сущностей

- **Role**: Сущность для ролей пользователей:
    - `roleId`: уникальный ID (UUID).
    - `name`: имя роли (из `RoleNameEnum`, уникальное).
    - `users`: множество связанных пользователей (многие-ко-многим).

- **User**: Сущность пользователя, реализующая `UserDetails` для Spring Security:
    - `userId`: уникальный ID (UUID).
    - `login`: уникальный логин.
    - `passwordHash`: хэш пароля.
    - `cards`: список карт пользователя (один-ко-многим).
    - `blockRequests`: список запросов на блокировку (один-ко-многим).
    - `roles`: множество ролей (многие-ко-многим).
    - Методы `UserDetails`: возвращают роли как `GrantedAuthority`, логин, пароль и флаги состояния аккаунта.

- **BlockRequest**: Сущность для запросов на блокировку карты:
    - `requestId`: уникальный ID (UUID).
    - `cardNumber`: номер карты.
    - `requester`: пользователь, подавший запрос (многие-к-одному).
    - `status`: статус запроса (из `BlockRequestStatus`).
    - `requestDate`: дата создания запроса.
    - `processedDate`: дата обработки (опционально).
    - `reason`: причина запроса (опционально).

- **Card**: Сущность банковской карты:
    - `number`: номер карты (16 цифр, первичный ключ).
    - `expirationDate`: дата истечения (тип `YearMonth`).
    - `status`: статус карты (из `CardStatusEnum`).
    - `balance`: баланс карты (с точностью до 2 знаков).
    - `owner`: владелец карты (многие-к-одному, связь с `User`).

# Логика enums

- **BlockRequestStatus**: Статусы запроса на блокировку карты:
    - `PENDING`: запрос ожидает обработки.
    - `APPROVED`: запрос одобрен.
    - `REJECTED`: запрос отклонен.
    - `PROCESSED`: запрос обработан.

- **RoleNameEnum**: Роли пользователей:
    - `ROLE_ADMIN`: администратор.
    - `ROLE_USER`: пользователь.

- **CardStatusEnum**: Статусы банковской карты:
    - `ACTIVE`: карта активна.
    - `BLOCKED`: карта заблокирована.
    - `EXPIRED`: срок действия карты истек.

# Логика мапперов

- **CardMapper**: Преобразование данных для карт:
    - `toResponse`: Из `Card` в `CardResponse`, маскирует номер карты, включает дату истечения, статус, баланс и ID владельца.
    - `toEntity`: Из `CardCreateRequest` и `User` в `Card`, устанавливает статус `ACTIVE` и нулевой баланс.
    - `toBalanceResponse`: Из `Card` в `BalanceResponse`, содержит замаскированный номер карты и баланс.

- **BlockRequestMapper**: Преобразование данных для запросов на блокировку:
    - `toResponse`: Из `BlockRequest` в `BlockRequestResponse`, включает ID запроса, замаскированный номер карты, логин запрашивающего, статус, даты запроса/обработки и причину.

- **UserMapper**: Преобразование данных для пользователей:
    - `toResponse`: Из `User` в `UserResponse`, включает логин, список замаскированных номеров карт, имена ролей и статусы запросов на блокировку.
    - `toEntity`: Из `UserCreateRequest` в `User`, кодирует пароль с помощью `PasswordEncoder`.

# Логика сервисов

- **UserService** / **UserServiceImpl**: Управление пользователями:
    - `getAll`: Получение списка пользователей с пагинацией, преобразование в `UserResponse`.
    - `getById`: Получение пользователя по ID, возврат `UserResponse` или исключение `UserNotFoundException`.
    - `create`: Создание пользователя из `UserCreateRequest`, добавление роли `ROLE_USER`, проверка уникальности логина, возврат ID.
    - `update`: Обновление пароля пользователя по `UserPasswordUpdateRequest`, проверка старого пароля, сохранение.
    - `delete`: Удаление пользователя по ID, проверка существования.

- **TransferService** / **TransferServiceImpl**: Обработка переводов:
    - `transfer`: Выполнение перевода из `TransferRequest`, проверка:
        - Существование карт (`numberFrom`, `numberTo`).
        - Принадлежность карт одному владельцу.
        - Статус карт (`ACTIVE`).
        - Положительность суммы.
        - Достаточность средств.
        - Обновление балансов и сохранение.

- **CardService** / **CardServiceImpl**: Управление картами:
    - `getAll`: Получение списка карт с пагинацией, возврат `CardResponse`.
    - `getByNumber`: Получение карты по номеру, возврат `CardResponse`.
    - `create`: Создание карты из `CardCreateRequest`, проверка владельца, сохранение.
    - `block`: Блокировка карты (статус `BLOCKED`).
    - `activate`: Активация карты (статус `ACTIVE` или `EXPIRED` при истечении срока).
    - `delete`: Удаление карты по номеру, проверка существования.
    - `blockRequest`: Создание запроса на блокировку карты (`PENDING`, текущая дата, причина).
    - `getBalance`: Получение баланса карты, возврат `BalanceResponse`.
    - `updateExpiredCards`: Обновление статуса истекших карт на `EXPIRED`.

- **BlockRequestService** / **BlockRequestServiceImpl**: Управление запросами на блокировку:
    - `getAll`: Получение списка запросов с пагинацией, возврат `BlockRequestResponse`.
    - `getByStatus`: Получение запросов по статусу с пагинацией.
    - `approve`: Одобрение запроса, установка статуса `APPROVED` и текущей даты обработки.
    - `reject`: Отклонение запроса, установка статуса `REJECTED` и текущей даты обработки.

# Логика репозиториев

- **RoleRepository**: Репозиторий для работы с ролями:
  - Наследует `JpaRepository<Role, UUID>`.
  - `findByName`: Поиск роли по `RoleNameEnum`.

- **CardRepository**: Репозиторий для работы с картами:
  - Наследует `JpaRepository<Card, String>` (ID — номер карты).
  - `findByNumber`: Поиск карты по номеру.
  - `findExpiredCards`: Поиск карт с истекшим сроком действия (до указанного `YearMonth`).
  - `existsByNumber`: Проверка существования карты по номеру.

- **BlockRequestRepository**: Репозиторий для запросов на блокировку:
  - Наследует `JpaRepository<BlockRequest, UUID>`.
  - `findAllWithRequester`: Получение всех запросов с данными запрашивающего, с пагинацией, сортировка по дате запроса (убывание).
  - `findByStatusWithRequester`: Получение запросов по статусу с данными запрашивающего, с пагинацией, сортировка по дате запроса.

- **UserRepository**: Репозиторий для работы с пользователями:
  - Наследует `JpaRepository<User, UUID>`.
  - `existsByLogin`: Проверка существования пользователя по логину.
  - `findByLogin`: Поиск пользователя по логину.

# Логика утилитных классов

- **CardUtil**: Утилитные методы для работы с картами:
  - `maskCardNumber`: Маскирует номер карты, оставляя последние 4 цифры (требует 16-значный номер, иначе исключение).
  - `isExpired`: Проверяет, истек ли срок действия карты (`YearMonth` раньше текущего).

- **ValidationValues**: Константы для валидации:
  - `Page.LIMIT_DEFAULT_VALUE`: Значение по умолчанию для размера страницы — "10".
  - `Page.OFFSET_DEFAULT_VALUE`: Значение по умолчанию для смещения страницы — "0".

- **YearMonthConverter**: Конвертер для `YearMonth` в базе данных:
  - Реализует `AttributeConverter<YearMonth, String>`.
  - `convertToDatabaseColumn`: Преобразует `YearMonth` в строку формата "yyyy-MM".
  - `convertToEntityAttribute`: Преобразует строку "yyyy-MM" в `YearMonth`.

# Логика планировщика

- **ExpiredCardScheduler**: Планировщик для обработки истекших карт:
  - Активируется, если свойство `card.expired.scheduler.enabled` равно `true`.
  - `processExpiredCards`: Выполняется ежемесячно в 02:00 (по cron `0 0 2 1 * ?`).
    - Вызывает `CardService.updateExpiredCards()` для обновления статуса истекших карт.
    - Логирует начало и успешное завершение процесса, а также ошибки с их деталями.

# Логика конфигурационных классов

- **SchedulingConfig**: Конфигурация планировщика:
  - Активирует поддержку планирования задач (`@EnableScheduling`).

- **SecurityConfig**: Конфигурация безопасности:
  - Включает веб-безопасность (`@EnableWebSecurity`) и безопасность методов (`@EnableMethodSecurity`).
  - `securityFilterChain`: Настройка HTTP-безопасности:
    - Отключает CSRF.
    - Разрешает доступ без аутентификации к `/api/auth/**`, `/v3/api-docs/**`, `/swagger-ui/**`, `/swagger-resources/**`.
    - Требует аутентификацию для всех остальных запросов.
    - Использует stateless-сессии (`SessionCreationPolicy.STATELESS`).
    - Добавляет `JwtAuthenticationFilter` перед `UsernamePasswordAuthenticationFilter`.
  - `userDetailsService`: Загружает пользователя по логину из `UserRepository`, выбрасывает `UsernameNotFoundException` при отсутствии.
  - `authenticationProvider`: Настраивает `DaoAuthenticationProvider` с `UserDetailsService` и `PasswordEncoder`.
  - `authenticationManager`: Предоставляет менеджер аутентификации из `AuthenticationConfiguration`.
  - `passwordEncoder`: Использует `BCryptPasswordEncoder` для хэширования паролей.

# Логика классов безопасности

- **JwtAuthenticationFilter**: Фильтр для обработки JWT-аутентификации:
  - Проверяет заголовок `Authorization` на наличие токена (`Bearer`).
  - Извлекает логин из токена через `JwtService`.
  - Загружает `UserDetails` через `UserDetailsService` и проверяет валидность токена.
  - При валидном токене создает `UsernamePasswordAuthenticationToken` и устанавливает его в `SecurityContextHolder`.
  - Пропускает запрос через фильтр, если токен отсутствует или невалиден.

- **AuthenticationService**: Сервис для аутентификации:
  - `authenticate`: Принимает `AuthenticationRequest`, выполняет аутентификацию через `AuthenticationManager`.
  - Находит пользователя по логину в `UserRepository`.
  - Генерирует JWT-токен через `JwtService` и возвращает `AuthenticationResponse` с токеном.

- **JwtService**: Сервис для работы с JWT-токенами:
  - `extractUsername`: Извлекает логин из токена.
  - `extractClaim`: Извлекает указанное поле из токена.
  - `generateToken`: Генерирует токен для `UserDetails` с дополнительными claims, устанавливает `subject`, дату выпуска и истечения (из `jwt.expiration`), подписывает с помощью `secretKey`.
  - `isTokenValid`: Проверяет, совпадает ли логин токена с `UserDetails` и не истек ли срок действия.
  - `extractAllClaims`: Парсит токен и возвращает все claims, используя секретный ключ.
  - `getSignInKey`: Декодирует секретный ключ из Base64 для подписи токена.

# Логика обработки исключений

- **GlobalExceptionHandler**: Обработчик исключений для REST API:
  - `MethodArgumentTypeMismatchException`: Обрабатывает ошибки преобразования аргументов метода, возвращает `ExceptionMessage` с HTTP 400.
  - `HttpMessageNotReadableException`: Обрабатывает ошибки чтения тела запроса, возвращает `ExceptionMessage` с HTTP 400.
  - `HandlerMethodValidationException`: Обрабатывает ошибки валидации параметров метода, возвращает `ExceptionMessage` с деталями ошибок и HTTP 400.
  - `MethodArgumentNotValidException`: Обрабатывает ошибки валидации DTO, возвращает `ExceptionMessage` с деталями ошибок полей и HTTP 400.
  - `ServiceException`: Обрабатывает исключения уровня сервиса, возвращает `ExceptionMessage` с динамическим HTTP статусом из исключения.
  - `BadCredentialsException`: Обрабатывает ошибки аутентификации, возвращает `ExceptionMessage` с HTTP 401.
  - `AccessDeniedException`: Обрабатывает ошибки доступа, возвращает `ExceptionMessage` с сообщением "Access denied" и HTTP 403.
  - `Exception`: Ловит все непредусмотренные исключения, логирует стек-трейс, возвращает `ExceptionMessage` с HTTP 500.

- **ServiceException**: Базовое исключение сервиса:
  - Содержит сообщение и HTTP статус.
  - Используется как родитель для кастомных исключений сервиса.

- **BadCredentialsException**: Исключение для неверных учетных данных:
  - Наследует `RuntimeException`, содержит только сообщение.

- **BlockRequestNotFoundException**: Исключение для ненайденного запроса на блокировку:
  - Наследует `ServiceException`, возвращает HTTP 404.

- **UserAlreadyExistsException**: Исключение для существующего пользователя:
  - Наследует `ServiceException`, возвращает HTTP 409.

- **UserNotFoundException**: Исключение для ненайденного пользователя:
  - Наследует `ServiceException`, возвращает HTTP 404.

- **TransferException**: Исключение для ошибок перевода:
  - Наследует `ServiceException`, возвращает HTTP 400.

- **CardNotFoundException**: Исключение для ненайденной карты:
  - Наследует `ServiceException`, возвращает HTTP 404.

- **RoleNotFoundException**: Исключение для ненайденной роли:
  - Наследует `ServiceException`, возвращает HTTP 404.

# Логика тестов

- **CardControllerTest**: Тестирование `CardController` с использованием `MockMvc`:
  - `getCards`: Проверяет возврат списка карт (HTTP 200, проверка номера карты и ID владельца).
  - `getCardByNumber`: Проверяет получение карты по номеру (HTTP 200, проверка номера и баланса).
  - `createCard`: Проверяет создание карты (HTTP 201, валидный запрос).
  - `blockCard`: Проверяет блокировку карты (HTTP 200).
  - `activateCard`: Проверяет активацию карты (HTTP 200).
  - `deleteCard`: Проверяет удаление карты (HTTP 204).
  - `getCardBalance`: Проверяет получение баланса карты (HTTP 200, проверка номера и баланса).
  - `blockCardRequest`: Проверяет создание запроса на блокировку (HTTP 200).
  - `getAllBlockRequests`: Проверяет получение списка запросов на блокировку (HTTP 200, проверка номера карты и логина).
  - `getBlockRequestsByStatus`: Проверяет получение запросов по статусу (HTTP 200, проверка статуса и логина).
  - `approveBlockRequest`: Проверяет одобрение запроса на блокировку (HTTP 204).
  - `rejectBlockRequest`: Проверяет отклонение запроса на блокировку (HTTP 204).
  - `createCard_ShouldReturnBadRequest_WhenInvalidInput`: Проверяет ошибку при невалидном запросе (HTTP 400, наличие ошибок).

- **TransferServiceTest**: Тестирование `TransferServiceImpl` с использованием Mockito:
  - `transfer_ShouldTransferAmount_WhenConditionsAreMet`: Проверяет успешный перевод (обновление балансов, вызов `save`).
  - `transfer_ShouldThrowException_WhenCardsHaveDifferentOwners`: Проверяет исключение при разных владельцах карт (`TransferException`).
  - `transfer_ShouldThrowException_WhenSourceCardIsNotActive`: Проверяет исключение, если исходная карта не активна (`TransferException`).
  - `transfer_ShouldThrowException_WhenDestinationCardIsNotActive`: Проверяет исключение, если целевая карта не активна (`TransferException`).
  - `transfer_ShouldThrowException_WhenInsufficientFunds`: Проверяет исключение при недостатке средств (`TransferException`).
  - `transfer_ShouldThrowException_WhenSourceCardNotFound`: Проверяет исключение, если исходная карта не найдена (`CardNotFoundException`).
  - `transfer_ShouldThrowException_WhenDestinationCardNotFound`: Проверяет исключение, если целевая карта не найдена (`CardNotFoundException`).
  - `transfer_ShouldThrowException_WhenAmountIsZero`: Проверяет исключение при нулевой сумме (`TransferException`).
  - `transfer_ShouldThrowException_WhenAmountIsNegative`: Проверяет исключение при отрицательной сумме (`TransferException`).

- **UserControllerTest**: Тестирование `UserController` с использованием `MockMvc`:
  - `getUsers`: Проверяет получение списка пользователей (HTTP 200, проверка логина, карт, ролей).
  - `getUserById`: Проверяет получение пользователя по ID (HTTP 200, проверка логина и карт).
  - `getUserById_ShouldReturnNotFound_WhenUserDoesNotExist`: Проверяет ошибку, если пользователь не найден (HTTP 404).
  - `createUser`: Проверяет создание пользователя (HTTP 201, возвращает ID).
  - `createUser_ShouldReturnBadRequest_WhenInvalidInput`: Проверяет ошибку при невалидном запросе (HTTP 400).
  - `createUser_ShouldReturnConflict_WhenUserAlreadyExists`: Проверяет ошибку при существующем пользователе (HTTP 409).
  - `updateUser`: Проверяет обновление пароля (HTTP 204).
  - `updateUser_ShouldReturnNotFound_WhenUserDoesNotExist`: Проверяет ошибку, если пользователь не найден (HTTP 404).
  - `updateUser_ShouldReturnBadRequest_WhenInvalidInput`: Проверяет ошибку при невалидном запросе (HTTP 400).
  - `deleteUser`: Проверяет удаление пользователя (HTTP 204).
  - `deleteUser_ShouldReturnNotFound_WhenUserDoesNotExist`: Проверяет ошибку, если пользователь не найден (HTTP 404).

- **TransferControllerTest**: Тестирование `TransferController` с использованием `MockMvc`:
  - `transfer_ShouldReturnOk_WhenTransferIsSuccessful`: Проверяет успешный перевод (HTTP 200).
  - `transfer_ShouldReturnBadRequest_WhenInputIsInvalid`: Проверяет ошибку при невалидном запросе (HTTP 400).
  - `transfer_ShouldReturnNotFound_WhenCardNotFound`: Проверяет ошибку, если карта не найдена (HTTP 404).

- **CardServiceTest**: Тестирование `CardServiceImpl` с использованием Mockito:
  - `getAll`: Проверяет получение списка карт (возвращает `CardResponse`, проверяет пагинацию).
  - `getByNumber`: Проверяет получение карты по номеру (возвращает `CardResponse`).
  - `getByNumber_ShouldThrowException_WhenCardDoesNotExist`: Проверяет исключение, если карта не найдена (`CardNotFoundException`).
  - `create`: Проверяет создание карты при существующем пользователе.
  - `create_ShouldThrowException_WhenUserDoesNotExist`: Проверяет исключение, если пользователь не найден (`UserNotFoundException`).
  - `block`: Проверяет блокировку карты (устанавливает `BLOCKED`).
  - `block_ShouldThrowException_WhenCardDoesNotExist`: Проверяет исключение, если карта не найдена (`CardNotFoundException`).
  - `activate`: Проверяет активацию карты (устанавливает `ACTIVE`, если не истек срок).
  - `activate_ShouldSetExpired_WhenCardExistsAndExpired`: Проверяет установку `EXPIRED` для истекшей карты.
  - `activate_ShouldThrowException_WhenCardDoesNotExist`: Проверяет исключение, если карта не найдена (`CardNotFoundException`).
  - `delete`: Проверяет удаление карты.
  - `delete_ShouldThrowException_WhenCardDoesNotExist`: Проверяет исключение, если карта не найдена (`CardNotFoundException`).
  - `blockRequest`: Проверяет создание запроса на блокировку (`PENDING`).
  - `blockRequest_ShouldThrowException_WhenCardDoesNotExist`: Проверяет исключение, если карта не найдена (`CardNotFoundException`).
  - `getBalance`: Проверяет получение баланса карты.
  - `getBalance_ShouldThrowException_WhenCardDoesNotExist`: Проверяет исключение, если карта не найдена (`CardNotFoundException`).
  - `updateExpiredCards`: Проверяет обновление статуса истекших карт на `EXPIRED`.

- **BlockRequestServiceTest**: Тестирование `BlockRequestServiceImpl` с использованием Mockito:
  - `getAll`: Проверяет получение списка запросов на блокировку (возвращает `BlockRequestResponse`).
  - `getByStatus`: Проверяет получение запросов по статусу.
  - `getByStatus_ShouldThrowException_WhenInvalidStatus`: Проверяет исключение при невалидном статусе (`IllegalArgumentException`).
  - `approve`: Проверяет одобрение запроса (устанавливает `APPROVED`, дату обработки).
  - `approve_ShouldThrowException_WhenRequestDoesNotExist`: Проверяет исключение, если запрос не найден (`BlockRequestNotFoundException`).
  - `reject`: Проверяет отклонение запроса (устанавливает `REJECTED`, дату обработки).
  - `reject_ShouldThrowException_WhenRequestDoesNotExist`: Проверяет исключение, если запрос не найден (`BlockRequestNotFoundException`).

- **UserServiceTest**: Тестирование `UserServiceImpl` с использованием Mockito:
  - `getAll`: Проверяет получение списка пользователей (возвращает `UserResponse`).
  - `getById`: Проверяет получение пользователя по ID.
  - `getById_ShouldThrowException_WhenUserDoesNotExist`: Проверяет исключение, если пользователь не найден (`UserNotFoundException`).
  - `create`: Проверяет создание пользователя с уникальным логином (добавляет роль `ROLE_USER`).
  - `create_ShouldThrowException_WhenLoginAlreadyExists`: Проверяет исключение, если логин занят (`UserAlreadyExistsException`).
  - `create_ShouldThrowException_WhenDefaultRoleNotFound`: Проверяет исключение, если роль `ROLE_USER` не найдена (`RoleNotFoundException`).
  - `update`: Проверяет обновление пароля при существующем пользователе.
  - `update_ShouldThrowException_WhenUserDoesNotExist`: Проверяет исключение, если пользователь не найден (`UserNotFoundException`).
  - `update_ShouldUpdatePassword_WhenOldPasswordIsCorrect`: Проверяет обновление пароля при правильном старом пароле.
  - `update_ShouldNotUpdatePassword_WhenOldPasswordIsInvalid`: Проверяет отсутствие обновления при неверном старом пароле.
  - `delete`: Проверяет удаление пользователя.
  - `delete_ShouldThrowException_WhenUserDoesNotExist`: Проверяет исключение, если пользователь не найден (`UserNotFoundException`).
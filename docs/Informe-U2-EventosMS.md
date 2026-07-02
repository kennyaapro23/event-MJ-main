# Plantilla de Informe — Proyecto Final de Microservicios

**Referencia de plantilla:** [informe-template](https://261dist.github.io/ecom/informe-template/)

| Campo | Valor |
|-------|-------|
| **Universidad** | Universidad Peruana Unión |
| **Curso** | Microservicios |
| **Docente** | Abel Angel Sullon Macalupu |
| **Equipo** | Mario Miguel Soto Zea, Josimar Yoseph Huarilloclla |
| **Proyecto** | EventosMS — Sistema de Gestión de Eventos |
| **Fecha** | 04 de junio de 2026 — Juliaca, Perú |

---

## 1. Descripción del Proyecto

**EventosMS** es un sistema backend para la gestión integral de eventos académicos, corporativos o comunitarios. Resuelve la necesidad de centralizar la información de eventos y sus sesiones, el registro de participantes y el control de inscripciones o asistencias, evitando bases de datos monolíticas y acoplamiento fuerte entre dominios.

El dominio se divide en cuatro áreas: **autenticación y usuarios** (quién organiza o accede al sistema), **eventos y sesiones** (qué se celebra y en qué franjas), **participantes** (quién asiste) y **registros de asistencia** (vinculación participante–evento con observaciones y marca temporal). Cada área vive en un microservicio con su propia base de datos MySQL, siguiendo el patrón *database per service*.

El **flujo principal de negocio** es el siguiente: un usuario se autentica en el API Gateway y obtiene un JWT; un organizador crea un evento (validando su identidad contra el servicio de autenticación vía OpenFeign); define sesiones asociadas al evento; se registran participantes en el sistema; y finalmente se crea un registro de asistencia que asocia un participante con un evento, enriqueciendo la respuesta con los datos del participante obtenidos por Feign. Opcionalmente, al crear un participante se publica un mensaje en Kafka para desacoplar notificaciones futuras.

Los **actores** del sistema son: **Administrador** (`ROLE_ADMIN`), con acceso amplio; **Organizador** (`ROLE_ORGANIZER` / usuario registrado), responsable de crear y modificar sus eventos; **Usuario autenticado** (`ROLE_USER`), que consume APIs protegidas; y **Clientes externos** (aplicaciones, Postman, curl o un futuro frontend) que consumen la API única expuesta por el Gateway en el puerto 9095.

La solución técnica se apoya en **Spring Boot 3.5**, **Spring Cloud** (Config Server, Eureka, Gateway), **Docker Compose** para orquestación, **Kafka** para mensajería asíncrona inicial y el stack **Prometheus + Loki + Grafana** para observabilidad.

---

## 2. Arquitectura

### 2.1 Diagrama de Arquitectura

```mermaid
graph TD
    Cliente[Cliente / API REST]
    GW[ms-gateway-server :9095]
    CFG[ms-config-server :7075]
    EUREKA[ms-registry-server :8095]

    AUTH[ms-auth-service :8099]
    EVT[ms-events-service :8096]
    PART[ms-participants-service :8097]
    REG[ms-registro-service :8098]

    DB_AUTH[(MySQL ms_auth)]
    DB_EVT[(MySQL ms_evento)]
    DB_PART[(MySQL ms_participants)]
    DB_REG[(MySQL ms_registro)]

    KAFKA[Kafka :9092]
    PROM[Prometheus :9090]
    LOKI[Loki :3100]
    GRAF[Grafana :3000]
    PROMTAIL[Promtail]

    Cliente --> GW
    GW --> AUTH
    GW --> EVT
    GW --> PART
    GW --> REG

    GW --> EUREKA
    AUTH --> EUREKA
    EVT --> EUREKA
    PART --> EUREKA
    REG --> EUREKA

    GW -.-> CFG
    AUTH -.-> CFG
    EVT -.-> CFG
    PART -.-> CFG
    REG -.-> CFG

    AUTH --> DB_AUTH
    EVT --> DB_EVT
    PART --> DB_PART
    REG --> DB_REG

    REG -->|OpenFeign| PART
    EVT -->|OpenFeign| AUTH

    PART -->|Producer| KAFKA
    PART -->|Consumer| KAFKA

    PROM --> GW
    PROM --> AUTH
    PROM --> EVT
    PROM --> PART
    PROM --> REG

    PROMTAIL --> LOKI
    GRAF --> PROM
    GRAF --> LOKI
```

> **Nota:** Las sesiones (`/sessions`) están implementadas en `ms-events-service` pero **no** están enrutadas por el Gateway; el acceso vía API unificada requiere añadir una ruta o usar el puerto 8096 directamente.

### 2.2 Tecnologías

| Componente | Tecnología | Versión |
|------------|------------|---------|
| Lenguaje / Runtime | Java | 17 |
| Framework | Spring Boot | 3.5.4 – 3.5.6 (por módulo) |
| Cloud | Spring Cloud | 2025.0.0 |
| API Gateway | Spring Cloud Gateway (WebFlux) | (BOM Spring Cloud) |
| Config Server | Spring Cloud Config (native) | (BOM Spring Cloud) |
| Service Registry | Netflix Eureka | (BOM Spring Cloud) |
| Comunicación síncrona | OpenFeign + Resilience4j | (BOM Spring Cloud) |
| Base de datos | MySQL | 8.x (host local) |
| ORM | Spring Data JPA / Hibernate | (Spring Boot) |
| Mensajería | Apache Kafka (KRaft, sin Zookeeper) | 3.8.0 |
| Seguridad | JWT (jjwt) + filtro global en Gateway | — |
| Documentación API | SpringDoc OpenAPI / Swagger UI agregado | — |
| Frontend | No implementado | N/A |
| Contenedores | Docker + Docker Compose | — |
| Observabilidad | Prometheus, Loki, Promtail, Grafana | v2.54.1 / 3.1.1 / 11.2.0 |
| Build | Maven (multi-módulo) | — |

### 2.3 Puertos y Naming

| Servicio | Nombre interno (Eureka / DNS Docker) | Puerto interno | Puerto host (prod / dev) |
|----------|--------------------------------------|----------------|---------------------------|
| Config Server | `config-server` | 7075 | 7075 |
| Eureka Registry | `registry-server` | 8095 | 8095 |
| API Gateway | `ms-gateway-server` | 9095 | 9095 |
| Eventos | `ms-events-service` | 8096 | 8096 |
| Participantes | `ms-participants-service` | 8097 | dinámico (scale) |
| Registro | `ms-registro-service` | 8098 | — (solo red Docker) |
| Autenticación | `ms-auth-service` | 8099 | 8099 |
| Kafka | `kafka` | 9092 | 9092 |
| Prometheus | `prometheus` | 9090 | 9090 |
| Grafana | `grafana` | 3000 | 3000 |
| Loki | `loki` | 3100 | 3100 |
| Promtail | `promtail` | 9080 | — |

---

## 3. Microservicios

### 3.1 Lista de servicios

| Servicio | Responsabilidad | Base de datos | Dependencias |
|----------|-----------------|---------------|--------------|
| `ms-config-server` | Configuración centralizada (`config-data/`) | — | — |
| `ms-registry-server` | Service discovery (Eureka) | — | Config Server |
| `ms-gateway-server` | Punto de entrada, rutas, validación JWT, Swagger agregado | — | Config, Eureka |
| `ms-auth-service` | Login, registro, emisión JWT, consulta de usuarios/organizadores | `ms_auth` | Config, Eureka, MySQL |
| `ms-events-service` | CRUD eventos y sesiones, filtros y paginación | `ms_evento` | Config, Eureka, MySQL, Auth (Feign) |
| `ms-participants-service` | CRUD participantes, publicación/consumo Kafka | `ms_participants` | Config, Eureka, MySQL, Kafka |
| `ms-registro-service` | Inscripciones/asistencia participante–evento | `ms_registro` | Config, Eureka, MySQL, Participants (Feign) |

### 3.2 Interfaces entre servicios

| Origen | Destino | Tipo | Endpoint / recurso | Resiliencia |
|--------|---------|------|-------------------|-------------|
| Cliente | `ms-gateway-server` | HTTP REST | `/auth/**`, `/events/**`, `/participants/**`, `/registros/**` | — |
| `ms-gateway-server` | `ms-auth-service` | HTTP (lb Eureka) | Rutas `/auth/**` | — |
| `ms-gateway-server` | `ms-events-service` | HTTP (lb Eureka) | Rutas `/events/**` | — |
| `ms-gateway-server` | `ms-participants-service` | HTTP (lb Eureka) | Rutas `/participants/**` | — |
| `ms-gateway-server` | `ms-registro-service` | HTTP (lb Eureka) | Rutas `/registros/**` | — |
| `ms-registro-service` | `ms-participants-service` | **OpenFeign** | `GET /participants/{id}` | Circuit breaker `participanteListarPorIdCB` |
| `ms-events-service` | `ms-auth-service` | **OpenFeign** | `GET /auth/{id}`, `GET /auth/{id}/exists` | Circuit breaker `organizerClientCB` |
| `ms-participants-service` | Kafka | **Producer** | Tópico `participantes-eventos` | — |
| `ms-participants-service` | Kafka | **Consumer** | Tópico `participantes-eventos`, group `participants-group` | — |

**APIs principales expuestas (vía Gateway):**

| Método | Ruta (gateway) | Servicio | Descripción |
|--------|----------------|----------|-------------|
| POST | `/auth/login` | Auth | Obtener JWT |
| POST | `/auth/register` | Auth | Registrar usuario |
| GET | `/auth/{id}` | Auth | Usuario por id |
| GET/POST/PUT/DELETE | `/events/**` | Eventos | Gestión de eventos |
| GET/POST/PUT/DELETE | `/participants/**` | Participantes | Gestión de participantes |
| POST | `/registros/participant/{id}` | Registro | Crear asistencia |
| GET/DELETE | `/registros/**` | Registro | Consultar / eliminar |

**Sesiones** (`/sessions/**`): implementadas en `ms-events-service`, acceso directo puerto **8096** (limitación actual del Gateway).

---

## 4. Seguridad

### Dónde se valida el token

La validación del JWT se realiza en el **API Gateway**, mediante el filtro global `JwtAuthenticationFilter`. El gateway verifica la firma HMAC del token usando la propiedad `jwt.secret` (compartida con `ms-auth-service`, vía Spring Cloud Config).

### Cómo se protegen las rutas

- **Rutas públicas** (sin `Authorization: Bearer`): `/auth/login`, `/auth/register`, rutas que empiezan por `/openapi`, `/swagger-ui`, `/v3/api-docs` y `/actuator`.
- **Resto de rutas:** exigen cabecera `Authorization: Bearer <token>` válido; si falta o es inválido → **401 Unauthorized**.

### Validación directa en microservicios

| Microservicio | ¿Valida JWT? |
|---------------|--------------|
| `ms-gateway-server` | **Sí** (filtro global) |
| `ms-auth-service` | No en APIs de negocio (Spring Security en login/register) |
| `ms-events-service` | **No** |
| `ms-participants-service` | **No** |
| `ms-registro-service` | **No** |

> **Implicación:** el acceso directo a puertos de microservicios (p. ej. 8096, 8099) **omite** el filtro del Gateway. En producción se debe restringir la red o añadir validación en cada servicio.

### Roles

- Roles almacenados en JWT y en BD: `ROLE_ADMIN`, `ROLE_USER` (registro por defecto), y uso previsto de `ROLE_ORGANIZER` en `SessionController` vía cabecera `X-User-Roles`.
- El Gateway **no propaga** claims del JWT a los microservicios; algunos endpoints de eventos exigen manualmente `X-Organizer-Id` o `X-User-Roles`.
- Usuario semilla: `admin` / `admin123` (`DataInitializer`).

---

## 5. Despliegue

### 5.1 Estructura del repositorio

```
EventosMS/
├── pom.xml                    # Parent Maven (módulos)
├── docker-compose.yml         # Orquestación completa
├── config-data/               # YAML para Spring Cloud Config (native)
│   ├── application.yml
│   ├── ms-gateway-server-prod.yml
│   ├── ms-events-service-prod.yml
│   ├── ms-participants-service-prod.yml
│   ├── ms-registro-service-prod.yml
│   └── ms-auth-service-prod.yml
├── ms-config-server/
├── ms-registry-server/
├── ms-gateway-server/
├── ms-auth-service/
├── ms-evento-service/
├── ms-participants/
├── ms-registro-service/
├── obs/                       # Observabilidad
│   ├── prometheus/
│   ├── loki/
│   ├── promtail/
│   └── grafana/
├── docs/
│   └── Informe-U2-EventosMS.md
└── README.md
```

> La plantilla del curso sugiere `infra/`, `services/` y `clients/frontend/`. Este proyecto usa módulos en la raíz; la funcionalidad es equivalente.

### 5.2 Instrucciones de ejecución

**Requisitos**

- JDK **17**
- Maven 3.8+
- Docker Desktop
- MySQL en el host (puerto 3306, usuario `root`, sin contraseña según README)

**Bases de datos**

```sql
CREATE DATABASE ms_evento;
CREATE DATABASE ms_participants;
CREATE DATABASE ms_registro;
CREATE DATABASE ms_auth;
```

**Paso 1 — Compilar**

```bash
mvn clean package -DskipTests
```

**Paso 2 — Levantar el sistema**

```bash
docker compose up -d --build
```

**Paso 3 — Verificar**

```bash
docker compose ps
```

- Eureka: http://localhost:8095  
- Gateway: http://localhost:9095  
- Swagger: http://localhost:9095/swagger-ui.html  

**Paso 4 — Autenticación y llamada protegida**

```bash
curl -X POST http://localhost:9095/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"admin\",\"password\":\"admin123\"}"
```

```bash
curl http://localhost:9095/events \
  -H "Authorization: Bearer <TOKEN>"
```

**Escalado (opcional)**

```bash
docker compose up -d --scale ms-participants=3
```

### 5.3 Variables de entorno

| Variable | Descripción | Ejemplo (Docker Compose) |
|----------|-------------|---------------------------|
| `SPRING_PROFILES_ACTIVE` | Perfil Spring | `prod` |
| `SPRING_CONFIG_IMPORT` | URL Config Server | `optional:configserver:http://root:123456@config-server:7075` |
| `SPRING_DATASOURCE_URL` | JDBC MySQL | `jdbc:mysql://host.docker.internal:3306/ms_evento` |
| `SPRING_DATASOURCE_USERNAME` | Usuario BD | `root` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña BD | `` (vacío en dev) |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Esquema JPA | `update` |
| `EUREKA_URI` / `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | Eureka | `http://registry-server:8095/eureka` |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | Broker Kafka | `kafka:9092` (default en config participants) |
| `jwt.secret` | Firma JWT (Config Server) | Definido en `config-data` |
| `jwt.expiration` | Expiración token (ms) | `3600000` (auth) |
| `SPRING_SECURITY_USER_NAME` | Usuario Config Server | `root` |
| `SPRING_SECURITY_USER_PASSWORD` | Password Config Server | `123456` |

---

## 6. Observabilidad

### 6.1 Métricas

Todos los microservicios de negocio y el Gateway incluyen **Spring Boot Actuator** y **Micrometer Prometheus**. La configuración compartida en `config-data/application.yml` expone: `health`, `info`, `prometheus`, `metrics`.

**Prometheus** (`obs/prometheus/prometheus.yml`) hace scrape cada 10s a:

| Job | Target | Path |
|-----|--------|------|
| gateway-server | `gateway-server:9095` | `/actuator/prometheus` |
| ms-evento-service | `ms-evento-service:8096` | `/actuator/prometheus` |
| ms-participants | `ms-participants:8097` | `/actuator/prometheus` |
| ms-registro-service | `ms-registro-service:8098` | `/actuator/prometheus` |
| ms-auth-service | `ms-auth-service:8099` | `/actuator/prometheus` |

**Visualización:** Grafana (http://localhost:3000, `admin` / `admin`) con datasource Prometheus preconfigurado en `obs/grafana/provisioning/datasources/datasources.yml`. Se recomienda importar dashboards de JVM/Spring Boot o crear paneles para `http_server_requests`, `up`, y JVM memory.

### 6.2 Logs

**Promtail** descubre contenedores Docker vía `docker.sock`, etiqueta logs con `container` y `service` (label Compose), y los envía a **Loki** (`http://loki:3100`). Grafana incluye Loki como segundo datasource para consultas LogQL (ej. `{service="ms-participants"}`).

### 6.3 Alertas

Definidas en `obs/prometheus/alert.rules.yml`:

| Alerta | Condición | Qué detecta |
|--------|-----------|-------------|
| **ServicioCaido** | `up == 0` durante 30s | Target de scrape no responde |
| **TasaErrores5xxAlta** | tasa `http_server_requests` con status 5xx > 0.2/s por 1m | Errores HTTP elevados por aplicación |

### 6.4 Matriz de observabilidad

_Valores verificados con el stack levantado mediante `docker compose up` (ajustar sí/no si en tu máquina difiere):_

| Microservicio | UP en Prometheus | Requests visibles | Errores visibles | Logs en Loki | Alerta definida |
|---------------|------------------|-------------------|------------------|--------------|-----------------|
| ms-gateway-server | sí | sí | sí | sí | sí |
| ms-auth-service | sí | sí | sí | sí | sí |
| ms-events-service | sí | sí | sí | sí | sí |
| ms-participants-service | sí | sí | sí | sí | sí |
| ms-registro-service | sí | sí | sí | sí | sí |
| ms-config-server | no* | no | no | sí | no |
| ms-registry-server | no* | no | no | sí | no |

\*No están en `prometheus.yml`; Promtail puede recolectar sus logs igualmente.

---

## 7. Kafka

### 7.1 Tópicos

| Tópico | Productor | Consumidor | Formato del evento |
|--------|-----------|------------|-------------------|
| `participantes-eventos` | `ms-participants-service` (`EventProducer`) | `ms-participants-service` (`EventConsumer`, group `participants-group`) | `String` (mensaje de texto, ej. `"Nuevo participante creado: " + DTO`) |

### 7.2 Flujo de eventos

1. Cliente autenticado llama `POST /participants` vía Gateway.  
2. `ParticipantController` persiste el participante y llama a `EventProducer.publicar(...)`.  
3. Kafka almacena el mensaje en el tópico `participantes-eventos`.  
4. `EventConsumer` recibe el mensaje y lo registra en log (`log.info`).  

**Estado actual:** patrón *event-driven* en fase inicial; el consumidor no dispara acciones de negocio (notificaciones, sincronización con registro, etc.). Mejora futura: esquema JSON, idempotencia y consumidores en otros servicios.

**Infraestructura:** broker Apache Kafka 3.8.0 en modo KRaft (sin Zookeeper), definido en `docker-compose.yml`.

---

## 8. Pruebas

| Tipo | Herramienta | Cobertura / estado actual |
|------|-------------|---------------------------|
| **Unitarias** | JUnit 5 (potencial) | No implementadas de forma sistemática |
| **Integración** | Spring Boot Test / Testcontainers (potencial) | No implementadas |
| **API / Contract** | Postman, curl, Swagger UI | Pruebas manuales vía Gateway |
| **Carga / estrés** | — | No realizadas |

**Smoke tests existentes:** `@SpringBootTest` de contexto (arranque de aplicación) en:

- `ms-config-server`
- `ms-registry-server`
- `ms-gateway-server`
- `ms-evento-service`
- `ms-participants`
- `ms-registro-service`

**Pendiente:** `ms-auth-service` no incluye tests; no hay informe JaCoCo ni pruebas de Feign/Kafka.

**Plan de mejora sugerido:**

1. Tests unitarios de `AuthService` y `RegistroServiceImpl`.  
2. Test de integración con Testcontainers (MySQL + Kafka).  
3. Colección Postman exportada en `docs/postman/`.  

---

## 9. Lecciones Aprendidas

### Integrante 1: Mario Miguel Soto Zea

1. Comprendí que una arquitectura de microservicios divide responsabilidades y facilita el mantenimiento, pero exige orden en configuración, puertos, comunicación y despliegue.
2. Aprendí la importancia de Eureka y el Gateway para centralizar la entrada y el descubrimiento dinámico de servicios.
3. Entendí que Docker, métricas y logs son necesarios para probar el sistema completo y detectar errores con mayor rapidez.

### Integrante 2: Josimar Yoseph Huarilloclla

1. Aprendí a documentar la comunicación entre microservicios con OpenFeign y a identificar cuándo usar Kafka frente a llamadas síncronas.
2. Entendí que Postman y Swagger son clave para probar el gateway, el JWT y las respuestas 401/200 en rutas protegidas.
3. Reconocí que Prometheus y Grafana permiten verificar que los servicios estén activos antes de la demostración final.

---

## 10. Conclusiones

### Logros

- Arquitectura de microservicios funcional con **Config Server**, **Eureka**, **API Gateway** y cuatro servicios de dominio más autenticación JWT.  
- Separación de bases de datos y comunicación **OpenFeign** con **circuit breakers**.  
- **Docker Compose** para despliegue local reproducible.  
- **Observabilidad** con Prometheus, Loki y Grafana.  
- **Kafka** integrado como primer paso hacia arquitectura orientada a eventos.  

### Dificultades

- MySQL en el host (`host.docker.internal`) complica el despliegue en equipos distintos.  
- Inconsistencia de nombres de BD (`ms_evento` vs `ms_eventos` en config por defecto).  
- Rutas `/sessions` no expuestas en el Gateway.  
- JWT validado solo en el borde; roles no propagados automáticamente.  

### Mejoras futuras

- Frontend web o móvil consumiendo el Gateway.  
- Propagación de JWT/roles (OAuth2 Resource Server o headers desde Gateway).  
- Consumidores Kafka con lógica de negocio y esquema de eventos versionado.  
- Tests automatizados y pipelines CI/CD.  
- MySQL y secretos en contenedores o gestor de secretos.  
- Ruta Gateway para `/sessions/**` y dashboards Grafana preimportados.  

---

## 11. Referencias

- **Repositorio GitHub:** https://github.com/josimarupeu/EventosMS  
- Plantilla del curso: https://261dist.github.io/ecom/informe-template/  
- Documentación Spring Cloud: https://spring.io/projects/spring-cloud  
- Documentación Spring Boot: https://spring.io/projects/spring-boot  

---

## Anexo A — Evidencias (capturas)

_Pegar en el PDF las capturas de pantalla al exportar. Comandos para reproducir:_

| # | Captura | Cómo obtenerla |
|---|---------|----------------|
| 1 | Eureka con servicios UP | Abrir http://localhost:8095 → capturar panel con `MS-AUTH-SERVICE`, `MS-EVENTS-SERVICE`, etc. |
| 2 | Swagger UI | http://localhost:9095/swagger-ui.html |
| 3 | Prometheus targets | http://localhost:9090/targets → todos en estado **UP** |
| 4 | Grafana | http://localhost:3000 (admin/admin) → Explore → Prometheus o Loki |
| 5 | Login con token | Ver comandos en sección 5.2 (respuesta JSON con campo `token`) |
| 6 | 401 vs 200 en `/events` | Sin header → 401; con `Authorization: Bearer <token>` → 200 |

**Comandos para evidencia 5 y 6 (PowerShell):**

```powershell
# Login (guardar token de la respuesta)
curl.exe -X POST http://localhost:9095/auth/login -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"admin123\"}"

# Sin token (debe devolver 401)
curl.exe http://localhost:9095/events

# Con token (reemplazar TOKEN)
curl.exe http://localhost:9095/events -H "Authorization: Bearer TOKEN"
```

---

## Anexo B — Limitaciones conocidas (transparencia técnica)

| Limitación | Impacto |
|------------|---------|
| Sin frontend | Solo API REST |
| `/sessions` sin ruta en Gateway | Acceso indirecto o puerto 8096 |
| Kafka consumer solo loguea | Sin procesamiento asíncrono real |
| `jwt.secret` en repositorio de config | Riesgo en producción |
| Artefactos `target/` en repo (si no ignorados) | Higiene de repositorio |
| Pruebas automatizadas mínimas | Sección 8 con baja cobertura |

---

*Entregable U2 — EventosMS. Equipo: Mario Miguel Soto Zea, Josimar Yoseph Huarilloclla. Docente: Abel Angel Sullon Macalupu.*

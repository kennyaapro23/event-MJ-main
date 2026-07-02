# EventosMS — Cómo levantar todo (backend + frontend Angular)

Sistema de asistencia a eventos: **crear evento → registrar participante → inscribir → pagar → generar asistencia (QR) → check-in**.

El frontend Angular habla **siempre con el API Gateway** (`http://localhost:9095`), que valida el JWT y enruta a cada microservicio. `ms-participants` y `ms-registro` no exponen puerto propio: solo se alcanzan por el gateway.

---

## 0. Requisitos

- **Docker Desktop** corriendo
- **MySQL** en el host (`localhost:3306`, usuario `root`, sin contraseña)
- **Node 22** (ya lo tienes) + Angular CLI 20

## 1. Crear las 6 bases de datos (una sola vez)

```bash
mysql -u root -p < init-databases.sql
```
Crea: `ms_evento`, `ms_participants`, `ms_registro`, `ms_auth`, `ms_pago`, `ms_asistencia`.
(Las tablas se crean solas con `ddl-auto=update` al arrancar cada servicio.)

## 2. Levantar el backend (jars ya compilados)

Desde la raíz del repo:

```bash
docker compose up -d --build
```

Arranca: config-server (7075), eureka (8095), gateway (9095), auth (8099), evento (8096), participants (interno), registro (interno), pago (8097), asistencia (8098), kafka, prometheus/grafana/loki.

> **Importante:** MySQL debe estar arriba **antes** de este paso, o los servicios entran en crash-loop.

Verifica que todo esté registrado en Eureka: **http://localhost:8095** (espera ~30–60 s a que aparezcan todos).

## 3. Levantar el frontend Angular

```bash
cd frontend
npm install     # solo la primera vez (ya viene instalado)
ng serve        # o: npm start
```
Abre **http://localhost:4200**.

## 4. Usar la app

1. **Login** con el usuario sembrado: `admin` / `admin123`.
2. **Eventos → Nuevo evento**: crea uno. `organizerId = 1` (el admin). Fecha de inicio hoy o futura.
3. **Participantes**: agrega un participante (email único).
4. **Inscripciones**: elige participante + evento → inscribir.
5. **Pagos**: registra el pago (usa `registroId`, `participantId`, `eventoId`, monto).
6. **Registrar asistencia**: genera la asistencia → te muestra el **código QR**.
7. **Check-in**: pega el código QR → confirma la asistencia (queda `PRESENTE`).

---

## Qué se cambió para conectar el frontend

**Frontend nuevo** (`frontend/`, Angular 20 standalone):
- Servicios tipados para auth, eventos, participantes, inscripciones, pagos y asistencia (apuntan al gateway `http://localhost:9095`).
- Interceptor JWT (añade `Authorization: Bearer` automáticamente) + guard de rutas.
- Páginas: login, registro, dashboard, eventos (lista + alta), participantes, inscripciones, pagos, registrar asistencia (con QR) y check-in.

**Gateway** (para que el navegador pueda consumir la API):
- `CorsConfig.java`: CORS permitiendo `http://localhost:4200`.
- `JwtAuthenticationFilter`: deja pasar el preflight `OPTIONS`.
- Rutas nuevas `/pagos/**` y `/asistencias/**` (config-data dev y prod).

**Backend robustecido** (trabajo previo):
- `ms-participants`: validación, manejo de errores 404/409, email único, Kafka resiliente.
- `ms-asistencia-service`: validación, excepciones + `GlobalExceptionHandler`, Feign a participants (valida que el participante exista), actuator/prometheus, check-in QR idempotente.

## Si algo falla

- **El navegador bloquea por CORS** → asegúrate de reconstruir el gateway (`docker compose up -d --build gateway-server`) para que tome `CorsConfig`.
- **Gateway devuelve 503** → el servicio destino aún no está registrado en Eureka; espera y reintenta.
- **401 al llamar la API** → el token expiró o no hay sesión; vuelve a hacer login.
- **Servicio no arranca** → revisa `docker compose logs <servicio>`; casi siempre es MySQL abajo o una BD faltante.

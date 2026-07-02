# Comandos para capturas que faltan en el Word

Ejecutar en PowerShell desde `D:\EventosMS-main`.

---

## CAPTURA 5 — Maven (BUILD SUCCESS)

```powershell
cd D:\EventosMS-main
mvn clean package -DskipTests
```

**Captura:** últimas líneas con `BUILD SUCCESS`.

---

## CAPTURA 6 — Docker Compose

```powershell
cd D:\EventosMS-main
docker compose ps
```

**Captura:** todos los servicios en estado `Up` (config-server, registry, gateway, kafka, ms-*, prometheus, grafana).

---

## CAPTURA 2 — Estructura del proyecto

Abrir en el Explorador de archivos:

`D:\EventosMS-main`

**Captura:** carpetas visibles: `ms-config-server`, `ms-gateway-server`, `ms-evento-service`, `ms-participants`, `ms-registro-service`, `ms-auth-service`, `config-data`, `obs`, `docker-compose.yml`.

---

## CAPTURA 11 — Loki en Grafana

1. http://localhost:3000 (admin / admin)
2. Explore → datasource **Loki**
3. Query: `{service="ms-participants"}`
4. Run query
5. Captura si aparecen líneas de log

---

## CAPTURA 15 y 16 — POST evento + GET con datos

### 1) Login (copiar token)

```powershell
curl.exe -X POST http://localhost:9095/auth/login -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"admin123\"}"
```

### 2) POST evento (CAPTURA 15) — reemplazar TOKEN

```powershell
curl.exe -X POST http://localhost:9095/events -H "Content-Type: application/json" -H "Authorization: Bearer TOKEN" -d "{\"name\":\"evento prueba\",\"description\":\"Demo U2\",\"startDate\":\"2026-06-10\",\"endDate\":\"2026-06-10\",\"modality\":\"PRESENTIAL\",\"eventType\":\"FREE\",\"organizerId\":1,\"maxCapacity\":100,\"price\":0}"
```

**Captura:** respuesta **201** con JSON del evento creado.

### 3) GET eventos (CAPTURA 16)

```powershell
curl.exe http://localhost:9095/events -H "Authorization: Bearer TOKEN"
```

**Captura:** `"totalElements": 1` o `content` con el evento.

---

## CAPTURA 1 — Diagrama como imagen

1. Copiar el bloque Mermaid de la sección 2.1 del informe
2. Pegar en https://mermaid.live
3. Export → PNG
4. Insertar en Word donde dice CAPTURA 1

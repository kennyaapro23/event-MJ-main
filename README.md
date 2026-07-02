# EventosMS

Sistema de gestión de eventos (eventos, sesiones, participantes e inscripciones) con arquitectura de microservicios sobre Spring Boot 3.5 y Spring Cloud.

## Arquitectura

| Servicio | Puerto | Descripción |
|---|---|---|
| ms-config-server | 7075 | Configuración centralizada (Spring Cloud Config, modo native) |
| ms-registry-server | 8095 | Service Discovery (Eureka) |
| ms-gateway-server | 9095 | API Gateway + validación JWT |
| ms-auth-service | 8099 | Autenticación y emisión de JWT |
| ms-evento-service | 8096 | Eventos y sesiones |
| ms-participants | interno | Participantes |
| ms-registro-service | interno | Inscripciones |

Observabilidad: Prometheus (9090), Grafana (3000), Loki (3100).

## Requisitos previos

- JDK 17 y Maven (o usar `mvnw`)
- Docker Desktop
- MySQL en el host (puerto 3306, usuario `root` sin contraseña) con estas bases creadas:
```sql
  CREATE DATABASE ms_evento;
  CREATE DATABASE ms_participants;
  CREATE DATABASE ms_registro;
  CREATE DATABASE ms_auth;
```

## Cómo ejecutar

1. Compilar todos los módulos:
```bash
   mvn clean package -DskipTests
```
2. Levantar todo el sistema:
```bash
   docker compose up -d --build
```
3. Verificar:
```bash
   docker compose ps
```

## URLs útiles

- Eureka: http://localhost:8095
- API Gateway: http://localhost:9095
- Swagger (vía gateway): http://localhost:9095/swagger-ui.html
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (admin / admin)

## Seguridad (JWT)

Las rutas del gateway exigen JWT, salvo las públicas (`/auth/login`, `/auth/register`, swagger, actuator).

1. Obtener token (usuario sembrado `admin` / `admin123`):
```bash
   curl -X POST http://localhost:9095/auth/login -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"admin123\"}"
```
2. Usar el token en rutas protegidas:
```bash
   curl http://localhost:9095/events -H "Authorization: Bearer <TOKEN>"
```
Sin token o token inválido → 401.

## Escalado manual

```bash
docker compose up -d --scale ms-participants=3
```
Las instancias se registran en Eureka y se balancean automáticamente."# event-MJ-main" 

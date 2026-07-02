# Datos del equipo — copiar al Word `docuemt dt.docx`

## Portada

| Campo | Valor |
|-------|-------|
| **Universidad** | Universidad Peruana Unión |
| **Curso** | Microservicios |
| **Docente** | Abel Angel Sullon Macalupu |
| **Equipo** | Mario Miguel Soto Zea, Josimar Yoseph Huarilloclla |
| **Proyecto** | EventosMS — Sistema de Gestión de Eventos |
| **Fecha** | 04 de junio de 2026 — Juliaca, Perú |

---

## Sección 9 — Lecciones aprendidas

### Integrante 1: Mario Miguel Soto Zea

1. Comprendí que una arquitectura de microservicios divide responsabilidades y facilita el mantenimiento, pero exige orden en configuración, puertos, comunicación y despliegue.
2. Aprendí la importancia de Eureka y el Gateway para centralizar la entrada y el descubrimiento dinámico de servicios.
3. Entendí que Docker, métricas y logs son necesarios para probar el sistema completo y detectar errores con mayor rapidez.

### Integrante 2: Josimar Yoseph Huarilloclla

1. Aprendí a documentar la comunicación entre microservicios con OpenFeign y a identificar cuándo usar Kafka frente a llamadas síncronas.
2. Entendí que Postman y Swagger son clave para probar el gateway, el JWT y las respuestas 401/200 en rutas protegidas.
3. Reconocí que Prometheus y Grafana permiten verificar que los servicios estén activos antes de la demostración final.

**Borrar** el bloque "Integrante 3" si aún está en el Word.

---

## Sección 11 — Referencias

- **Repositorio GitHub:** https://github.com/josimarupeu/EventosMS
- Plantilla del curso: https://261dist.github.io/ecom/informe-template/
- Spring Boot, Spring Cloud, Docker, Kafka, Prometheus, Grafana

---

## Textos técnicos (si aún no los pegaste)

Ver secciones completas en `docs/Informe-U2-EventosMS.md` (puertos, seguridad, Kafka, conclusiones).

**Puertos clave:** Config 7075, Eureka 8095, Gateway 9095, Eventos 8096, Participantes 8097, Registro 8098, Auth 8099.

**Kafka:** solo tópico `participantes-eventos`.

**Seguridad:** JWT validado en Gateway; rutas públicas login/register/swagger/actuator.

---

## Capturas — pegar en el Word

| # | Estado |
|---|--------|
| 3 Login, 4 401/200, 7 Eureka, 8 Swagger, 9 Grafana, 10 Prometheus, 14 GET, Kafka | Ya hechas |
| 1 Diagrama, 2 Carpetas, 5 Maven, 6 Docker, 11 Loki, 15 POST, 16 éxito | Ver `docs/evidencias/COMANDOS-CAPTURAS-5-6-15-16.md` |

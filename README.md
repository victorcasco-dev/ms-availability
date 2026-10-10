# ms-availability

Microservicio de franjas horarias (`TimeSlot`) para tutorías de tesis.

## Parte #1 — cumplimiento

| Requisito | Implementación |
|-----------|----------------|
| Entidad simple | `TimeSlot` — CRUD en `/time-slots` |
| Segunda entidad simple | `Professor` — solo lectura en `/professor-references` |
| M:N | Gestionado en **ms-users** (`ProfessorResearchLine`); búsqueda de franjas por `researchLineId` |
| Capas | `controller` → `service` → `repository` |
| GET paginado + POST `/search` | Time slots y professor references |
| DELETE lógico | `TimeSlot.cancel()` → estado `CANCELLED` |
| Commons JAR, JPA, Swagger, Logback, excepciones centralizadas | Sí |

Puerto por defecto: **8082**. Variables: ver `.env.example`.

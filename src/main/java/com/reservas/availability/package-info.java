/**
 * Microservicio de disponibilidad horaria ({@link com.reservas.commons.entity.TimeSlot}).
 *
 * <p><b>Parte #1 — entidades:</b></p>
 * <ul>
 *   <li>Simple principal: {@code TimeSlot} (CRUD completo)</li>
 *   <li>Simple de apoyo: {@code Professor} (solo lectura para validar franjas)</li>
 *   <li>M:N del dominio Víctor: gestionado en {@code ms-users} ({@code ProfessorResearchLine});
 *       aquí se consulta vía filtros {@code researchLineId} al buscar franjas</li>
 * </ul>
 */
package com.reservas.availability;

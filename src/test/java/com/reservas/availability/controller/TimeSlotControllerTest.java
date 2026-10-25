package com.reservas.availability.controller;

import com.reservas.commons.dto.TimeSlotPageDto;
import com.reservas.commons.exception.ApiExceptionHandler;
import com.reservas.commons.exception.ResourceNotFoundException;
import com.reservas.availability.dto.request.TimeSlotSearchRequest;
import com.reservas.availability.service.TimeSlotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TimeSlotController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ApiExceptionHandler.class)
class TimeSlotControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TimeSlotService timeSlotService;

	@Test
	void searchByQueryUsesGet() throws Exception {
		when(timeSlotService.search(any(TimeSlotSearchRequest.class)))
				.thenReturn(new TimeSlotPageDto().content(List.of()));

		mockMvc.perform(get("/time-slots?page=0&size=10"))
				.andExpect(status().isOk());
	}

	@Test
	void searchUsesRequestBody() throws Exception {
		when(timeSlotService.search(any(TimeSlotSearchRequest.class)))
				.thenReturn(new TimeSlotPageDto().content(List.of()));

		mockMvc.perform(post("/time-slots/search")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"page\":0,\"size\":10,\"onlyAvailable\":true}"))
				.andExpect(status().isOk());
	}

	@Test
	void missingTimeSlotReturns404() throws Exception {
		UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");
		when(timeSlotService.getById(id)).thenThrow(new ResourceNotFoundException("Time slot not found: " + id));

		mockMvc.perform(get("/time-slots/" + id))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"));
	}

	@Test
	void logicalDeleteReturns204() throws Exception {
		UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");
		doNothing().when(timeSlotService).deleteLogically(id);

		mockMvc.perform(delete("/time-slots/" + id))
				.andExpect(status().isNoContent());
	}
}

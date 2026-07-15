package com.tripagent.accommodation.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tripagent.accommodation.domain.AccommodationType;
import com.tripagent.accommodation.dto.AccommodationResponse;
import com.tripagent.accommodation.service.AccommodationService;
import com.tripagent.common.exception.GlobalExceptionHandler;
import com.tripagent.common.response.PageResponse;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class AccommodationControllerTest {

    private AccommodationService accommodationService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        accommodationService = org.mockito.Mockito.mock(AccommodationService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AccommodationController(accommodationService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void searchAccommodationsReturnsCommonPageResponse() throws Exception {
        when(accommodationService.searchAccommodations(
                AccommodationType.HOTEL,
                "SOUTH",
                "ocean",
                0,
                20
        )).thenReturn(new PageResponse<>(List.of(accommodation()), 0, 20, 1, 1, true, true));

        mockMvc.perform(get("/api/accommodations")
                        .param("type", "HOTEL")
                        .param("region", "SOUTH")
                        .param("keyword", "ocean")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].accommodationId").value(10L))
                .andExpect(jsonPath("$.data.content[0].name").value("Ocean Hotel"))
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    void getAccommodationReturnsCommonSuccessResponse() throws Exception {
        when(accommodationService.getAccommodation(10L)).thenReturn(accommodation());

        mockMvc.perform(get("/api/accommodations/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accommodationId").value(10L))
                .andExpect(jsonPath("$.data.accommodationType").value("HOTEL"));
    }

    @Test
    void getAccommodationReturnsNotFoundResponse() throws Exception {
        when(accommodationService.getAccommodation(999L))
                .thenThrow(new NoSuchElementException("Accommodation not found. accommodationId=999"));

        mockMvc.perform(get("/api/accommodations/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    private AccommodationResponse accommodation() {
        return new AccommodationResponse(
                10L,
                "Ocean Hotel",
                AccommodationType.HOTEL,
                "SOUTH",
                "제주특별자치도 서귀포시 테스트로 1",
                33.25,
                126.56,
                "description",
                "https://example.com/thumbnail.jpg",
                true
        );
    }
}

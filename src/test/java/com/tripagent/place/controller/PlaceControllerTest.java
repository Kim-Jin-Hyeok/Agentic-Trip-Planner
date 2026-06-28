package com.tripagent.place.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tripagent.common.exception.GlobalExceptionHandler;
import com.tripagent.place.dto.PlaceRecommendConcept;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.service.PlaceService;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class PlaceControllerTest {

    private PlaceService placeService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        placeService = org.mockito.Mockito.mock(PlaceService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new PlaceController(placeService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void searchPlacesReturnsCommonSuccessResponse() throws Exception {
        when(placeService.searchPlaces(PlaceRecommendConcept.FOOD, null, "ocean", null))
                .thenReturn(List.of(place(10L, "Ocean Food")));

        mockMvc.perform(get("/api/places")
                        .param("concept", "FOOD")
                        .param("keyword", "ocean"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].placeId").value(10L))
                .andExpect(jsonPath("$.data[0].name").value("Ocean Food"));
    }

    @Test
    void getPlaceReturnsCommonSuccessResponse() throws Exception {
        when(placeService.getPlace(10L)).thenReturn(place(10L, "Test Place"));

        mockMvc.perform(get("/api/places/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.placeId").value(10L))
                .andExpect(jsonPath("$.data.name").value("Test Place"));
    }

    @Test
    void getPlaceReturnsNotFoundErrorResponse() throws Exception {
        when(placeService.getPlace(999L)).thenThrow(new NoSuchElementException("Place not found. placeId=999"));

        mockMvc.perform(get("/api/places/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Place not found. placeId=999"));
    }

    private PlaceResponse place(Long placeId, String name) {
        return new PlaceResponse(
                placeId,
                name,
                "NATURE",
                "EAST",
                "JEJU",
                33.0,
                126.0,
                60,
                false,
                true,
                1,
                2,
                3,
                4,
                5,
                4,
                3,
                "description"
        );
    }
}

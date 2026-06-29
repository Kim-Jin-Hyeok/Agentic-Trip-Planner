package com.tripagent.common.validation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tripagent.common.exception.GlobalExceptionHandler;
import com.tripagent.itinerary.dto.ItineraryCreateRequest;
import com.tripagent.itinerary.dto.ItineraryGenerateRequest;
import com.tripagent.itinerary.dto.ItineraryReorderRequest;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.itinerary.dto.ItineraryUpdateRequest;
import com.tripagent.itinerary.service.ItineraryGenerateService;
import com.tripagent.itinerary.service.ItineraryService;
import com.tripagent.trip.controller.TripController;
import com.tripagent.trip.dto.TripCreateRequest;
import com.tripagent.trip.service.TripService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class ControllerValidationTest {

    private TripService tripService;
    private ItineraryGenerateService itineraryGenerateService;
    private ItineraryService itineraryService;
    private MockMvc tripMockMvc;
    private MockMvc itineraryMockMvc;

    @BeforeEach
    void setUp() {
        tripService = org.mockito.Mockito.mock(TripService.class);
        itineraryGenerateService = org.mockito.Mockito.mock(ItineraryGenerateService.class);
        itineraryService = org.mockito.Mockito.mock(ItineraryService.class);

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        tripMockMvc = MockMvcBuilders
                .standaloneSetup(new TripController(tripService, itineraryGenerateService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
        itineraryMockMvc = MockMvcBuilders
                .standaloneSetup(new com.tripagent.itinerary.controller.ItineraryController(itineraryService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void createTripReturnsInvalidRequestWhenRequiredFieldIsMissing() throws Exception {
        tripMockMvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").exists());

        verify(tripService, never()).createTrip(any(TripCreateRequest.class));
    }

    @Test
    void createItineraryReturnsInvalidRequestWhenDayNoOrOrderNoIsNotPositive() throws Exception {
        String requestBody = """
                {
                  "placeId": 10,
                  "dayNo": 0,
                  "orderNo": 0,
                  "startTime": "09:00:00",
                  "endTime": "10:00:00",
                  "travelMinutesFromPrevious": 0
                }
                """;

        itineraryMockMvc.perform(post("/api/trips/1/itineraries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").exists());

        verify(itineraryService, never()).createItinerary(any(), any(ItineraryCreateRequest.class));
    }

    @Test
    void updateItineraryReturnsInvalidRequestWhenTravelMinutesFromPreviousIsNegative() throws Exception {
        String requestBody = """
                {
                  "travelMinutesFromPrevious": -1
                }
                """;

        itineraryMockMvc.perform(patch("/api/trips/1/itineraries/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").exists());

        verify(itineraryService, never()).updateItinerary(any(), any(), any(ItineraryUpdateRequest.class));
    }

    @Test
    void reorderItinerariesReturnsInvalidRequestWhenItemsAreEmpty() throws Exception {
        String requestBody = """
                {
                  "items": []
                }
                """;

        itineraryMockMvc.perform(patch("/api/trips/1/itineraries/reorder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").exists());

        verify(itineraryService, never()).reorderItineraries(any(), any(ItineraryReorderRequest.class));
    }

    @Test
    void reorderItinerariesReturnsCommonSuccessResponse() throws Exception {
        String requestBody = """
                {
                  "items": [
                    {
                      "itineraryId": 100,
                      "dayNo": 1,
                      "orderNo": 1
                    }
                  ]
                }
                """;
        when(itineraryService.reorderItineraries(any(), any(ItineraryReorderRequest.class))).thenReturn(List.of());

        itineraryMockMvc.perform(patch("/api/trips/1/itineraries/reorder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void generateItinerariesAllowsMissingOptionalRequestBody() throws Exception {
        when(itineraryGenerateService.generateItineraries(1L, null)).thenReturn(List.of());

        tripMockMvc.perform(post("/api/trips/1/generate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());

        verify(itineraryGenerateService).generateItineraries(1L, null);
    }

    @Test
    void generateItinerariesReturnsInvalidRequestWhenPreferredCategoryIsUnknown() throws Exception {
        String requestBody = """
                {
                  "preferredCategories": ["UNKNOWN"]
                }
                """;

        tripMockMvc.perform(post("/api/trips/1/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("Request body is invalid."));

        verify(itineraryGenerateService, never()).generateItineraries(any(), any(ItineraryGenerateRequest.class));
    }
}

package com.tripagent.member.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tripagent.common.exception.GlobalExceptionHandler;
import com.tripagent.member.dto.MemberCreateRequest;
import com.tripagent.member.dto.MemberResponse;
import com.tripagent.member.service.MemberService;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class MemberControllerTest {

    private MemberService memberService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        memberService = org.mockito.Mockito.mock(MemberService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new MemberController(memberService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createMemberReturnsCommonSuccessResponse() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest(
                "test@example.com",
                "password123",
                "testUser"
        );
        when(memberService.createMember(request))
                .thenReturn(member(1L));

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "test@example.com",
                                  "password": "password123",
                                  "nickname": "testUser"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.memberId").value(1L))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.nickname").value("testUser"))
                .andExpect(jsonPath("$.data.role").value("USER"));
    }

    @Test
    void createMemberReturnsInvalidRequestWhenEmailIsInvalid() throws Exception {
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "invalid-email",
                                  "password": "password123",
                                  "nickname": "testUser"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("email: Member email must be valid."));
    }

    @Test
    void getMemberReturnsCommonSuccessResponse() throws Exception {
        when(memberService.getMember(1L)).thenReturn(member(1L));

        mockMvc.perform(get("/api/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.memberId").value(1L))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }

    @Test
    void getMemberReturnsNotFoundErrorResponse() throws Exception {
        when(memberService.getMember(999L))
                .thenThrow(new NoSuchElementException("Member not found. memberId=999"));

        mockMvc.perform(get("/api/members/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Member not found. memberId=999"));
    }

    private MemberResponse member(Long memberId) {
        return new MemberResponse(
                memberId,
                "test@example.com",
                "testUser",
                LocalDateTime.of(2026, 7, 1, 9, 0)
        );
    }
}

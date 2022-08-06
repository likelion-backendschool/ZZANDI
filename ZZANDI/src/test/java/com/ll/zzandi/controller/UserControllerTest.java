package com.ll.zzandi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.UserRequestDto;
import com.ll.zzandi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void 회원가입() throws Exception {
        String userId="testId";
        String password="testPassword";
        String userEmail="test@test.com";
        String userNickname="Test";
        UserRequestDto userRequestDto=new UserRequestDto(userId,password,userEmail,userNickname);


        when(userService.join(userRequestDto)).thenReturn(mock(User.class));

        //Post요청으로 Dto를 넘기면 200(OK)가 나와야한다.
        mockMvc.perform(post("/api/v1/user/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserRequestDto(userId,password,userEmail,userNickname)))
                )
                .andDo(print()).andExpect(status().isOk());
    }
    
}
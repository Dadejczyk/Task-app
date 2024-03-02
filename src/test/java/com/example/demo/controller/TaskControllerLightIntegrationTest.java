package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(TaskController.class)
public class TaskControllerLightIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean //po to zeby mockować jakiegos bean i wtedy nie korzystamy z tego naszego integration profile trzymanego w pamieci natomiast mozemy się posłuzyć innym mechanizmem
    private TaskRepository repo;

    @Test
    void httpGet_returnsGivenTask() throws Exception {
        //given
        String description = "foo";
        when(repo.findById(anyInt()))
                .thenReturn(Optional.of(new Task(description, LocalDateTime.now())));
        //when + then
        mockMvc.perform(get("/tasks/123"))
                .andDo(print())
                .andExpect(content().string(containsString(description)));
    }
}


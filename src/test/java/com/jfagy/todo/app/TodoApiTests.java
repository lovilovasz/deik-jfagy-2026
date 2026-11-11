package com.jfagy.todo.app;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TodoApiTests {

  @Autowired
  MockMvc mvc;

  private static final String PERSON = "{\"name\":\"Ann\",\"email\":\"ann@x.com\"}";

  @Test
  void personAndTodoCrud() throws Exception {
    mvc.perform(post("/api/persons").contentType(MediaType.APPLICATION_JSON).content(PERSON))
        .andExpect(status().isCreated());
    mvc.perform(post("/api/persons").contentType(MediaType.APPLICATION_JSON).content(PERSON))
        .andExpect(status().isConflict());

    mvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON)
        .content("{\"title\":\"t\",\"personId\":1}"))
        .andExpect(status().isCreated()).andExpect(jsonPath("$.personId").value(1));
    mvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON)
        .content("{\"title\":\"t\",\"personId\":99}"))
        .andExpect(status().isNotFound());
    mvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON)
        .content("{\"personId\":1}"))
        .andExpect(status().isBadRequest());
    mvc.perform(put("/api/todos/1").contentType(MediaType.APPLICATION_JSON)
        .content("{\"title\":\"t2\",\"completed\":true,\"personId\":1}"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.completed").value(true));
    mvc.perform(get("/api/todos?personId=1")).andExpect(jsonPath("$.length()").value(1));
    mvc.perform(delete("/api/todos/1")).andExpect(status().isNoContent());
    mvc.perform(get("/api/todos/1")).andExpect(status().isNotFound());
    mvc.perform(delete("/api/persons/1")).andExpect(status().isNoContent());
  }
}

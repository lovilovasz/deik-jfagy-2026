package com.jfagy.todo.app;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import java.util.UUID;
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

  private long createPerson() throws Exception {
    String body = "{\"name\":\"Ann\",\"email\":\"" + UUID.randomUUID() + "@example.com\"}";
    return idOf(mvc.perform(post("/api/persons").contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString());
  }

  private long createTodo(long personId, String extraJson) throws Exception {
    String body = "{\"title\":\"t\",\"personId\":" + personId + extraJson + "}";
    return idOf(mvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString());
  }

  private static long idOf(String json) {
    return ((Number) JsonPath.read(json, "$.id")).longValue();
  }

  @Test
  void personAndTodoCrud() throws Exception {
    long personId = createPerson();
    long todoId = createTodo(personId, "");

    mvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON)
        .content("{\"title\":\"t\",\"personId\":999999}"))
        .andExpect(status().isNotFound());
    mvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON)
        .content("{\"personId\":" + personId + "}"))
        .andExpect(status().isBadRequest());
    mvc.perform(put("/api/todos/" + todoId).contentType(MediaType.APPLICATION_JSON)
        .content("{\"title\":\"t2\",\"completed\":true,\"personId\":" + personId + "}"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.completed").value(true));
    mvc.perform(get("/api/todos?personId=" + personId))
        .andExpect(jsonPath("$.length()").value(1));
    mvc.perform(delete("/api/todos/" + todoId)).andExpect(status().isNoContent());
    mvc.perform(get("/api/todos/" + todoId)).andExpect(status().isNotFound());
    mvc.perform(delete("/api/persons/" + personId)).andExpect(status().isNoContent());
  }

  @Test
  void completedDefaultsToFalseWhenOmitted() throws Exception {
    long id = createTodo(createPerson(), "");
    mvc.perform(get("/api/todos/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.completed").value(false));
  }

  @Test
  void listWithoutFilterReturnsAllTodos() throws Exception {
    long id = createTodo(createPerson(), ",\"description\":\"d\"");
    mvc.perform(get("/api/todos"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.id == " + id + ")].description").value("d"));
  }

  @Test
  void listFilteredByPersonOnlyReturnsTheirTodos() throws Exception {
    long ann = createPerson();
    long bob = createPerson();
    createTodo(ann, "");
    createTodo(bob, "");
    createTodo(bob, "");
    mvc.perform(get("/api/todos?personId=" + bob))
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  void updateCanReassignPerson() throws Exception {
    long ann = createPerson();
    long bob = createPerson();
    long todoId = createTodo(ann, "");
    mvc.perform(put("/api/todos/" + todoId).contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"t\",\"personId\":" + bob + "}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.personId").value(bob));
  }

  @Test
  void updateMissingTodoIsNotFound() throws Exception {
    long personId = createPerson();
    mvc.perform(put("/api/todos/999999").contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"t\",\"personId\":" + personId + "}"))
        .andExpect(status().isNotFound());
  }

  @Test
  void updateWithMissingPersonIsNotFound() throws Exception {
    long todoId = createTodo(createPerson(), "");
    mvc.perform(put("/api/todos/" + todoId).contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"t\",\"personId\":999999}"))
        .andExpect(status().isNotFound());
  }

  @Test
  void updateWithInvalidBodyIsBadRequest() throws Exception {
    long personId = createPerson();
    long todoId = createTodo(personId, "");
    mvc.perform(put("/api/todos/" + todoId).contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"\",\"personId\":" + personId + "}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deleteMissingTodoIsNotFound() throws Exception {
    mvc.perform(delete("/api/todos/999999")).andExpect(status().isNotFound());
  }
}

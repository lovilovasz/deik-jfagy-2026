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
class PersonApiTests {

  @Autowired
  MockMvc mvc;

  private static String uniqueEmail() {
    return UUID.randomUUID() + "@example.com";
  }

  private static String person(String name, String email) {
    return "{\"name\":\"" + name + "\",\"email\":\"" + email + "\"}";
  }

  private long createPerson(String name, String email) throws Exception {
    String body = mvc.perform(
            post("/api/persons").contentType(MediaType.APPLICATION_JSON)
                .content(person(name, email)))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();
    return ((Number) JsonPath.read(body, "$.id")).longValue();
  }

  @Test
  void createReturnsLocationAndBody() throws Exception {
    String email = uniqueEmail();
    mvc.perform(post("/api/persons").contentType(MediaType.APPLICATION_JSON)
            .content(person("Ann", email)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Ann"))
        .andExpect(jsonPath("$.email").value(email));
  }

  @Test
  void createWithDuplicateEmailConflicts() throws Exception {
    String email = uniqueEmail();
    createPerson("Ann", email);
    mvc.perform(post("/api/persons").contentType(MediaType.APPLICATION_JSON)
            .content(person("Bob", email)))
        .andExpect(status().isConflict());
  }

  @Test
  void createWithInvalidBodyIsBadRequest() throws Exception {
    mvc.perform(post("/api/persons").contentType(MediaType.APPLICATION_JSON)
            .content(person("", "not-an-email")))
        .andExpect(status().isBadRequest());
  }

  @Test
  void listContainsCreatedPerson() throws Exception {
    String email = uniqueEmail();
    createPerson("Listed", email);
    mvc.perform(get("/api/persons"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.email == '" + email + "')]").exists());
  }

  @Test
  void getReturnsPersonOrNotFound() throws Exception {
    long id = createPerson("Ann", uniqueEmail());
    mvc.perform(get("/api/persons/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id));
    mvc.perform(get("/api/persons/999999")).andExpect(status().isNotFound());
  }

  @Test
  void updateKeepingSameEmailSucceeds() throws Exception {
    String email = uniqueEmail();
    long id = createPerson("Ann", email);
    mvc.perform(put("/api/persons/" + id).contentType(MediaType.APPLICATION_JSON)
            .content(person("Anna", email)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Anna"));
  }

  @Test
  void updateToFreeEmailSucceeds() throws Exception {
    long id = createPerson("Ann", uniqueEmail());
    String newEmail = uniqueEmail();
    mvc.perform(put("/api/persons/" + id).contentType(MediaType.APPLICATION_JSON)
            .content(person("Ann", newEmail)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value(newEmail));
  }

  @Test
  void updateToTakenEmailConflicts() throws Exception {
    String taken = uniqueEmail();
    createPerson("Taken", taken);
    long id = createPerson("Ann", uniqueEmail());
    mvc.perform(put("/api/persons/" + id).contentType(MediaType.APPLICATION_JSON)
            .content(person("Ann", taken)))
        .andExpect(status().isConflict());
  }

  @Test
  void updateMissingPersonIsNotFound() throws Exception {
    mvc.perform(put("/api/persons/999999").contentType(MediaType.APPLICATION_JSON)
            .content(person("Ann", uniqueEmail())))
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteRemovesPersonAndTheirTodos() throws Exception {
    long id = createPerson("Ann", uniqueEmail());
    String todoBody = mvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"t\",\"personId\":" + id + "}"))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();
    long todoId = ((Number) JsonPath.read(todoBody, "$.id")).longValue();

    mvc.perform(delete("/api/persons/" + id)).andExpect(status().isNoContent());
    mvc.perform(get("/api/persons/" + id)).andExpect(status().isNotFound());
    mvc.perform(get("/api/todos/" + todoId)).andExpect(status().isNotFound());
  }

  @Test
  void deleteMissingPersonIsNotFound() throws Exception {
    mvc.perform(delete("/api/persons/999999")).andExpect(status().isNotFound());
  }
}

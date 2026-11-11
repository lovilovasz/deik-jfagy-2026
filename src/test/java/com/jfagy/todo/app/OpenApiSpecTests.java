package com.jfagy.todo.app;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Fetches the springdoc OpenAPI document and writes it to {@code openapi/openapi.json}
 * so the frontend can generate its client from it. Re-run {@code ./mvnw test} after API changes.
 */
@SpringBootTest
@AutoConfigureMockMvc
class OpenApiSpecTests {

  @Autowired
  MockMvc mvc;

  @Test
  void exportsOpenApiSpec() throws Exception {
    String json = mvc.perform(get("/v3/api-docs"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.paths['/api/todos']").exists())
        .andExpect(jsonPath("$.paths['/api/persons']").exists())
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    Path out = Path.of("openapi", "openapi.json");
    Files.createDirectories(out.getParent());
    Files.writeString(out, json + System.lineSeparator(), StandardCharsets.UTF_8);
  }
}

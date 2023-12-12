package net.barrage.school.java.ecatalog.app;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("db")
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    // Use https://jwt.io/ to check this bearer token
    public static final String BEARER = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJ1cm46dG95b3U6YXBpIiwic3ViIjoiM2QxYzljZjktYWI0ZC00MjcwLWJiMjYtMjYxOTI5N2UyNTg0IiwiaXNzIjoidXJuOmJhcnJhZ2U6dXNlciIsImlhdCI6MTcwMTAzMDQzOCwiZXhwIjoxNzAxMDM0MDM4LCJiLXJvbGVzIjoiUk9MRV9NRVJDSEFOVF9NQU5BR0VSLFJPTEVfU1lTVEVNX0FETUlOIn0.xtdlBZ4Zc80_EFd-eB7L4Du60K-QIx7PysRl8xYgYks";


    @Autowired
    MockMvc mockMvc;

    @Test
    @SneakyThrows
    void jwt_auth_authorized() {
        mockMvc.perform(get("/e-catalog/api/v1/products/list")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void basic_auth_authorized() {
        mockMvc.perform(get("/e-catalog/api/v1/products/list")
                        .with(httpBasic("John", "Password")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void basic_auth_wrong_credentials() {
        mockMvc.perform(get("/e-catalog/api/v1/products/list")
                        .with(httpBasic("John", "Password2")))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void anonymous_auth_no_role() {
        mockMvc.perform(get("/e-catalog/api/v1/products/list")
                        .with(anonymous()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
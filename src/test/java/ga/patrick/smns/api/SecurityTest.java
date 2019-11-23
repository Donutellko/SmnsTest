package ga.patrick.smns.api;

import ga.patrick.smns.TestJpaConfig;
import ga.patrick.smns.config.*;
import ga.patrick.smns.dto.ModelMapper;
import ga.patrick.smns.repository.TemperatureRepository;
import ga.patrick.smns.service.TemperatureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SecurityTest {

    private MockMvc mockMvc;

    @MockBean
    private TemperatureRepository temperatureRepository;

    @Autowired
    private ApiController apiController;

    // https://jira.spring.io/si/jira.issueviews:issue-html/SEC-3174/SEC-3174.html
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    FilterChainProxy springSecurityFilterChain;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(apiController)
                .apply(springSecurity(springSecurityFilterChain))
                .build();
    }

    @WithAnonymousUser()
    @Test
    public void addAsAnonymous() throws Exception {
        mockMvc.perform(post("/api/add"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = "SENSOR")
    @Test
    public void addAsSensor() throws Exception {
        mockMvc.perform(post("/api/add"))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(roles = "USER")
    @Test
    public void addAsUser() throws Exception {
        mockMvc.perform(post("/api/add"))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(roles = {"ADMIN", "USER"})
    @Test
    public void addAsAdmin() throws Exception {
        mockMvc.perform(post("/api/add"))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithAnonymousUser
    public void latestAsAnonymous() throws Exception {
        mockMvc.perform(get("/api/latest"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "SENSOR")
    public void latestAsSensor() throws Exception {
        mockMvc.perform(get("/api/latest"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void latestAsUser() throws Exception {
        mockMvc.perform(get("/api/latest"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    public void latestAsAdmin() throws Exception {
        mockMvc.perform(get("/api/latest"))
                .andExpect(status().isOk());
    }


}

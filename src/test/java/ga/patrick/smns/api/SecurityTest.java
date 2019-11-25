package ga.patrick.smns.api;

import ga.patrick.smns.domain.Temperature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SecurityTest {

    private MockMvc apiMock;

    private MockMvc uiMock;

    @Autowired
    TestSharedService testUtils;

    private Temperature temperatureExample = new Temperature(0, 0, 0);

    // https://jira.spring.io/si/jira.issueviews:issue-html/SEC-3174/SEC-3174.html
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    FilterChainProxy springSecurityFilterChain;

    @Before
    public void init() {
        testUtils.cleanup();

        apiMock = MockMvcBuilders
                .standaloneSetup(testUtils.apiController)
                .apply(springSecurity(springSecurityFilterChain))
                .build();

        uiMock = MockMvcBuilders
                .standaloneSetup(testUtils.uiController)
                .apply(springSecurity(springSecurityFilterChain))
                .build();
    }

    @After
    public void cleanup() {
        testUtils.cleanup();
    }

    @WithAnonymousUser
    @Test
    public void loginAsAnonymous() throws Exception {
        testUtils.performGetLogin(uiMock)
                .andExpect(status().isOk());
    }

    @WithAnonymousUser()
    @Test
    public void indexAsAnonymous() throws Exception {
        testUtils.performGetIndex(uiMock)
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

    }

    @WithMockUser(roles = "SENSOR")
    @Test
    public void indexAsSensor() throws Exception {
        testUtils.performGetIndex(uiMock)
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @Test
    public void indexAsUser() throws Exception {
        testUtils.performGetIndex(uiMock)
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void indexAsAdmin() throws Exception {
        testUtils.performGetIndex(uiMock)
                .andExpect(status().isOk());
    }

    @WithAnonymousUser()
    @Test
    public void addAsAnonymous() throws Exception {
        testUtils.performPostAdd(apiMock, temperatureExample)
//                .andExpect(status().isUnauthorized());
                .andExpect(status().isFound());
    }

    @WithMockUser(roles = "SENSOR")
    @Test
    public void addAsSensor() throws Exception {
        testUtils.performPostAdd(apiMock, temperatureExample)
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @Test
    public void addAsUser() throws Exception {
        testUtils.performPostAdd(apiMock, temperatureExample)
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void addAsAdmin() throws Exception {
        testUtils.performPostAdd(apiMock, temperatureExample)
                .andExpect(status().isOk());
    }


    @Test
    @WithAnonymousUser
    public void latestAsAnonymous() throws Exception {
        testUtils.performGetLatest(apiMock)
//                .andExpect(status().isUnauthorized());
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "SENSOR")
    public void latestAsSensor() throws Exception {
        testUtils.performGetLatest(apiMock)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void latestAsUser() throws Exception {
        testUtils.performGetLatest(apiMock)
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void latestAsAdmin() throws Exception {
        testUtils.performGetLatest(apiMock)
                .andExpect(status().isOk());
    }


}

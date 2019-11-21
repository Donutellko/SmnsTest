package ga.patrick.smns.api;

import ga.patrick.smns.TestJpaConfig;
import ga.patrick.smns.repository.TemperatureRepository;
import ga.patrick.smns.service.TemperatureService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {TestJpaConfig.class, ApiErrorHandler.class},
        loader = AnnotationConfigContextLoader.class
)
public class ApiAddIntegrationTest {

    private MockMvc mockMvc;

    @Resource
    private TemperatureRepository temperatureRepository;

    @Autowired
    private ApiErrorHandler apiErrorHandler;

    @Before
    public void init() {
        temperatureRepository.deleteAll();

        mockMvc = MockMvcBuilders.standaloneSetup(
                new ApiController(new TemperatureService(temperatureRepository))
        ).addFilters(apiErrorHandler).build();
    }

    @After
    public void cleanup() {
        temperatureRepository.deleteAll();
    }

    @Test
    public void addCorrect1() throws Exception {
        mockMvc.perform(
                post("/add")
                        .param("lat", "90.0")
                        .param("lon", "180.0")
                        .param("value", "10.0")
        ).andExpect(status().isOk());
    }

    @Test
    public void addNoData() throws Exception {
        mockMvc.perform(
                post("/add")
        ).andExpect(status().is4xxClientError());
    }


    @Test
    public void addTooBigLat() throws Exception {
        mockMvc.perform(
                post("/add")
                        .param("lat", "91.0")
                        .param("lon", "180.0")
                        .param("value", "10.0")
        );
    }

    @Test
    public void addTooSmallLat() throws Exception {
        mockMvc.perform(
                post("/add")
                        .param("lat", "-91.0")
                        .param("lon", "180.0")
                        .param("value", "10.0")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void addTooBigLon() throws Exception {
        mockMvc.perform(
                post("/add")
                        .param("lat", "10.0")
                        .param("lon", "181.0")
                        .param("value", "10.0")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void addTooSmallLon() throws Exception {
        mockMvc.perform(
                post("/add")
                        .param("lat", "10.0")
                        .param("lon", "-181.0")
                        .param("value", "10.0")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void addTooSmallTemp() throws Exception {
        mockMvc.perform(
                post("/add")
                        .param("lat", "10.0")
                        .param("lon", "10.0")
                        .param("value", "-500.0")
        ).andExpect(status().is4xxClientError());
    }

}
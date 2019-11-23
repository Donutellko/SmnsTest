package ga.patrick.smns.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ga.patrick.smns.TestJpaConfig;
import ga.patrick.smns.domain.Temperature;
import ga.patrick.smns.dto.ModelMapper;
import ga.patrick.smns.dto.TemperatureDto;
import ga.patrick.smns.repository.TemperatureRepository;
import ga.patrick.smns.service.TemperatureService;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {
                TestJpaConfig.class,
                ApiErrorHandler.class,
                TemperatureService.class,
                ApiController.class,
                ModelMapper.class},
        loader = AnnotationConfigContextLoader.class
)
public class ApiAddIntegrationTest {

    private MockMvc mockMvc;
    @Resource
    private TemperatureRepository temperatureRepository;

    @Autowired
    private ApiController apiController;

    @Autowired
    private ApiErrorHandler apiErrorHandler;

    @Autowired
    private ModelMapper modelMapper;

    private ObjectMapper mapper = new ObjectMapper();

    private static final Matcher<String>
            INVALID_LATITUDE_MATCHER = Matchers.containsString("Invalid latitude: "),
            INVALID_LONGITUDE_MATCHER = Matchers.containsString("Invalid longitude: "),
            INVALID_TEMPERATURE_MATCHER = Matchers.containsString("Invalid temperature: ");


    @Before
    public void init() {
        temperatureRepository.deleteAll();

        mockMvc = MockMvcBuilders
                .standaloneSetup(apiController)
                .addFilters(apiErrorHandler)
                .build();
    }

    @After
    public void cleanup() {
        temperatureRepository.deleteAll();
    }

    private String toJson(Object o) throws JsonProcessingException {
        return mapper.writeValueAsString(o);
    }

    private ResultActions performPostAdd(Temperature t) throws Exception {
        TemperatureDto body = modelMapper.toDto(t);
        return mockMvc.perform(
                post("/add")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJson(body)));
    }

    @Test
    public void addCorrect() throws Exception {
        double lat = 90;
        double lon = 180;
        double temperature = -273.15;

        performPostAdd(new Temperature(temperature, lat, lon))
                .andExpect(status().isOk());
    }

    @Test
    public void addNoData() throws Exception {
        mockMvc.perform(
                post("/add")
        ).andExpect(status().isBadRequest());
    }


    @Test
    public void addTooBigLat() throws Exception {
        double lat = 91;
        double lon = 180;
        double temperature = 10;

        performPostAdd(new Temperature(temperature, lat, lon))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INVALID_LATITUDE_MATCHER));
    }

    @Test
    public void addTooSmallLat() throws Exception {
        double lat = -91;
        double lon = 180;
        double temperature = 10;

        performPostAdd(new Temperature(temperature, lat, lon))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INVALID_LATITUDE_MATCHER));
    }

    @Test
    public void addTooBigLon() throws Exception {
        double lat = 10;
        double lon = 181;
        double temperature = 10;

        performPostAdd(new Temperature(temperature, lat, lon))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INVALID_LONGITUDE_MATCHER));
    }

    @Test
    public void addTooSmallLon() throws Exception {
        double lat = 10;
        double lon = -181;
        double temperature = 10;

        performPostAdd(new Temperature(temperature, lat, lon))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INVALID_LONGITUDE_MATCHER));
    }

    @Test
    public void addTooSmallTemp() throws Exception {
        double lat = 10;
        double lon = 10;
        double temperature = -273.16;

        performPostAdd(new Temperature(temperature, lat, lon))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INVALID_TEMPERATURE_MATCHER));
    }

    /** When several constraints are violated, there should be a message for each of them. */
    @Test
    public void addInvalidLatLonTemp() throws Exception {
        double lat = 91;
        double lon = 181;
        double temperature = -273.16;

        performPostAdd(new Temperature(temperature, lat, lon))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INVALID_LATITUDE_MATCHER))
                .andExpect(content().string(INVALID_LONGITUDE_MATCHER))
                .andExpect(content().string(INVALID_TEMPERATURE_MATCHER));
    }
}
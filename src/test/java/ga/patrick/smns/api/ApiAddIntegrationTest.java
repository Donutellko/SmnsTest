package ga.patrick.smns.api;

import ga.patrick.smns.domain.Temperature;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ApiAddIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    TestSharedService testUtils;

    @Autowired
    private ApiErrorHandler apiErrorHandler;

    private static final Matcher<String>
            INVALID_LATITUDE_MATCHER = Matchers.containsString("Invalid latitude: "),
            INVALID_LONGITUDE_MATCHER = Matchers.containsString("Invalid longitude: "),
            INVALID_TEMPERATURE_MATCHER = Matchers.containsString("Invalid temperature: ");


    @Before
    public void init() {
        testUtils.cleanup();

        mockMvc = MockMvcBuilders
                .standaloneSetup(testUtils.apiController)
                .addFilters(apiErrorHandler)
                .build();
    }

    @After
    public void cleanup() {
        testUtils.cleanup();
    }


    @Test
    public void addCorrect() throws Exception {
        double lat = 90;
        double lon = 180;
        double temperature = -273.15;

        testUtils.performPostAdd(mockMvc, new Temperature(temperature, lat, lon))
                .andExpect(status().isOk());
    }

    @Test
    public void addNoData() throws Exception {
        mockMvc.perform(post("/api/add"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void addTooBigLat() throws Exception {
        double lat = 91;
        double lon = 180;
        double temperature = 10;

        testUtils.performPostAdd(mockMvc, new Temperature(temperature, lat, lon))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INVALID_LATITUDE_MATCHER));
    }

    @Test
    public void addTooSmallLat() throws Exception {
        double lat = -91;
        double lon = 180;
        double temperature = 10;

        testUtils.performPostAdd(mockMvc, new Temperature(temperature, lat, lon))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INVALID_LATITUDE_MATCHER));
    }

    @Test
    public void addTooBigLon() throws Exception {
        double lat = 10;
        double lon = 181;
        double temperature = 10;

        testUtils.performPostAdd(mockMvc, new Temperature(temperature, lat, lon))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INVALID_LONGITUDE_MATCHER));
    }

    @Test
    public void addTooSmallLon() throws Exception {
        double lat = 10;
        double lon = -181;
        double temperature = 10;

        testUtils.performPostAdd(mockMvc, new Temperature(temperature, lat, lon))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INVALID_LONGITUDE_MATCHER));
    }

    @Test
    public void addTooSmallTemp() throws Exception {
        double lat = 10;
        double lon = 10;
        double temperature = -273.16;

        testUtils.performPostAdd(mockMvc, new Temperature(temperature, lat, lon))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INVALID_TEMPERATURE_MATCHER));
    }

    /** When several constraints are violated, there should be a message for each of them. */
    @Test
    public void addInvalidLatLonTemp() throws Exception {
        double lat = 91;
        double lon = 181;
        double temperature = -273.16;

        testUtils.performPostAdd(mockMvc, new Temperature(temperature, lat, lon))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INVALID_LATITUDE_MATCHER))
                .andExpect(content().string(INVALID_LONGITUDE_MATCHER))
                .andExpect(content().string(INVALID_TEMPERATURE_MATCHER));
    }
}
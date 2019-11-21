package ga.patrick.smns.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ga.patrick.smns.TestJpaConfig;
import ga.patrick.smns.domain.Temperature;
import ga.patrick.smns.repository.TemperatureRepository;
import ga.patrick.smns.service.TemperatureService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {TestJpaConfig.class, ApiErrorHandler.class},
        loader = AnnotationConfigContextLoader.class
)
public class ApiLatestIntegrationTest {

    private MockMvc mockMvc;

    @Resource
    private TemperatureRepository temperatureRepository;

    @Autowired
    private ApiErrorHandler apiErrorHandler;

    private List<Temperature> addedEntries = new ArrayList<>();

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new ApiController(new TemperatureService(temperatureRepository))
        ).addFilters(apiErrorHandler).build();

        temperatureRepository.deleteAll();
        for (int i = 0; i < 15; i++) {
            Temperature t = new Temperature(i, i, i);
            addedEntries.add(temperatureRepository.save(t));
        }
    }

    @After
    public void cleanup() {
        temperatureRepository.deleteAll();
    }

    private ResultActions perform(int count) throws Exception {
        return mockMvc.perform(
                get("/latest")
                        .param("count", String.valueOf(count))
        );
    }

    private Temperature[] deserialize(MvcResult result) throws IOException {
        String responseString = result.getResponse().getContentAsString();
        return mapper.readValue(responseString, Temperature[].class);
    }

    private Temperature[] latestEntries(int n) {
        int size = Math.min(addedEntries.size(), n);
        List<Temperature> latestN = addedEntries.subList(addedEntries.size() - size, addedEntries.size());
        Collections.reverse(latestN);
        Temperature[] t = new Temperature[size];
        latestN.toArray(t);
        return t;
    }

    private void testLatest(int count) throws Exception {
        MvcResult result = perform(count).andExpect(status().isOk()).andReturn();
        Temperature[] response = deserialize(result);
        assertArrayEquals(latestEntries(count), response);
    }

    @Test
    public void latestOne() throws Exception {
        testLatest(1);
    }

    @Test
    public void latestAll() throws Exception {
        testLatest(addedEntries.size());
    }

    @Test
    public void latestOver() throws Exception {
        testLatest(addedEntries.size() + 1);
    }
}
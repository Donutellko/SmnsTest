package ga.patrick.smns.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ga.patrick.smns.TestSharedService;
import ga.patrick.smns.domain.Location;
import ga.patrick.smns.domain.Temperature;
import ga.patrick.smns.dto.TemperatureDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@WithMockUser(roles = "USER")
public class ApiLatestIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    TestSharedService testUtils;

    @Autowired
    private ApiErrorHandler apiErrorHandler;

    private List<TemperatureDto> addedEntries = new ArrayList<>();

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void init() {

        testUtils.cleanup();

        mockMvc = MockMvcBuilders
                .standaloneSetup(testUtils.apiController)
                .addFilters(apiErrorHandler)
                .build();

        Location l2 = new Location("Test location 2", 20, 20, 11, 11);
        Location l1 = new Location("Test location 1", 10, 10, 0, 0);
        testUtils.locationRepository.save(l1);
        testUtils.locationRepository.save(l2);

        for (int i = 0; i < 15; i++) {
            Temperature t = new Temperature(i, i, i);
            t.setLocation(i <= 10 ? l1 : l2);
            addedEntries.add(testUtils.map(testUtils.temperatureRepository.save(t)));
        }
    }

    @After
    public void cleanup() {
        testUtils.cleanup();
    }

    private ResultActions perform(int count) throws Exception {
        return testUtils.performGetLatest(mockMvc, count);
    }

    private TemperatureDto[] deserialize(MvcResult result) throws IOException {
        String responseString = result.getResponse().getContentAsString();
        return mapper.readValue(responseString, TemperatureDto[].class);
    }

    private TemperatureDto[] latestEntries(int n) {
        int size = Math.min(addedEntries.size(), n);
        List<TemperatureDto> latestN = addedEntries.subList(addedEntries.size() - size, addedEntries.size());
        Collections.reverse(latestN);
        TemperatureDto[] t = new TemperatureDto[size];
        latestN.toArray(t);
        return t;
    }

    private void testLatest(int count) throws Exception {
        MvcResult result = perform(count).andExpect(status().isOk()).andReturn();
        TemperatureDto[] response = deserialize(result);
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
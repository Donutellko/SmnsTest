package ga.patrick.smns.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ga.patrick.smns.domain.Temperature;
import ga.patrick.smns.dto.ModelMapper;
import ga.patrick.smns.dto.TemperatureDto;
import ga.patrick.smns.geocode.GeocodeClient;
import ga.patrick.smns.geocode.GeocodeResponse;
import ga.patrick.smns.repository.LocationRepository;
import ga.patrick.smns.repository.TemperatureRepository;
import ga.patrick.smns.web.UiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.annotation.Resource;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SuppressWarnings("WeakerAccess")
@Service
public class TestSharedService {

    @Autowired
    public UiController uiController;

    @Autowired
    public ApiController apiController;

    @Resource
    public TemperatureRepository temperatureRepository;

    @Resource
    public LocationRepository locationRepository;

    @MockBean
    public GeocodeClient geocodeClientMock;

    public GeocodeResponse geocodeResponseExample;

    private ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ModelMapper modelMapper;

    TestSharedService() throws IOException {

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        geocodeResponseExample = mapper.readValue(
                "{ \"plus_code\" : { \"compound_code\" : \"252X+2X Приморский район, Санкт-Петербург, Россия\", \"global_code\" : \"9GGG252X+2X\" }, \"results\" : [ { \"address_components\" : [ { \"long_name\" : \"Санкт-Петербург\", \"short_name\" : \"Санкт-Петербург\", \"types\" : [ \"administrative_area_level_1\", \"political\" ] }, { \"long_name\" : \"Санкт-Петербург\", \"short_name\" : \"Санкт-Петербург\", \"types\" : [ \"administrative_area_level_2\", \"political\" ] }, { \"long_name\" : \"Россия\", \"short_name\" : \"RU\", \"types\" : [ \"country\", \"political\" ] } ], \"formatted_address\" : \"Санкт-Петербург, Россия\", \"geometry\" : { \"bounds\" : { \"northeast\" : { \"lat\" : 60.2458091, \"lng\" : 30.7596049 }, \"southwest\" : { \"lat\" : 59.6337839, \"lng\" : 29.42539399999999 } }, \"location\" : { \"lat\" : 59.9342596, \"lng\" : 30.3350942 }, \"location_type\" : \"APPROXIMATE\", \"viewport\" : { \"northeast\" : { \"lat\" : 60.2458091, \"lng\" : 30.7596049 }, \"southwest\" : { \"lat\" : 59.6337839, \"lng\" : 29.42539399999999 } } }, \"place_id\" : \"ChIJnW9Kx4w3lkYR5MklR1rKR_o\", \"types\" : [ \"administrative_area_level_1\", \"political\" ] } ], \"status\" : \"OK\" }",
                GeocodeResponse.class);
    }

    public TemperatureDto map(Temperature t) {
        return modelMapper.toDto(t);
    }

    public ResultActions performGetIndex(MockMvc mockMvc) throws Exception {
        return mockMvc.perform(get("/"));
    }

    public ResultActions performGetLatest(MockMvc mockMvc) throws Exception {
        return mockMvc.perform(get("/api/latest"));
    }

    public ResultActions performGetLatest(MockMvc mockMvc, int count) throws Exception {
        return mockMvc.perform(get("/api/latest")
                .param("count", String.valueOf(count)));
    }

    public ResultActions performGetLogin(MockMvc mockMvc) throws Exception {
        return mockMvc.perform(get("/login"));
    }

    public ResultActions performPostAdd(MockMvc mockMvc, Temperature t) throws Exception {
        TemperatureDto body = modelMapper.toDto(t);
        return mockMvc.perform(
                post("/api/add")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJson(body)));
    }

    public String toJson(Object o) throws JsonProcessingException {
        return mapper.writeValueAsString(o);
    }

    public TemperatureDto parseTemperatureDto(String json) throws Exception {
        return mapper.readValue(json, TemperatureDto.class);
    }

    public void cleanup() {
        temperatureRepository.deleteAll();
        locationRepository.deleteAll();
    }

}

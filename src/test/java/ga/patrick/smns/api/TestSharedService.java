package ga.patrick.smns.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ga.patrick.smns.domain.Temperature;
import ga.patrick.smns.dto.ModelMapper;
import ga.patrick.smns.dto.TemperatureDto;
import ga.patrick.smns.repository.TemperatureRepository;
import ga.patrick.smns.web.UiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Service
public class TestSharedService {

    @Autowired
    UiController uiController;

    @Autowired
    ApiController apiController;

    @Resource
    TemperatureRepository temperatureRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ModelMapper modelMapper;

    TemperatureDto map(Temperature t) {
        return modelMapper.toDto(t);
    }

    ResultActions performGetIndex(MockMvc mockMvc) throws Exception {
        return mockMvc.perform(get("/"));
    }

    ResultActions performGetLatest(MockMvc mockMvc) throws Exception {
        return mockMvc.perform(get("/api/latest"));
    }

    ResultActions performGetLatest(MockMvc mockMvc, int count) throws Exception {
        return mockMvc.perform(get("/api/latest")
                .param("count", String.valueOf(count)));
    }

    ResultActions performGetLogin(MockMvc mockMvc) throws Exception {
        return mockMvc.perform(get("/login"));
    }

    ResultActions performPostAdd(MockMvc mockMvc, Temperature t) throws Exception {
        TemperatureDto body = modelMapper.toDto(t);
        return mockMvc.perform(
                post("/api/add")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJson(body)));
    }

    String toJson(Object o) throws JsonProcessingException {
        return mapper.writeValueAsString(o);
    }

    void cleanup() {
        temperatureRepository.deleteAll();
    }

}

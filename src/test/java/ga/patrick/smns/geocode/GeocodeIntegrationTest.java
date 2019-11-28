package ga.patrick.smns.geocode;


import ga.patrick.smns.api.TestSharedService;
import ga.patrick.smns.service.GeocodeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class GeocodeIntegrationTest {

    @Autowired
    TestSharedService testUtils;

    @Autowired
    GeocodeService geocodeService;

    @Autowired
    GeocodeClient geocodeClientMock;

    @Before
    public void init() {
        testUtils.cleanup();

        Mockito.when(geocodeClientMock.geocode(any()))
                .thenReturn(testUtils.geocodeResponseExample);
        Mockito.when(geocodeClientMock.decodeCityLevel(any(), any()))
                .thenReturn(testUtils.geocodeResponseExample);
    }

    @After
    public void cleanup() {
        testUtils.cleanup();
    }

    @Test
    public void testLocationCache() {
        double lat = 60;
        double lon = 30;
        geocodeService.getLocation(lat, lon);
        geocodeService.getLocation(lat, lon);
        verify(geocodeClientMock, times(1))
                .decodeCityLevel(lat, lon);

    }


}

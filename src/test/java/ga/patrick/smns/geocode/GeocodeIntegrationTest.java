package ga.patrick.smns.geocode;


import ga.patrick.smns.TestSharedService;
import ga.patrick.smns.domain.Location;
import ga.patrick.smns.service.GeocodeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class GeocodeIntegrationTest {

    @Autowired
    TestSharedService testUtils;

    @Autowired
    GeocodeService geocodeService;

    @Autowired
    GeocodeClient geocodeClientMock;


    static double spbLat = 60, spbLon = 30,
            emptyLat = 0, emptyLon = 0;

    @Before
    public void init() {
        testUtils.cleanup();

        Mockito.when(geocodeClientMock.geocode(any()))
                .thenReturn(testUtils.geocodeResponseSpbExample);

        Mockito.when(geocodeClientMock.decodeCityLevel(spbLat, spbLon))
                .thenReturn(testUtils.geocodeResponseSpbExample);

        Mockito.when(geocodeClientMock.decodeCityLevel(emptyLat, emptyLon))
                .thenReturn(testUtils.geocodeResponseEmptyExample);
    }

    @After
    public void cleanup() {
        testUtils.cleanup();
    }

    @Test
    public void testLocationCache() {
        geocodeService.getLocation(spbLat, spbLon);
        geocodeService.getLocation(spbLat + 0.1, spbLon + 0.1);
        geocodeService.getLocation(spbLat - 0.1, spbLon - 0.1);
        verify(geocodeClientMock, times(1))
                .decodeCityLevel(any(), any());

        assertEquals(testUtils.locationRepository.count(), 1);
    }

    @Test
    public void testRepositoryFind() {
        Location location = new Location("Empty space", 1, 1, -1, -1);
        location = testUtils.locationRepository.save(location);
        assertEquals(location, testUtils.locationRepository.find(0, 0));
        assertEquals(location, testUtils.locationRepository.find(-1, -1));
        assertEquals(location, testUtils.locationRepository.find(1, 1));
    }

    @Test
    public void testEmptyLocationCache() {
        geocodeService.getLocation(emptyLat, emptyLon);
        geocodeService.getLocation(emptyLat + 0.09, emptyLon + 0.09);
        geocodeService.getLocation(emptyLat - 0.09, emptyLon - 0.09);
        verify(geocodeClientMock, times(1))
                .decodeCityLevel(any(), any());

        assertEquals(testUtils.locationRepository.count(), 1);
        assertEquals(testUtils.locationRepository.find(emptyLat, emptyLon).getName(),
                "Empty place");
    }


}

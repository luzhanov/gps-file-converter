package com.waypointer.gpsfileconverter.parser.route;

import com.waypointer.gpsfileconverter.GpsRoute;
import com.waypointer.gpsfileconverter.GpsRoutePoint;
import com.waypointer.gpsfileconverter.parser.FileParser;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class KML21RouteParserTest {

    private final String pathRoute = getClass().getResource("/parsing/routes/route_test_2_1.kml").getFile();

    @Test
    public void parseFileKML21() throws Exception {
        FileInputStream fis = new FileInputStream(pathRoute);

        FileParser<GpsRoute> parser = new KML21RouteParser();
        List<GpsRoute> routes = parser.parseFile(new BufferedInputStream(fis));
        assertThat(routes).isNotNull();
        assertThat(routes).isNotEmpty();
        assertThat(routes).hasSize(1);
        assertThat(routes.get(0).getPoints()).hasSize(181);
        int order = 1;
        for (GpsRoutePoint r : routes.get(0).getPoints()) {
            assertThat(r.getLatitude()).isNotNull();
            assertThat(r.getLongitude()).isNotNull();
            assertThat(r.getOrder()).isNotNull();
            assertThat(r.getOrder()).isEqualTo(order);
            order++;
        }
    }

    @Test
    public void testFileDetection() throws Exception {
        FileInputStream fis = new FileInputStream(pathRoute);

        BufferedInputStream bis = new BufferedInputStream(fis);
        FileParser parser = new KML21RouteParser();
        assertThat(parser.isParsableFile(bis)).isTrue();
    }
}

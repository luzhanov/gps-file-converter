package com.waypointer.gpsfileconverter.parser.route;

import com.waypointer.gpsfileconverter.GpsRoute;
import com.waypointer.gpsfileconverter.GpsRoutePoint;
import com.waypointer.gpsfileconverter.parser.FileParser;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class KML22RouteParserTest {

    private final String pathTest = getClass().getResource("/parsing/routes/route_test_2_2.kml").getFile();
    private final String pathGE = getClass().getResource("/parsing/routes/route_test_2_2_from_google_ear.kml").getFile();
    private final String pathKarpaty = getClass().getResource("/parsing/routes/Karpaty_2014_new_year.kml").getFile();

    @Test
    public void parseFileKML22Karpaty() throws Exception {
        FileInputStream fis = new FileInputStream(pathKarpaty);

        FileParser<GpsRoute> parser = new KML22RouteParser();
        List<GpsRoute> routes = parser.parseFile(new BufferedInputStream(fis));
        assertThat(routes).isNotNull();
        assertThat(routes).isNotEmpty();
        assertThat(routes).hasSize(1);
        GpsRoute received = routes.get(0);
        assertThat(received.getRouteName()).isEqualTo("karpaty_2014_new_year");
        assertThat(received.getPoints()).hasSize(188);
        int order = 1;
        for (GpsRoutePoint point : received.getPoints()) {
            assertThat(point.getLatitude()).isNotNull();
            assertThat(point.getLongitude()).isNotNull();
            assertThat(point.getOrder()).isEqualTo(order);
            order++;
        }
    }

    @Test
    public void parseFileKML22OurFile() throws Exception {
        FileInputStream fis = new FileInputStream(pathTest);

        FileParser<GpsRoute> parser = new KML22RouteParser();
        List<GpsRoute> routes = parser.parseFile(new BufferedInputStream(fis));
        assertThat(routes).isNotNull();
        assertThat(routes).isNotEmpty();
        assertThat(routes).hasSize(1);
        GpsRoute received = routes.get(0);
        assertThat(received.getRouteName()).isEqualTo("Alex_2013_05_5days");
        assertThat(received.getPoints()).hasSize(878);
        int order = 1;
        for (GpsRoutePoint point : received.getPoints()) {
            assertThat(point.getLatitude()).isNotNull();
            assertThat(point.getLongitude()).isNotNull();
            assertThat(point.getOrder()).isNotNull();
            assertThat(point.getOrder()).isEqualTo(order);
            order++;
        }
    }

    @Test
    public void parseFileKML22GoogleFile() throws Exception {
        FileInputStream fis = new FileInputStream(pathGE);

        FileParser<GpsRoute> parser = new KML22RouteParser();
        List<GpsRoute> routes = parser.parseFile(new BufferedInputStream(fis));
        assertThat(routes).isNotNull();
        assertThat(routes).isNotEmpty();
        assertThat(routes).hasSize(1);
        GpsRoute received = routes.get(0);
        assertThat(received.getRouteName()).isEqualTo("route_test_2_2");
        assertThat(received.getPoints()).hasSize(224);
        int order = 1;
        for (GpsRoutePoint point : received.getPoints()) {
            assertThat(point.getLatitude()).isNotNull();
            assertThat(point.getLongitude()).isNotNull();
            assertThat(point.getOrder()).isNotNull();
            assertThat(point.getOrder()).isEqualTo(order);
            order++;
        }
    }

    @Test
    public void testFileDetectionOurFile() throws Exception {
        FileInputStream fis = new FileInputStream(pathTest);

        BufferedInputStream bis = new BufferedInputStream(fis);
        FileParser parser = new KML22RouteParser();
        assertThat(parser.isParsableFile(bis)).isTrue();
    }

    @Test
    public void testFileDetectionGoogleFile() throws Exception {
        FileInputStream fis = new FileInputStream(pathGE);

        BufferedInputStream bis = new BufferedInputStream(fis);
        FileParser parser = new KML22RouteParser();
        assertThat(parser.isParsableFile(bis)).isTrue();
    }
}

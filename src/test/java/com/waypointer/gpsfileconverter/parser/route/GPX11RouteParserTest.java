package com.waypointer.gpsfileconverter.parser.route;

import com.waypointer.gpsfileconverter.GpsRoute;
import com.waypointer.gpsfileconverter.parser.FileParser;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GPX11RouteParserTest {

    private final String pathRoute = getClass().getResource("/parsing/routes/route_test_1_1.gpx").getFile();
    private final String pathTrack = getClass().getResource("/parsing/routes/track_test_1_1.gpx").getFile();
    private final String pathRouteIsEmpty = getClass().getResource("/parsing/routes/route_test_1_1_is_empty.gpx").getFile();
    private final String pathTrackIsEmpty = getClass().getResource("/parsing/routes/track_test_1_1_is_empty.gpx").getFile();
    private final String pathRoutesTrackMix = getClass().getResource("/parsing/routes/routes_tracks_mix_1_1.gpx").getFile();
    private final String pathMarmarosi = getClass().getResource("/parsing/routes/Marmarosi_2013_05_5days.gpx").getFile();

    @Test
    public void testFileParsingRealTrack() throws Exception {
        FileInputStream fis = new FileInputStream(pathMarmarosi);

        FileParser<GpsRoute> parser = new GPX11RouteParser();
        List<GpsRoute> routes = parser.parseFile(new BufferedInputStream(fis));
        assertThat(routes).hasSize(1);

        GpsRoute route = routes.get(0);
        assertThat(route.getRouteName()).isEqualTo("Marmarosi_2013_05_5days on GPSies.com");
        assertThat(route.getPoints()).hasSize(878);
    }

    @Test
    public void testFileParsingRoute() throws Exception {
        FileInputStream fis = new FileInputStream(pathRoute);

        FileParser<GpsRoute> parser = new GPX11RouteParser();
        List<GpsRoute> routes = parser.parseFile(new BufferedInputStream(fis));
        assertThat(routes).hasSize(3);

        GpsRoute route = routes.get(0);
        assertThat(route.getRouteName()).isEqualTo("Alex's Route");
        assertThat(route.getPoints()).hasSize(5);
    }

    @Test
    public void testFileParsingRouteWithoutPointsIntoFile() throws Exception {
        FileInputStream fis = new FileInputStream(pathRouteIsEmpty);

        FileParser<GpsRoute> parser = new GPX11RouteParser();
        List<GpsRoute> routes = parser.parseFile(new BufferedInputStream(fis));
        assertThat(routes).isNotNull();
        assertThat(routes).isEmpty();
    }

    @Test
    public void testFileParsingTrack() throws Exception {
        FileInputStream fis = new FileInputStream(pathTrack);

        FileParser<GpsRoute> parser = new GPX11RouteParser();
        List<GpsRoute> routes = parser.parseFile(new BufferedInputStream(fis));
        assertThat(routes).hasSize(3);

        GpsRoute route = routes.get(0);
        assertThat(route.getRouteName()).isEqualTo("Ann's Track");
        assertThat(route.getPoints()).hasSize(5);
    }

    @Test
    public void testFileParsingTrackWithoutPointsIntoFile() throws Exception {
        FileInputStream fis = new FileInputStream(pathTrackIsEmpty);

        FileParser<GpsRoute> parser = new GPX11RouteParser();
        List<GpsRoute> routes = parser.parseFile(new BufferedInputStream(fis));
        assertThat(routes).isNotNull();
        assertThat(routes).isEmpty();
    }

    @Test
    public void testFileParsingMix() throws Exception {
        FileInputStream fis = new FileInputStream(pathRoutesTrackMix);

        FileParser<GpsRoute> parser = new GPX11RouteParser();
        List<GpsRoute> routes = parser.parseFile(new BufferedInputStream(fis));
        assertThat(routes).hasSize(5);

        GpsRoute route = routes.get(0);
        assertThat(route.getRouteName()).isEqualTo("Alex's Route");
        assertThat(route.getPoints()).hasSize(5);
        assertThat(routes.get(4).getRouteName()).isEqualTo("Alex's Track");
        assertThat(routes.get(4).getPoints()).hasSize(8);
    }

    @Test
    public void testFileDetection() throws Exception {
        FileInputStream fis = new FileInputStream(pathRoute);

        BufferedInputStream bis = new BufferedInputStream(fis);
        FileParser<GpsRoute> parser = new GPX11RouteParser();
        assertThat(parser.isParsableFile(bis)).isTrue();
    }

    @Test
    public void testIntegrity() throws Exception {
        FileInputStream fis = new FileInputStream(pathRoute);

        BufferedInputStream bis = new BufferedInputStream(fis);
        FileParser<GpsRoute> parser = new GPX11RouteParser();
        assertThat(parser.isParsableFile(bis)).isTrue();

        List<GpsRoute> routes = parser.parseFile(bis);
        assertThat(routes).hasSize(3);
        int size = 5;
        for (GpsRoute route : routes) {
            assertThat(route.getPoints()).hasSize(size);
            size--;
        }
    }
}

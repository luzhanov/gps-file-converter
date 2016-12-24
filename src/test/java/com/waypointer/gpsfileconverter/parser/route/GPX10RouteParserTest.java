package com.waypointer.gpsfileconverter.parser.route;

import com.waypointer.gpsfileconverter.GpsRoute;
import com.waypointer.gpsfileconverter.parser.FileParser;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GPX10RouteParserTest {

    private final String pathRoute = getClass().getResource("/parsing/routes/route_test_1_0.gpx").getFile();

    private final String pathTrack = getClass().getResource("/parsing/routes/track_test_1_0.gpx").getFile();

    private final String pathRouteIsEmpty = getClass().getResource("/parsing/routes/route_test_1_0_is_empty.gpx").getFile();

    private final String pathTrackIsEmpty = getClass().getResource("/parsing/routes/track_test_1_0_is_empty.gpx").getFile();

    private final String pathBowell = getClass().getResource("/parsing/routes/route_Bouwel_10.gpx").getFile();

    private final String pathRoutesTrackMix = getClass().getResource("/parsing/routes/routes_tracks_mix_1_0.gpx").getFile();

    @Test
    public void testPathBowell() throws Exception {
        FileInputStream fis = new FileInputStream(pathBowell);

        FileParser<GpsRoute> parser = new GPX10RouteParser();
        List<GpsRoute> routes = parser.parseFile(new BufferedInputStream(fis));
        assertThat(routes).hasSize(1);

        assertThat(routes.get(0).getPoints()).hasSize(51);
        assertThat(routes.get(0).getPoints().get(50).getLatitude()).isEqualTo(51.169664);
        assertThat(routes.get(0).getPoints().get(50).getLongitude()).isEqualTo(4.74302);
    }

    @Test
    public void testFileParsingRoute() throws Exception {
        FileInputStream fis = new FileInputStream(pathRoute);

        FileParser<GpsRoute> parser = new GPX10RouteParser();
        List<GpsRoute> routes = parser.parseFile(new BufferedInputStream(fis));
        assertThat(routes).hasSize(3);

        GpsRoute route = routes.get(0);
        assertThat(route.getRouteName()).isEqualTo("Alex's Route");
        assertThat(route.getPoints()).hasSize(3);
    }

    @Test
    public void testFileParsingRouteWithoutPointsIntoFile() throws Exception {
        FileInputStream fis = new FileInputStream(pathRouteIsEmpty);

        FileParser<GpsRoute> parser = new GPX10RouteParser();
        List<GpsRoute> routes = parser.parseFile(new BufferedInputStream(fis));
        assertThat(routes).isNotNull();
        assertThat(routes).isEmpty();
    }

    @Test
    public void testFileParsingTrack() throws Exception {
        FileInputStream fis = new FileInputStream(pathTrack);

        FileParser<GpsRoute> parser = new GPX10RouteParser();
        List<GpsRoute> routes = parser.parseFile(new BufferedInputStream(fis));
        assertThat(routes).hasSize(3);

        GpsRoute route = routes.get(0);
        assertThat(route.getRouteName()).isEqualTo("Patrick's Track");
        assertThat(route.getPoints()).hasSize(3);
    }

    @Test
    public void testFileParsingTrackWithoutPointsIntoFile() throws Exception {
        FileInputStream fis = new FileInputStream(pathTrackIsEmpty);

        FileParser<GpsRoute> parser = new GPX10RouteParser();
        List<GpsRoute> routes = parser.parseFile(new BufferedInputStream(fis));
        assertThat(routes).isNotNull();
        assertThat(routes).isEmpty();
    }

    @Test
    public void testFileParsingMix() throws Exception {
        FileInputStream fis = new FileInputStream(pathRoutesTrackMix);

        FileParser<GpsRoute> parser = new GPX10RouteParser();
        List<GpsRoute> routes = parser.parseFile(new BufferedInputStream(fis));
        assertThat(routes).isNotNull();
        assertThat(routes).hasSize(5);

        GpsRoute route = routes.get(0);
        assertThat(route.getRouteName()).isEqualTo("Alex's Route");
        assertThat(route.getPoints()).hasSize(3);
        assertThat(routes.get(4).getRouteName()).isEqualTo("Alex's Track");
        assertThat(routes.get(4).getPoints()).hasSize(8);
    }

    @Test
    public void testFileDetection() throws Exception {
        FileInputStream fis = new FileInputStream(pathRoute);

        BufferedInputStream bis = new BufferedInputStream(fis);
        FileParser<GpsRoute> parser = new GPX10RouteParser();
        assertThat(parser.isParsableFile(bis)).isTrue();
    }

    @Test
    public void testIntegrity() throws Exception {
        FileInputStream fis = new FileInputStream(pathRoute);

        BufferedInputStream bis = new BufferedInputStream(fis);
        FileParser<GpsRoute> parser = new GPX10RouteParser();
        assertThat(parser.isParsableFile(bis)).isTrue();

        List<GpsRoute> routes = parser.parseFile(bis);
        assertThat(routes).hasSize(3);
        int size = 3;
        for (GpsRoute route : routes) {
            assertThat(route.getPoints()).hasSize(size);
            size++;
        }
    }
}

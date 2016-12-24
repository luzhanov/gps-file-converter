package com.waypointer.gpsfileconverter.generator.route;

import com.waypointer.gpsfileconverter.GpsRoute;
import com.waypointer.gpsfileconverter.GpsRoutePoint;
import com.waypointer.gpsfileconverter.generator.FileGenerator;
import com.waypointer.gpsfileconverter.parser.route.KML22RouteParser;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.waypointer.gpsfileconverter.TestUtils.randomLatitude;
import static com.waypointer.gpsfileconverter.TestUtils.randomLongitude;
import static org.assertj.core.api.Assertions.assertThat;

public class KML22RouteGeneratorTest {

    @Test
    public void testRouteCoordinates() throws Exception {
        List<GpsRoutePoint> routePoints1 = new ArrayList<>();
        routePoints1.add(new GpsRoutePoint(randomLatitude(), randomLongitude(), 1));
        routePoints1.add(new GpsRoutePoint(randomLatitude(), randomLongitude(), 2));

        List<GpsRoute> rtList = new ArrayList<>();
        GpsRoute rt1 = new GpsRoute("alex's route", routePoints1);

        List<GpsRoutePoint> routePoints2 = new ArrayList<>();
        routePoints2.add(new GpsRoutePoint(randomLatitude(), randomLongitude(), 1));
        routePoints2.add(new GpsRoutePoint(randomLatitude(), randomLongitude(), 2));
        routePoints2.add(new GpsRoutePoint(randomLatitude(), randomLongitude(), 3));
        GpsRoute rt2 = new GpsRoute("igor's route", routePoints2);

        rtList.add(rt1);
        rtList.add(rt2);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileGenerator<GpsRoute> generator = new KML22RouteGenerator();
        generator.generateFile(baos, rtList);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        KML22RouteParser parser = new KML22RouteParser();
        List<GpsRoute> resultList = parser.parseFile(new BufferedInputStream(bais));
        assertThat(resultList).hasSize(2);
        assertThat(resultList.get(0).getRouteName()).isEqualTo("alex's route on Waypointer.info");
        assertThat(resultList.get(0).getPoints()).hasSize(2);
        assertThat(resultList.get(1).getPoints()).hasSize(3);
    }
}

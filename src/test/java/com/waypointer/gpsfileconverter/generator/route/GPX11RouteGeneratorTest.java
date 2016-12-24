package com.waypointer.gpsfileconverter.generator.route;

import com.waypointer.gpsfileconverter.GpsRoute;
import com.waypointer.gpsfileconverter.GpsRoutePoint;
import com.waypointer.gpsfileconverter.generator.FileGenerator;
import com.waypointer.gpsfileconverter.parser.route.GPX11RouteParser;
import org.assertj.core.data.Offset;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GPX11RouteGeneratorTest {

    @Test
    public void testRoundingCoordinates() throws Exception {
        GpsRoute route1 = new GpsRoute();
        String ownet1 = "igor's route";
        route1.setRouteName(ownet1);

        List<GpsRoutePoint> routePointList1 = new ArrayList<>();
        GpsRoutePoint routePoint1 = new GpsRoutePoint(5.325252, 6.325252, 1);
        GpsRoutePoint routePoint2 = new GpsRoutePoint(6.325252, 7.325252, 2);
        routePointList1.add(routePoint1);
        routePointList1.add(routePoint2);
        route1.setPoints(routePointList1);

        List<GpsRoutePoint> routePointList2 = new ArrayList<>();
        GpsRoute route2 = new GpsRoute();
        String ownet2 = "alex's route";
        route2.setRouteName(ownet2);
        routePointList2.clear();
        routePoint1 = new GpsRoutePoint(7.325252, 8.325252, 1);
        routePoint2 = new GpsRoutePoint(8.325252, 9.325252, 2);
        GpsRoutePoint routePoint3 = new GpsRoutePoint(9.325252, 10.325252, 3);
        routePointList2.add(routePoint1);
        routePointList2.add(routePoint2);
        routePointList2.add(routePoint3);
        route2.setPoints(routePointList2);

        List<GpsRoute> rteList = new ArrayList<>();
        rteList.add(route1);
        rteList.add(route2);

        FileGenerator<GpsRoute> generator = new GPX11RouteGenerator();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        generator.generateFile(baos, rteList);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        GPX11RouteParser parser = new GPX11RouteParser();
        List<GpsRoute> resultList = parser.parseFile(new BufferedInputStream(bais));
        assertThat(resultList).hasSize(2);
        assertThat(resultList.get(0).getRouteName()).isEqualTo(ownet1 + " on Waypointer.info");
        assertThat(resultList.get(1).getRouteName()).isEqualTo(ownet2 + " on Waypointer.info");

        double testValue = 5.325252;

        for (GpsRoute routeResult : resultList) {
            for (GpsRoutePoint routePointResult : routeResult.getPoints()) {
                assertThat(routePointResult.getLatitude()).isEqualTo(testValue, Offset.offset(0.0001));
                assertThat(routePointResult.getLongitude()).isEqualTo(testValue + 1, Offset.offset(0.0001));
                testValue++;
            }
        }

    }
}

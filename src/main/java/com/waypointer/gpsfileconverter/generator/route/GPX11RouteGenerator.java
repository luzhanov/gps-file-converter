package com.waypointer.gpsfileconverter.generator.route;

import com.topografix.gpx._1._1.GpxType;
import com.topografix.gpx._1._1.ObjectFactory;
import com.topografix.gpx._1._1.RteType;
import com.topografix.gpx._1._1.WptType;
import com.waypointer.gpsfileconverter.GpsRoute;
import com.waypointer.gpsfileconverter.GpsRoutePoint;
import com.waypointer.gpsfileconverter.generator.AbstractGPXGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class GPX11RouteGenerator extends AbstractGPXGenerator<GpsRoute> {

    public GpxType fill(List<GpsRoute> routesList) {
        ObjectFactory factory = new ObjectFactory();
        GpxType resultGPX = factory.createGpxType();

        fillMetadata(factory, resultGPX);

        List<RteType> routes = resultGPX.getRte();

        for (GpsRoute currRoute : routesList) {
            RteType routeRte = factory.createRteType();

            routeRte.setName(currRoute.getRouteName() + " on Waypointer.info");

            List<WptType> rteptList = routeRte.getRtept();
            for (GpsRoutePoint currRoutePointType : currRoute.getPoints()) {
                WptType rou = new WptType();
                rou.setLat(new BigDecimal(currRoutePointType.getLatitude()).setScale(6, RoundingMode.HALF_UP));
                rou.setLon(new BigDecimal(currRoutePointType.getLongitude()).setScale(6, RoundingMode.HALF_UP));
                rteptList.add(rou);
            }

            routes.add(routeRte);
        }

        return resultGPX;
    }


}

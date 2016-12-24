package com.waypointer.gpsfileconverter.parser.route;

import com.topografix.gpx._1._1.*;
import com.waypointer.gpsfileconverter.GpsRoute;
import com.waypointer.gpsfileconverter.GpsRoutePoint;
import com.waypointer.gpsfileconverter.parser.AbstractGPX11Parser;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GPX11RouteParser extends AbstractGPX11Parser<GpsRoute> {

    public List<GpsRoute> readObjectsFromGpxType(GpxType gpxType) {
        List<GpsRoute> resultList = new ArrayList<>();

        List<RteType> routesList = gpxType.getRte();
        List<TrkType> trackList = gpxType.getTrk();

        for (RteType rte : routesList) {
            resultList.add(parseRoute(rte));
        }
        for (TrkType trk : trackList) {
            resultList.add(parseTrack(trk));
        }
        if (resultList.isEmpty()) {
            logger.warn("Route parsing: result is empty");
        }

        return resultList;
    }

    private GpsRoute parseRoute(RteType rteRoute) {
        GpsRoute newRoute = new GpsRoute();

        newRoute.setRouteName(rteRoute.getName());

        List<GpsRoutePoint> routePointList = new ArrayList<>();
        int order = 1;
        for (WptType currRteptType : rteRoute.getRtept()) {
            GpsRoutePoint newRoutePoint = new GpsRoutePoint();
            newRoutePoint.setLatitude(currRteptType.getLat().doubleValue());
            newRoutePoint.setLongitude(currRteptType.getLon().doubleValue());
            newRoutePoint.setOrder(order);
            order++;
            routePointList.add(newRoutePoint);
        }
        newRoute.setPoints(routePointList);

        return newRoute;
    }

    private GpsRoute parseTrack(TrkType track) {
        GpsRoute newRoute = new GpsRoute();

        newRoute.setRouteName(track.getName());

        List<GpsRoutePoint> routePointList = new ArrayList<>();
        int order = 1;
        for (TrksegType trksegType : track.getTrkseg()) {
            for (WptType trkpt : trksegType.getTrkpt()) {
                GpsRoutePoint newRoutePoint = new GpsRoutePoint();
                newRoutePoint.setLatitude(trkpt.getLat().doubleValue());
                newRoutePoint.setLongitude(trkpt.getLon().doubleValue());
                newRoutePoint.setOrder(order);
                order++;
                routePointList.add(newRoutePoint);
            }
            newRoute.setPoints(routePointList);
        }

        return newRoute;
    }

}

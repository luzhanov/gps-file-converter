package com.waypointer.gpsfileconverter.parser.route;

import com.topografix.gpx._1._0.Gpx;
import com.waypointer.gpsfileconverter.GpsRoute;
import com.waypointer.gpsfileconverter.GpsRoutePoint;
import com.waypointer.gpsfileconverter.parser.AbstractGPX10Parser;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GPX10RouteParser extends AbstractGPX10Parser<GpsRoute> {

    public List<GpsRoute> readObjectsFromGpxType(Gpx gpxType) {
        List<GpsRoute> resultList = new ArrayList<>();

        List<Gpx.Rte> routesList = gpxType.getRte();
        List<Gpx.Trk> trackList = gpxType.getTrk();

        for (Gpx.Rte rte : routesList) {
            resultList.add(parseRoute(rte));
        }
        for (Gpx.Trk trk : trackList) {
            resultList.add(parseTrack(trk));
        }
        if (resultList.isEmpty()) {
            logger.warn("Route parsing: result is empty");
        }

        return resultList;
    }

    private GpsRoute parseRoute(Gpx.Rte rteRoute) {
        GpsRoute newRoute = new GpsRoute();
        newRoute.setRouteName(rteRoute.getName());

        int order = 1;
        List<GpsRoutePoint> routePointList = new ArrayList<>();
        for (Gpx.Rte.Rtept currRteptType : rteRoute.getRtept()) {
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

    private GpsRoute parseTrack(Gpx.Trk track) {
        GpsRoute newRoute = new GpsRoute();
        newRoute.setRouteName(track.getName());

        int order = 1;
        List<GpsRoutePoint> routePointList = new ArrayList<>();
        for (Gpx.Trk.Trkseg currTrkseg : track.getTrkseg()) {
            for (Gpx.Trk.Trkseg.Trkpt currTrkptType : currTrkseg.getTrkpt()) {
                GpsRoutePoint newRoutePoint = new GpsRoutePoint();
                newRoutePoint.setLatitude(currTrkptType.getLat().doubleValue());
                newRoutePoint.setLongitude(currTrkptType.getLon().doubleValue());
                newRoutePoint.setOrder(order++);
                routePointList.add(newRoutePoint);
            }
        }
        newRoute.setPoints(routePointList);

        return newRoute;
    }
}

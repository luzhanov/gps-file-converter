package com.waypointer.gpsfileconverter;


import com.waypointer.gpsfileconverter.generator.FileGenerator;
import com.waypointer.gpsfileconverter.generator.route.GPX11RouteGenerator;
import com.waypointer.gpsfileconverter.generator.route.KML22RouteGenerator;
import com.waypointer.gpsfileconverter.generator.waypoint.GPX11Generator;
import com.waypointer.gpsfileconverter.generator.waypoint.KML22Generator;
import com.waypointer.gpsfileconverter.generator.waypoint.WPTGenerator;
import com.waypointer.gpsfileconverter.parser.FileParser;
import com.waypointer.gpsfileconverter.parser.route.*;
import com.waypointer.gpsfileconverter.parser.waypoint.*;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;

import static com.waypointer.gpsfileconverter.FileConverterUtils.getFirst2KBFromIs;

@Slf4j
//todo: cover with tests
public final class ConverterProvider {

    private ConverterProvider() {
    }

    public static FileGenerator<GpsPoint> getWaypointGenerator(GpsFileExtension extension) {
        FileGenerator<GpsPoint> result = null;
        if (GpsFileExtension.GPX == extension) {
            result = new GPX11Generator();
        } else if (GpsFileExtension.KML == extension) {
            result = new KML22Generator();
        } else if (GpsFileExtension.WPT == extension) {
            result = new WPTGenerator();
        }
        return result;
    }

    public static FileGenerator<GpsRoute> getRouteGenerator(GpsFileExtension extension) {
        FileGenerator<GpsRoute> result = null;
        if (GpsFileExtension.GPX == extension) {
            result = new GPX11RouteGenerator();
        } else if (GpsFileExtension.KML == extension) {
            result = new KML22RouteGenerator();
        }
        return result;
    }

    public static FileParser<GpsRoute> getRouteParser(BufferedInputStream bufferedIs, GpsFileExtension extension) throws IOException {
        FileParser<GpsRoute> result;
        if (GpsFileExtension.GPX == extension) {
            FileParser<GpsRoute> tmpParser = new GPX11RouteParser();
            if (tmpParser.isParsableFile(bufferedIs)) {
                return tmpParser;
            }
            tmpParser = new GPX10RouteParser();
            if (tmpParser.isParsableFile(bufferedIs)) {
                return tmpParser;
            }

            result = new GPX11RouteParser();
        } else if (GpsFileExtension.KML == extension) {
            FileParser<GpsRoute> tmpParser = new KML22RouteParser();
            if (tmpParser.isParsableFile(bufferedIs)) {
                return tmpParser;
            }
            tmpParser = new KML21RouteParser();
            if (tmpParser.isParsableFile(bufferedIs)) {
                return tmpParser;
            }
            tmpParser = new KML20RouteParser();
            if (tmpParser.isParsableFile(bufferedIs)) {
                return tmpParser;
            }

            result = new KML22RouteParser();
        } else {
            logger.error("Unsupported route extension, returning GPX: " + extension);
            result = new GPX11RouteParser();
        }

        return result;
    }

    public static  FileParser<GpsPoint> getWaypointParser(BufferedInputStream bufferedIs, GpsFileExtension extension) throws IOException {
        FileParser<GpsPoint> result;
        if (GpsFileExtension.GPX == extension) {
            result = tryGPXParsers(bufferedIs);
            if (result == null) {
                result =  new GPX11Parser();
            }
        } else if (GpsFileExtension.KML == extension) {
            result = tryKMLParsers(bufferedIs);
            if (result == null) {
                result = new KML22Parser();
            }
        } else if (GpsFileExtension.WPT == extension) {
            result = tryWptParsers(bufferedIs);
            if (result == null) {
                result = new WPTParser();
            }
        } else {
            result = forceTryParsers(bufferedIs);
        }
        return result;
    }

    private static FileParser<GpsPoint> forceTryParsers(BufferedInputStream bufferedIs) throws IOException {
        FileParser<GpsPoint> result;
        result = tryGPXParsers(bufferedIs);
        if (result == null) {
            result = tryKMLParsers(bufferedIs);
        }
        if (result == null) {
            result = tryWptParsers(bufferedIs);
        }
        if (result == null) {
            String firstString = getFirst2KBFromIs(bufferedIs);
            throw new IllegalArgumentException("Impossible to detect parser for file, beginning with [" + firstString + "]");
        }
        return result;
    }

    private static FileParser<GpsPoint> tryWptParsers(BufferedInputStream bufferedIs) throws IOException {
        FileParser<GpsPoint> tmpParser = new WPTParser();
        if (tmpParser.isParsableFile(bufferedIs)) {
            return tmpParser;
        }
        return null;
    }

    private static FileParser<GpsPoint> tryKMLParsers(BufferedInputStream bufferedIs) throws IOException {
        FileParser<GpsPoint> tmpParser = new KML22Parser();
        if (tmpParser.isParsableFile(bufferedIs)) {
            return tmpParser;
        }
        tmpParser = new KML21Parser();
        if (tmpParser.isParsableFile(bufferedIs)) {
            return tmpParser;
        }
        tmpParser = new KML20Parser();
        if (tmpParser.isParsableFile(bufferedIs)) {
            return tmpParser;
        }
        return null;
    }

    private static FileParser<GpsPoint> tryGPXParsers(BufferedInputStream bufferedIs) throws IOException {
        FileParser<GpsPoint> tmpParser = new GPX11Parser();
        if (tmpParser.isParsableFile(bufferedIs)) {
            return tmpParser;
        }
        tmpParser = new GPX10Parser();
        if (tmpParser.isParsableFile(bufferedIs)) {
            return tmpParser;
        }
        return null;
    }

}

package com.waypointer.gpsfileconverter.parser.route;

import com.waypointer.gpsfileconverter.parser.FileParser;

import java.io.BufferedInputStream;
import java.io.IOException;

public class KML21RouteParser extends KML22RouteParser {

    @Override
    public boolean isParsableFile(BufferedInputStream is) throws IOException {
        return FileParser.isParsableFileCheck(is, "http://earth.google.com/kml/2.1");
    }
}

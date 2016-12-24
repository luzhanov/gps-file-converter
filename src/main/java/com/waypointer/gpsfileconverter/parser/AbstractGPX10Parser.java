package com.waypointer.gpsfileconverter.parser;

import com.topografix.gpx._1._0.Gpx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractGPX10Parser<T> extends AbstractGPXParser<T, Gpx> {

    public AbstractGPX10Parser() {
        super("com.topografix.gpx._1._0", "http://www.topografix.com/GPX/1/0");
    }

    @Override
    protected Gpx readRootElement(Object rawObject) {
        return (Gpx) rawObject;
    }

}

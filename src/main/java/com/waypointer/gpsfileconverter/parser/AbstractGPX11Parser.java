package com.waypointer.gpsfileconverter.parser;

import com.topografix.gpx._1._1.GpxType;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBElement;

@Slf4j
public abstract class AbstractGPX11Parser<T> extends AbstractGPXParser<T, GpxType> {

    public AbstractGPX11Parser() {
        super("com.topografix.gpx._1._1", "xmlns=\"http://www.topografix.com/GPX/1/1\"");
    }

    @Override
    protected GpxType readRootElement(Object rawObject) {
        return (GpxType) ((JAXBElement) rawObject).getValue();
    }

}

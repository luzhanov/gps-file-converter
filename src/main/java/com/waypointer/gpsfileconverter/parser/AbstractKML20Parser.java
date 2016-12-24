package com.waypointer.gpsfileconverter.parser;

import com.google.earth.kml._2.Kml;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

@Slf4j
public abstract class AbstractKML20Parser<T> implements FileParser<T> {

    @Override
    public List<T> parseFile(BufferedInputStream is) {
        try {
            JAXBContext jc = JAXBContext.newInstance("com.google.earth.kml._2");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            Kml rootKml = (Kml) unmarshaller.unmarshal(is);

            return readObjectsFromKml20Type(rootKml);
        } catch (JAXBException e) {
            logger.error("Error during KML 2.0 unmarshalling", e);
            throw new IllegalStateException("Error during parsing of KML 2.0 file", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                logger.error("Error during stream close", e);
            }
        }
    }

    @Override
    public boolean isParsableFile(BufferedInputStream is) throws IOException {
        return FileParser.isParsableFileCheck(is, "http://earth.google.com/kml/2.0");
    }

    protected abstract List<T> readObjectsFromKml20Type(Kml rootKml);

}

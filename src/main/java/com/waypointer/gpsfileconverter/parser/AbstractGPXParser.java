package com.waypointer.gpsfileconverter.parser;

import com.waypointer.gpsfileconverter.FileConverterUtils;
import com.waypointer.gpsfileconverter.GpsPoint;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@AllArgsConstructor
public abstract class AbstractGPXParser<T, G> implements FileParser<T> {

    private String packageName;
    private String nsName;

    protected abstract List<T> readObjectsFromGpxType(G gpxType);

    @SuppressWarnings("unchecked")
    public List<T> parseFile(BufferedInputStream in) {
        try {
            JAXBContext jc = JAXBContext.newInstance(packageName);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            G rootGPX = readRootElement(unmarshaller.unmarshal(in));

            return readObjectsFromGpxType(rootGPX);
        } catch (JAXBException e) {
            logger.error("Error during GPX unmarshalling", e);
            throw new IllegalStateException("Error during parsing of GPX file", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.error("Error during stream close", e);
            }
        }
    }

    protected abstract G readRootElement(Object rawObject);

    public boolean isParsableFile(BufferedInputStream is) throws IOException {
        return FileParser.isParsableFileCheck(is, nsName);
    }

    protected GpsPoint createPoint(double latitude, double longitude, String extractedName, String extractedDesc,
                                   String extractedCmt, XMLGregorianCalendar xmlCalendar) {
        GpsPoint newPoint = new GpsPoint();
        newPoint.setLatitude(latitude);
        newPoint.setLongitude(longitude);

        String name;
        if (isBlank(extractedName)) {
            name = !isBlank(extractedDesc) ? extractedDesc : extractedCmt;
        } else {
            name = extractedName;
        }
        newPoint.setName(name);

        String description;
        if (isBlank(extractedDesc)) {
            description = extractedCmt;
        } else if (!isBlank(extractedCmt)) {
            description = FileConverterUtils.mergeStringIfNotTheSame(extractedDesc, extractedCmt);
        } else {
            description = extractedDesc;
        }
        newPoint.setDescription(description);

        Date createdDate = new Date();

        if (xmlCalendar != null) {
            createdDate = xmlCalendar.toGregorianCalendar().getTime();
        }
        newPoint.setCreatedDate(createdDate);
        return newPoint;
    }
}

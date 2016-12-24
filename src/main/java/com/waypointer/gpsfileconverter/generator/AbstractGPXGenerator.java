package com.waypointer.gpsfileconverter.generator;

import com.topografix.gpx._1._1.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

@Slf4j
public abstract class AbstractGPXGenerator<T> implements FileGenerator<T> {

    @Override
    public void generateFile(OutputStream os, List<T> objectList) {
        try {
            JAXBContext jc = JAXBContext.newInstance("com.topografix.gpx._1._1");
            ObjectFactory factory = new ObjectFactory();
            GpxType rootGPX = fill(objectList);

            JAXBElement<GpxType> element = factory.createGpx(rootGPX);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            marshaller.marshal(element, os);

        } catch (JAXBException e) {
            logger.error("Error during GPS marshalling", e);
            throw new IllegalStateException("Error during generation of GPX 1.1 file", e);
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                logger.error("Error during stream close", e);
            }
        }
    }

    protected void fillMetadata(ObjectFactory factory, GpxType resultGPX) {
        resultGPX.setCreator("Waypointer http://waypointer.info");
        resultGPX.setVersion("1.1");

        //fill metadata
        MetadataType metadata = factory.createMetadataType();
        PersonType author = factory.createPersonType();
        author.setName("Waypointer http://waypointer.info");
        LinkType link = factory.createLinkType();
        link.setText("Waypointer");
        link.setHref("http://waypointer.info");
        author.setLink(link);
        metadata.setAuthor(author);
        resultGPX.setMetadata(metadata);
    }

    protected abstract GpxType fill(List<T> routesList);
}

package com.waypointer.gpsfileconverter.parser;

import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.*;

import java.io.*;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

@Slf4j
public abstract class AbstractKML22Parser<T> implements FileParser<T> {

    @Override
    public List<T> parseFile(BufferedInputStream is) {
        try {
            Kml kml = unmarshalCorrected(is);
            Feature feature = kml.getFeature();
            return parseFeature(feature);
        } catch (SAXException | ParserConfigurationException | JAXBException | UnsupportedEncodingException e) {
            logger.error("Error during KML 2.2 unmarshalling", e);
            throw new IllegalStateException("Error during parsing of KML 2.2 file", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                logger.error("Error during stream close", e);
            }
        }
    }

    private static Kml unmarshalCorrected(final InputStream content) throws SAXException, ParserConfigurationException, JAXBException, UnsupportedEncodingException {
        Unmarshaller unmarshaller = JAXBContext.newInstance(Kml.class).createUnmarshaller();
        Reader reader = new InputStreamReader(content, "UTF-8");
        InputSource input = new InputSource(reader);
        SAXSource saxSource = new SAXSource(new NamespaceFilterXMLReader(false), input);
        return ((Kml) unmarshaller.unmarshal(saxSource));
    }

    @Override
    public boolean isParsableFile(BufferedInputStream is) throws IOException {
        return FileParser.isParsableFileCheck(is, "http://www.opengis.net/kml/2.2", "http://earth.google.com/kml/2.2");
    }

    protected abstract List<T> parseFeature(Feature feature);

    private final static class NamespaceFilterXMLReader implements XMLReader {

        private XMLReader xmlReader;

        public NamespaceFilterXMLReader(boolean validate) throws ParserConfigurationException, SAXException {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);
            parserFactory.setValidating(validate);
            xmlReader = parserFactory.newSAXParser().getXMLReader();
        }

        public ContentHandler getContentHandler() {
            return xmlReader.getContentHandler();
        }

        public DTDHandler getDTDHandler() {
            return xmlReader.getDTDHandler();
        }

        public EntityResolver getEntityResolver() {
            return xmlReader.getEntityResolver();
        }

        public ErrorHandler getErrorHandler() {
            return xmlReader.getErrorHandler();
        }

        public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
            return xmlReader.getFeature(name);
        }

        public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
            return xmlReader.getProperty(name);
        }

        public void parse(InputSource input) throws IOException, SAXException {
            xmlReader.parse(input);
        }

        public void parse(String systemId) throws IOException, SAXException {
            xmlReader.parse(systemId);
        }

        public void setContentHandler(ContentHandler handler) {
            xmlReader.setContentHandler(new NamespaceFilterHandler(handler));
        }

        public void setDTDHandler(DTDHandler handler) {
            xmlReader.setDTDHandler(handler);
        }

        public void setEntityResolver(EntityResolver handler) {
            xmlReader.setEntityResolver(handler);
        }

        public void setErrorHandler(ErrorHandler handler) {
            xmlReader.setErrorHandler(handler);
        }

        public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
            xmlReader.setFeature(name, value);
        }

        public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
            xmlReader.setProperty(name, value);
        }

    }

    private final static class NamespaceFilterHandler implements ContentHandler {

        private final static String E_KML_20 = "http://earth.google.com/kml/2.0";
        private final static String E_KML_21 = "http://earth.google.com/kml/2.1";
        private final static String E_KML_22 = "http://earth.google.com/kml/2.2";
        private final static String KML_22 = "http://www.opengis.net/kml/2.2";

        private ContentHandler contentHandler;

        public NamespaceFilterHandler(ContentHandler contentHandler) {
            this.contentHandler = contentHandler;
        }

        public void startElement(String uri, String localName, String qualifierName, Attributes atts) throws SAXException {
            if (E_KML_20.equals(uri) || E_KML_21.equals(uri) || E_KML_22.equals(uri)) {
                contentHandler.startElement(KML_22, localName, qualifierName, atts);
            } else {
                //hack for KMLs without namespace
                if (uri.trim().isEmpty()) {
                    uri = KML_22;
                }

                contentHandler.startElement(uri, localName, qualifierName, atts);
            }
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            contentHandler.characters(ch, start, length);
        }

        public void endDocument() throws SAXException {
            contentHandler.endDocument();
        }

        public void endElement(String uri, String localName, String qualifierName) throws SAXException {
            contentHandler.endElement(uri, localName, qualifierName);
        }

        public void endPrefixMapping(String prefix) throws SAXException {
            contentHandler.endPrefixMapping(prefix);
        }

        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
            contentHandler.ignorableWhitespace(ch, start, length);
        }

        public void processingInstruction(String target, String data) throws SAXException {
            contentHandler.processingInstruction(target, data);
        }

        public void setDocumentLocator(Locator locator) {
            contentHandler.setDocumentLocator(locator);
        }

        public void skippedEntity(String name) throws SAXException {
            contentHandler.skippedEntity(name);
        }

        public void startDocument() throws SAXException {
            contentHandler.startDocument();
        }

        public void startPrefixMapping(String prefix, String uri) throws SAXException {
            contentHandler.startPrefixMapping(prefix, uri);
        }
    }
}

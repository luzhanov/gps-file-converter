package com.waypointer.gpsfileconverter.generator;

import java.io.OutputStream;
import java.util.List;

public interface FileGenerator<T> {

    void generateFile(OutputStream os, List<T> objectList);

}

package com.kostovtd.bemyguest.manager;

import android.util.Log;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by kostovtd on 08.01.17.
 */

public class XmlManager {

    private static final String TAG = XmlManager.class.getSimpleName();


    public XmlManager() {

    }


    public Document loadFile(File xmlFile) {
        return loadFile(xmlFile.getPath());
    }


    /**
     * Load a XML file.
     * @param filePath The path to the file.
     * @return A {@link Document} object containing the loaded file, or NULL
     * in case of empty file path or exception.
     */
    public Document loadFile(String filePath) {
        Log.d(TAG, "loadFile: hit");

        if(filePath != null && filePath.length() > 0) {
            try {
                Log.i(TAG, "loadFile: loading file " + filePath);
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
                File xmlFile = new File(filePath);
                Document document = documentBuilder.parse(xmlFile);
                document.getDocumentElement().normalize();
                return document;
            } catch (SAXException | IOException | ParserConfigurationException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
                return null;
            }
        } else {
            Log.i(TAG, "loadFile: file path is NULL or empty");
            return null;
        }
    }



    public void updateFile(Document document, File file) {
        updateFile(document, file.getPath());
    }


    public void updateFile(Document document, String filePath) {
        Log.d(TAG, "updateFile: hit");
        if(document != null && filePath != null &&
                filePath.length() > 0) {
            Log.i(TAG, "updateFile: updating file " + filePath);
            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(new File(filePath));
                transformer.transform(source, result);
            } catch (TransformerException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "updateFile: NULL document, or NULL or empty file path");
        }
    }
}

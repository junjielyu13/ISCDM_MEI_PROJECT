/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crypto;


import java.io.BufferedReader;
import java.io.OutputStream;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.security.Security;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xml.security.Init;
import org.apache.xml.security.encryption.XMLCipher;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author alumne
 */
public class XMLCrypto {
    private static final String secretPaht = "/home/alumne/ISCDM_MEI_PROJECT/webapp/src/main/java/crypto/secret.key";

    static {
        Init.init(); // Inicializa la librería de Apache XML Security
        Security.addProvider(new org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI());
    }
    
    private static String loadKeyFromFile() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(secretPaht))) {
            return reader.readLine(); 
        }
    }
    
    public static void outputDOM(Document doc, OutputStream out) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(doc), new StreamResult(out));
    }
    

    // Método para encriptar un elemento del XML
    public static void encryptXML(String inputFilePath, String outputFilePath, String tagNameToEncrypt) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = dbf.newDocumentBuilder().parse(new File(inputFilePath));

        // Obtén el nodo a encriptar (por ejemplo: Resource)
        NodeList elements = doc.getElementsByTagName(tagNameToEncrypt);
        if (elements.getLength() == 0) {
            throw new Exception("No se encontró el elemento: " + tagNameToEncrypt);
        }

        Element elementToEncrypt = (Element) elements.item(0);

        // Configura el cifrado AES-128
        XMLCipher cipher = XMLCipher.getInstance(XMLCipher.AES_128);
        SecretKey secretKey = new SecretKeySpec(loadKeyFromFile().getBytes(), "AES");
        cipher.init(XMLCipher.ENCRYPT_MODE, secretKey);

        // Encripta el nodo completo (no solo el contenido)
        cipher.doFinal(doc, elementToEncrypt, false);

        // Escribe el archivo resultante
        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            outputDOM(doc, fos);
        }
    }

    // Método para desencriptar el XML
    public static void decryptXML(String inputFilePath, String outputFilePath) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = dbf.newDocumentBuilder().parse(new File(inputFilePath));

        // Encuentra el elemento EncryptedData
        NodeList encDataList = doc.getElementsByTagNameNS("*", "EncryptedData");
        if (encDataList.getLength() == 0) {
            throw new Exception("No se encontró ningún dato encriptado.");
        }

        Element encDataElement = (Element) encDataList.item(0);

        // Configura el descifrado
        XMLCipher cipher = XMLCipher.getInstance();
        SecretKey secretKey = new SecretKeySpec(loadKeyFromFile().getBytes(), "AES");
        cipher.init(XMLCipher.DECRYPT_MODE, secretKey);

        cipher.doFinal(doc, encDataElement);

        // Escribe el archivo desencriptado
        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            outputDOM(doc, fos);
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crypto;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;

/**
 *
 * @author alumne
 */
public class VideoCrypto {    
    private static final String secretPaht = "/home/alumne/ISCDM_MEI_PROJECT/webapp/src/main/java/crypto/secret.key";
    
    private static void generateAndSaveKey() throws Exception {
        if (!new File(secretPaht).exists()) {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();
            String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());

            try (FileWriter writer = new FileWriter(secretPaht)) {
                writer.write(base64Key);
            }
        }
    }
    
    private static String loadKeyFromFile() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(secretPaht))) {
            return reader.readLine(); 
        }
    }

    private static SecretKey getKeyFromBase64(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public static void encryptFile(String inputFilePath, String outputFilePath) throws Exception {
        generateAndSaveKey();
        SecretKey key = getKeyFromBase64( loadKeyFromFile());
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        try (
            FileInputStream fis = new FileInputStream(inputFilePath);
            FileOutputStream fos = new FileOutputStream(outputFilePath);
            CipherOutputStream cos = new CipherOutputStream(fos, cipher)
        ) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, bytesRead);
            }
        }
    }

    public static void decryptFile(String inputFilePath, String outputFilePath) throws Exception {
        SecretKey key = getKeyFromBase64( loadKeyFromFile());
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        try (
            FileInputStream fis = new FileInputStream(inputFilePath);
            CipherInputStream cis = new CipherInputStream(fis, cipher);
            FileOutputStream fos = new FileOutputStream(outputFilePath)
        ) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = cis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }
}

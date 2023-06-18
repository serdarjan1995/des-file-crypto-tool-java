import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public class DESCrypto implements CryptoInterface {

    private Cipher cipher;
    private int mode;
    private final String key;
    private final SecretKey secretKey;

    private final Map<String, Integer> modes = Map.of(
            "encrypt", Cipher.ENCRYPT_MODE,
            "decrypt", Cipher.DECRYPT_MODE
    );

    // Cipher.ENCRYPT_MODE
    public DESCrypto(String key, String mode) {
        this.mode = this.modes.get(mode);
        this.key = key;
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance("DES");
            this.secretKey = factory.generateSecret(new DESKeySpec(this.key.getBytes()));
        } catch (InvalidKeySpecException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        this.cipher = this.createDESCipher();
    }

    @Override
    public byte[] encrypt(byte[] in) {
        if (this.mode == Cipher.DECRYPT_MODE) {
            throw new RuntimeException("Cipher in Decrypt mode");
        }

        byte[] bytes;
        try {
            bytes = this.cipher.doFinal(in);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    @Override
    public byte[] decrypt(byte[] in) {
        if (this.mode == Cipher.ENCRYPT_MODE) {
            throw new RuntimeException("Cipher in Encrypt mode");
        }

        byte[] bytes;
        try {
            bytes = this.cipher.doFinal(in);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    @Override
    public void encryptFile(String in, String out) {
        try {
            CipherInputStream cipherInputStream = new CipherInputStream(new FileInputStream(in), this.cipher);
            FileOutputStream fileOutputStream = new FileOutputStream(out);
            this.saveToFile(cipherInputStream, fileOutputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void decryptFile(String in, String out) {
        try {
            FileInputStream fileInputStream = new FileInputStream(in);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(new FileOutputStream(out), this.cipher);
            this.saveToFile(fileInputStream, cipherOutputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveToFile(InputStream inputStream, OutputStream outputStream) {
        byte[] block64Bytes = new byte[64];
        int bytesRead;
        try {
            while ((bytesRead = inputStream.read(block64Bytes)) != -1) {
                outputStream.write(block64Bytes, 0, bytesRead);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void changeMode(String mode) {
        this.mode = this.modes.get(mode);
        this.cipher = this.createDESCipher();
    }

    public Cipher createDESCipher() {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(this.mode, this.secretKey);
            return cipher;
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}

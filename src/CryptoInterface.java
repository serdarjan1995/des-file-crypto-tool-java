public interface CryptoInterface {
    byte[] encrypt(byte[] in);

    byte[] decrypt(byte[] in);

    void encryptFile(String in, String out);

    void decryptFile(String in, String out);
}

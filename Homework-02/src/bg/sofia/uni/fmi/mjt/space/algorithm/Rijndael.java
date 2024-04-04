package bg.sofia.uni.fmi.mjt.space.algorithm;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Rijndael implements SymmetricBlockCipher {

    private static final String ENCRYPTION_ALGORITHM = "AES";

    private static final int KILOBYTE = 1024;

    private final SecretKey secretKey;

    public Rijndael(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void encrypt(InputStream inputStream, OutputStream outputStream) throws CipherException {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new CipherException("Error initializing decryption cipher!", e);
        } catch (InvalidKeyException e) {
            throw new CipherException("Invalid key!", e);
        }

        byte[] dataBytes;
        try {
            dataBytes = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException("Error occurred while reading from the file!", e);
        }

        try (var encryptedOutput = new CipherOutputStream(outputStream, cipher)) {
            encryptedOutput.write(dataBytes);

        } catch (IOException e) {
            throw new CipherException("Error occurred while encrypting!", e);
        }
    }

    @Override
    public void decrypt(InputStream inputStream, OutputStream outputStream) throws CipherException {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new CipherException("Error initializing decryption cipher!", e);
        } catch (InvalidKeyException e) {
            throw new CipherException("Invalid key!", e);
        }

        try (OutputStream decryptedOutputStream = new CipherOutputStream(outputStream, cipher)) {
            byte[] buffer = new byte[KILOBYTE];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                decryptedOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new CipherException("A problem occurred while writing on the output!", e);
        }
    }
}

package bg.sofia.uni.fmi.mjt.space.algorithm;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RijndaelTest {
    private final SecretKey secretkey;

    {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGenerator.init(128);
        secretkey = keyGenerator.generateKey();
    }

    SymmetricBlockCipher cipher = new Rijndael(secretkey);

    @Test
    void testEncryptWithInvalidKey() {
        SymmetricBlockCipher testCipher = new Rijndael(null);
        assertThrows(CipherException.class,
            () -> testCipher.encrypt(new ByteArrayInputStream(" ".getBytes()), new ByteArrayOutputStream()),
            "CipherException expected when invalid key is present!");
    }

    @Test
    void testDecryptWithInvalidKey() {
        SymmetricBlockCipher testCipher = new Rijndael(null);
        assertThrows(CipherException.class,
            () -> testCipher.decrypt(new ByteArrayInputStream(" ".getBytes()), new ByteArrayOutputStream()),
            "CipherException expected when invalid key is present!");
    }

    @Test
    void testEncryptAndDecrypt() throws CipherException, IOException {
        String originalData = "This is a test message.";
        byte[] encryptedData;

        try (ByteArrayOutputStream encryptedOutputStream = new ByteArrayOutputStream()) {
            cipher.encrypt(new ByteArrayInputStream(originalData.getBytes()), encryptedOutputStream);
            encryptedData = encryptedOutputStream.toByteArray();
        }

        try (ByteArrayInputStream encryptedInputStream = new ByteArrayInputStream(encryptedData);
             ByteArrayOutputStream decryptedOutputStream = new ByteArrayOutputStream()) {
            cipher.decrypt(encryptedInputStream, decryptedOutputStream);

            String decryptedData = decryptedOutputStream.toString();
            assertArrayEquals(originalData.getBytes(), decryptedData.getBytes(),
                "There is an error while encrypting or decrypting the string");
        }
    }

    @Test
    void testEncryptThrowsUncheckedIOException() throws IOException {
        InputStream mockInputStream = mock();
        when(mockInputStream.readAllBytes()).thenThrow(new IOException());

        assertThrows(UncheckedIOException.class, () -> cipher.encrypt(mockInputStream, new ByteArrayOutputStream()),
            "UncheckedIOException is expected when there is a problem while reading from the input!");
    }
}

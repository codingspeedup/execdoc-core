package io.github.codingspeedup.execdoc.toolbox.security;

import io.github.codingspeedup.execdoc.toolbox.utilities.StringUtility;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.SecureRandom;

public class SymmetricEndec {

    private static final String AES = "AES";
    private static final String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5PADDING";

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private static final SecureRandom secureRandom = newSecureRandom();

    private final SecretKey secretKey;
    private final IvParameterSpec ivParameterSpec;
    private final Cipher enCipher;
    private final Cipher deCipher;

    @SneakyThrows
    public SymmetricEndec(SecretKey key, byte[] initializationVector) {
        this.secretKey = key;
        ivParameterSpec = new IvParameterSpec(initializationVector);
        enCipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        enCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        deCipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        deCipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
    }

    private static SecureRandom newSecureRandom() {
        return new SecureRandom();
    }

    @SneakyThrows
    public static Pair<SecretKey, byte[]> generateKeyChain() {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(256, getSecureRandom());
        SecretKey key = keyGenerator.generateKey();
        byte[] initializationVector = new byte[16];
        getSecureRandom().nextBytes(initializationVector);
        return Pair.of(key, initializationVector);
    }

    @SneakyThrows
    public static void storeKeyChain(File keyChain, SecretKey key, byte[] initializationVector) {
        ByteArrayOutputStream serializedKey = new ByteArrayOutputStream();
        try (ObjectOutputStream keyStream = new ObjectOutputStream(serializedKey)) {
            keyStream.writeObject(key);
        }
        String key64 = StringUtility.encodeBase64(serializedKey.toByteArray());
        String iv64 = StringUtility.encodeBase64(initializationVector);
        try (Writer writer = new FileWriter(keyChain)) {
            writer.write(key64);
            writer.write("\n");
            writer.write(iv64);
        }
    }

    @SneakyThrows
    public static Pair<SecretKey, byte[]> readKeyChain(File keyChain) {
        try (BufferedReader reader = new BufferedReader(new FileReader(keyChain))) {
            ByteArrayInputStream serializedKey = new ByteArrayInputStream(StringUtility.decodeBase64(reader.readLine()));
            try (ObjectInputStream keyStream = new ObjectInputStream(serializedKey)) {
                SecretKey key = (SecretKey) keyStream.readObject();
                byte[] initializationVector = StringUtility.decodeBase64(reader.readLine());
                return Pair.of(key, initializationVector);
            }
        }
    }

    public static SymmetricEndec from(Pair<SecretKey, byte[]> keyChain) {
        return new SymmetricEndec(keyChain.getLeft(), keyChain.getRight());
    }

    @SneakyThrows
    public String encrypt64(String plainText) {
        byte[] bytes = enCipher.doFinal(plainText.getBytes());
        return StringUtility.encodeBase64(bytes);
    }

    @SneakyThrows
    public String decrypt64(String encryptedTest) {
        byte[] bytes = StringUtility.decodeBase64(encryptedTest);
        return new String(deCipher.doFinal(bytes));
    }

}

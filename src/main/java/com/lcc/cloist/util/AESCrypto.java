package com.lcc.cloist.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AESCrypto {
    private final SecretKey secretKey;
    private final IvParameterSpec ivParameterSpec;

    public AESCrypto() throws Exception {
        secretKey = getKey();
        ivParameterSpec = getIv();
    }

    private SecretKey getKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    private IvParameterSpec getIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public String encrypt(String specName, String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(specName);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.getEncoder().encode(encrypted));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public String decrypt(String specName, String cipherText) {
        try {
            Cipher cipher = Cipher.getInstance(specName);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}

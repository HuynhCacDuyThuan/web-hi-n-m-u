package com.example.demo.until;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyGeneratorUtil {

	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048); // Kích thước khóa
			return keyPairGenerator.generateKeyPair();
		} catch (Exception e) {
			throw new RuntimeException("Error generating RSA key pair", e);
		}
	}
}

package com.example.demo.service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.springframework.stereotype.Service;

import com.example.demo.until.KeyGeneratorUtil;

@Service
public class KeyPairService {
	private KeyPair keyPair;

	public KeyPairService() {
		this.keyPair = generateKeyPair();
	}

	private KeyPair generateKeyPair() {
		return KeyGeneratorUtil.generateKeyPair();
	}

	public PrivateKey getPrivateKey() {
		return keyPair.getPrivate();
	}

	public PublicKey getPublicKey() {
		return keyPair.getPublic();
	}
}

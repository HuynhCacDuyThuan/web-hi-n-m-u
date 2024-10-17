package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.*;
import java.util.Base64;

import com.example.demo.until.HashUtil;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "block")
public class Block {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private int blockIndex; // Đổi tên từ 'index' thành 'blockIndex'

	@Column(nullable = false)
	private long timestamp;

	@Column(length = 255)
	private String data;

	@Column(length = 255)
	private String previousHash;

	@Column(length = 255)
	private String hash;

	@Column(length = 255)
	private String digitalSignature;
	@ManyToOne
	@JoinColumn(name = "id_user")
	private User user;

	public Block(int index, String data, String previousHash, PrivateKey privateKey) {
		this.blockIndex = index;
		this.timestamp = System.currentTimeMillis();
		this.data = data;
		this.previousHash = previousHash;
		this.hash = calculateHash();
		this.digitalSignature = signBlock(privateKey);
	}

	public String calculateHash() {
		String input =blockIndex + timestamp + data + previousHash;
		return HashUtil.Sha256(input);
	}

	private String signBlock(PrivateKey privateKey) {
		try {
			Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initSign(privateKey);
			signature.update(hash.getBytes());
			byte[] digitalSignatureBytes = signature.sign();
			return Base64.getEncoder().encodeToString(digitalSignatureBytes);
		} catch (Exception e) {
			throw new RuntimeException("Error signing block", e);
		}
	}

	public String getDigitalSignature() {
		return digitalSignature;
	}

	public String getHash() {
		return hash;
	}

	public String getPreviousHash() {
		return previousHash;
	}
}

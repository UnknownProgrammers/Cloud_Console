package com.bluefrost.encryption;


import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class Crypto
{
	
	private static PrivateKey prikey;
	public static PrivateKey getPriKey(){return prikey;}
    private static PublicKey pubkey;
	public static PublicKey getPubKey(){return pubkey;}

	public static void genKeys() throws NoSuchAlgorithmException{
		Long start = System.currentTimeMillis();
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048); //1024 used for normal securities
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		System.out.println("It took "+ (System.currentTimeMillis()-start)+" millis");
		prikey = keyPair.getPrivate();
		pubkey = keyPair.getPublic();
	}

	
	@Deprecated public static byte[] encryptData(byte[] data) {
		byte[] encryptedData = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pubkey);
			encryptedData = cipher.doFinal(data);
		} catch (Exception e) {e.printStackTrace();} 
		return encryptedData;
	}

	
	@Deprecated public static byte[] decryptData(byte[] data) {
		byte[] descryptedData = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, prikey);
			descryptedData = cipher.doFinal(data);
		} catch (Exception e) {e.printStackTrace();} 
		return descryptedData;
	}
	

	public static byte[] encryptData(byte[] data, Key k) {
		byte[] encryptedData = null;
		try {
			String alg = "";
			if(k instanceof PublicKey){alg = "RSA";}
			else{alg = "AES";}
			Cipher cipher = Cipher.getInstance(alg);
			cipher.init(Cipher.ENCRYPT_MODE, pubkey);
			encryptedData = cipher.doFinal(data);
		} catch (Exception e) {e.printStackTrace();} 
		return encryptedData;
	}

	
	public static byte[] decryptData(byte[] data, Key k) {
		byte[] descryptedData = null;
		try {
			String alg = "";
			if(k instanceof PrivateKey){alg = "RSA";}
			else{alg = "AES";}
			Cipher cipher = Cipher.getInstance(alg);
			cipher.init(Cipher.DECRYPT_MODE, k);
			descryptedData = cipher.doFinal(data);
		} catch (Exception e) {e.printStackTrace();} 
		return descryptedData;
	}
	
	
	
	
	public static Key randomAESKey(){
		SecureRandom sr = new SecureRandom();
		byte[] key = new byte[16];
		sr.nextBytes(key);
		return new SecretKeySpec(key,"AES");
	}
	
}

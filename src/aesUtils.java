
import java.security.InvalidAlgorithmParameterException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class aesUtils {
	
	public SecretKeySpec generateSecretkeySpec(String password,String salt) 
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		
		var sb = Base64.getDecoder().decode(salt);
		final var key = new PBEKeySpec(password.toCharArray(),sb, 12000,256);
		final var skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		final var kb = skf.generateSecret(key).getEncoded();
		return new SecretKeySpec(kb, "AES");
	}
	
	public IvParameterSpec generateIvSpec(String iv) {
		
		var i = Base64.getDecoder().decode(iv.getBytes());
		return new IvParameterSpec(i);
	}
	
	public HashMap<String,String> encrypt(String ... arg) 
			throws NoSuchAlgorithmException, InvalidKeySpecException, 
			NoSuchPaddingException, InvalidKeyException, 
			InvalidAlgorithmParameterException, 
			IllegalBlockSizeException, BadPaddingException {

			var rand = new SecureRandom();
			var s = new byte[256];
			rand.nextBytes(s);
			
			final var salt = Base64.getEncoder().encodeToString(s);
			final var password = arg[0];
		

			final var keySpec = generateSecretkeySpec(password,salt);
					
			rand = new SecureRandom();
			var i = new byte[16];
			rand.nextBytes(i);
			final var iv = Base64.getEncoder().withoutPadding().encodeToString(i);
			
			final var ivSpec = generateIvSpec(iv);			
			
			
			final var message=arg[1];
			final var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);			
			final var emsg = cipher.doFinal(message.getBytes());
			final var emessage = Base64.getEncoder().encodeToString(emsg);			
			HashMap<String,String> map=new HashMap<String, String>();
			map.put("salt", salt);
			map.put("iv",iv);
			map.put("message", emessage);			
			return map;
			
	}
	
	public String decrypt(HashMap<String,String> map) 
			throws NoSuchAlgorithmException, InvalidKeySpecException, 
			NoSuchPaddingException, InvalidKeyException, 
			InvalidAlgorithmParameterException, IllegalBlockSizeException, 
			BadPaddingException {
		
		final var password = map.get("password");
		final var salt = map.get("salt");
		final var iv = map.get("iv");
		
		var keySpec = generateSecretkeySpec(password, salt);
		var ivSpec = generateIvSpec(iv);
		
		final var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
		
		var msg = Base64.getDecoder().decode(map.get("message").getBytes());
		final var message = new String(cipher.doFinal(msg));

		return message;
	}	
	
		
}

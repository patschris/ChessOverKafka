package security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


/**	
 * <code>SecureLogin</code> is a singleton class that contains a method for
 * 	encrypting a password using a SHA-256 hash function.
 */
public class SecurePassword {
	

	/**	Encrypts a given string-password using SHA-256.
	 * 	@param	base	the password to be encrypted.
	 * 	@return			a String with the encrypted password.
	 */
	public static String sha256(String base) {
		/*
		 http://stackoverflow.com/questions/3103652/hash-string-via-sha-256-in-java
		 */
		try{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
			StringBuilder hexString = new StringBuilder();

			for (byte b : hash) {
				// encrypting the given password.
				String hex = Integer.toHexString(0xff & b);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		}
		catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
}
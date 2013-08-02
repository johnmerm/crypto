package cryptography;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AES {
	enum Mode{
		
		CBC("AES/CBC/PKCS5Padding"),
		CTR("AES/CTR/PKCS5Padding");
		
		private String cipherString;
		private Cipher cipher;
		private Mode(String cipher) {
			try {this.cipherString = cipher;
				this.cipher = Cipher.getInstance(cipher);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	};
	class Record{
		Mode mode;
		String key;
		String encoded;
		public Record(Mode mode, String key, String encoded) {
			this.mode = mode;
			this.key = key;
			this.encoded = encoded;
		}
	}
	
	private Record[] records = new Record[]{
			new Record(Mode.CBC, "140b41b22a29beb4061bda66b6747e14", "4ca00ff4c898d61e1edbf1800618fb2828a226d160dad07883d04e008a7897ee2e4b7465d5290d0c0e6c6822236e1daafb94ffe0c5da05d9476be028ad7c1d81"),
			new Record(Mode.CBC, "140b41b22a29beb4061bda66b6747e14", "5b68629feb8606f9a6667670b75b38a5b4832d0f26e1ab7da33249de7d4afc48e713ac646ace36e872ad5fb8a512428a6e21364b0c374df45503473c5242a253"),
			new Record(Mode.CTR, "36f18357be4dbd77f050515c73fcf9f2", "69dda8455c7dd4254bf353b773304eec0ec7702330098ce7f7520d1cbbb20fc388d1b0adb5054dbd7370849dbf0b88d393f252e764f1f5f7ad97ef79d59ce29f5f51eeca32eabedd9afa9329"),
			new Record(Mode.CTR,"36f18357be4dbd77f050515c73fcf9f2","770b80259ec33beb2561358a9f2dc617e46218c0a53cbeca695ae45faa8952aa0e311bde9d4e01726d3184c34451")
	};
	
	private byte[] parse(String hex){
		byte[] ret = new byte[hex.length()/2];
		
		for (int i=0;i<ret.length;i++){
			String token = hex.substring(i*2,i*2+2);
			ret[i] = Integer.decode("0x"+token).byteValue();
		}
		
		return ret;
	}
	
	private String decode(Record rec) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException{
		Cipher c = rec.mode.cipher;
		
		String ivString = rec.encoded.substring(0,32);
		String cipherText = rec.encoded.substring(32);
		
		c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(parse(rec.key), "AES"),new IvParameterSpec(parse(ivString)));
		
		byte dec[] = c.doFinal(parse(cipherText));
		return new String(dec);
	}
	
	
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		AES aes = new AES();
		for (Record r:aes.records){
			System.out.println(aes.decode(r));
		}
		

	}
}

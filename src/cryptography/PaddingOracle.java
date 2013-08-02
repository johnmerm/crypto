package cryptography;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class PaddingOracle{
	
		
	
	private static byte[] lastBytes(int size,byte... bytes){
		byte[] ret = new byte[size];
		for (int i=0;i<bytes.length;i++){
			ret[ret.length-i-1] = bytes[i];
		}
		
		return ret;
	}
	private static byte[] pad(int pad){
		
		byte[] p = new byte[pad];
		for (byte i=0;i<pad;i++){
			p[i] = (byte) pad;
		}
		return p;
	}
	
	private static byte[] xor(byte[] a,byte[] b){
		byte[] max = a.length>=b.length?a:b;
		byte[] min = a.length<b.length?a:b;
		
		byte[] r = new byte[max.length];
		for (int i=0;i<max.length-min.length;i++){
			r[i]=max[i];
		}
		
		for (int i=0;i<min.length;i++){
			r[max.length-min.length+i] = (byte) (max[max.length-min.length+i] ^ min[i]);
		}
		
		return r;
	}
	
	protected boolean challenge(byte[][] challenge) throws IOException{ //returns false if bad_padding is received
		StringBuilder sb = new StringBuilder();
		for (byte[] challengeBlock:challenge){
			sb.append(Hex.encodeHexString(challengeBlock));
		}
		
		URL url = new URL("http://crypto-class.appspot.com/po?er="+sb.toString());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection(new Proxy(Type.HTTP,new InetSocketAddress("172.28.12.5", 8080)));
		int status = connection.getResponseCode();
		return status!=403;
	}
	
	private static byte[] front(byte b,byte[] c){
		byte[] r = new byte[c.length+1];
		r[0]=b;
		System.arraycopy(c, 0, r, 1, c.length);
		return r;
	}
	
	
	private byte[] attackOneBlock(byte[] previous,byte[] current) throws IOException{
		
		
		byte[] sofar=new byte[0];
		
		while (sofar.length<current.length){
		
			int pos = sofar.length;//pos goes backwards .e.g 0 for the last
			byte[] pad = pad(pos+1);
			byte[] guessArray = front((byte) 0,sofar);
			boolean success = false;
			for (int guess=3;guess<=255;guess++){
				guessArray[0]=(byte)guess;
				//System.out.print(""+Hex.encodeHexString(new byte[]{(byte)guess})+".");
				  
				 byte[] modBlock = xor(previous, xor(pad,guessArray));
				
				if (challenge(new byte[][]{modBlock,current})){
					System.out.println(guess);
					sofar = front((byte)guess,sofar);
					success = true;
					break;
				}
				
			}
			if (!success){
				throw new RuntimeException("Faild to oracle at pos:"+pos);
				//sofar = front((byte)'.',sofar);
			}
		}
		return sofar;
	}
	
	public byte[] attack(byte[][] cipher,int block) throws IOException{
		return attackOneBlock(cipher[block-1],cipher[block]);
	}

	public static void main3(String[] args) throws Exception {
		final byte[][] cipher = new byte[][]{
				Hex.decodeHex("f20bdba6ff29eed7b046d1df9fb70000".toCharArray()),
				Hex.decodeHex("58b1ffb4210a580f748b4ac714c001bd".toCharArray()),
				Hex.decodeHex("4a61044426fb515dad3f21f18aa577c0".toCharArray()),
				Hex.decodeHex("bdf302936266926ff37dbf7035d5eeb4".toCharArray())
		};
		PaddingOracle oracle = new PaddingOracle();
		
		byte[] attack = oracle.attack(cipher,2);
		System.out.println(new String(attack));
	}
	
	public static void main(String[] args) throws DecoderException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		String message="This is the secret message I'm trying to findab";
		byte[][] cipherBytes = null; 
		Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
		final SecretKey key = KeyGenerator.getInstance("AES").generateKey();
		final IvParameterSpec iv = new IvParameterSpec(Hex.decodeHex("f20bdba6ff29eed7b046d1df9fb70000".toCharArray()));
		aes.init(Cipher.ENCRYPT_MODE, key,iv);
		try {
			byte[] response = aes.doFinal(message.getBytes());
			cipherBytes = new byte[response.length/16][];
			for (int i=0;i<cipherBytes.length;i++){
				cipherBytes[i] = new byte[16];
				System.arraycopy(response, i*16, cipherBytes[i], 0, 16);
			}
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//TODO:
		
		PaddingOracle oracle = new PaddingOracle(){
			private Cipher aes = null;
			@Override
			protected boolean challenge(byte[][] challenge) throws IOException {
				if (aes == null){
					try{
						aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
						aes.init(Cipher.DECRYPT_MODE, key,iv);
					}catch (Exception e){
						throw new RuntimeException(e);
					}
				}
				byte[] input = new byte[challenge.length*challenge[0].length];
				for (int i=0;i<challenge.length;i++){
					System.arraycopy(challenge[i], 0, input, i==0?0:i*challenge[i-1].length, challenge[i].length);
				}
				try{
					byte[] response = aes.doFinal(input) ;
				}catch (BadPaddingException p){
					return false;
				} catch (IllegalBlockSizeException e) {
					throw new RuntimeException(e);
				}
				return true;
			}
		};
		/*
		

		
		final byte[][] cipher2 = new byte[][]{
			Hex.decodeHex("f20bdba6ff29eed7".toCharArray()),
			Hex.decodeHex("b046d1df9fb70000".toCharArray()),

			Hex.decodeHex("58b1ffb4210a580f".toCharArray()),
			Hex.decodeHex("748b4ac714c001bd".toCharArray()),

			Hex.decodeHex("4a61044426fb515d".toCharArray()),
			Hex.decodeHex("ad3f21f18aa577c0".toCharArray()),

			Hex.decodeHex("bdf302936266926f".toCharArray()),
			Hex.decodeHex("f37dbf7035d5eeb4".toCharArray())
		};
		*/
		
		byte[] attack = oracle.attack(cipherBytes,2);
		System.out.println(new String(attack));

//		System.out.println(new String(new byte[]{(byte)0x20,(byte)0x73,(byte)0x64,(byte)0x72,(byte)0x6f,(byte)0x57,(byte)0x20,(byte)0x63,(byte)0x69,(byte)0x67,(byte)0x61,(byte)0x4d,(byte)0x20,(byte)0x65,(byte)0x68,(byte)0x54}));
	}
}
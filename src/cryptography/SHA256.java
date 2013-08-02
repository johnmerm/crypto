package cryptography;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Hex;

public class SHA256 {

	
	private static String testFile = "6 - 2 - Generic birthday attack (16 min).mp4";
	private static String targetFile = "6 - 1 - Introduction (11 min).mp4";
	private static String targetDir = "G:\\coursera\\Crypto1\\week3";
	
	
	private static List<byte[]> blocks = new ArrayList<byte[]>();
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		File file = new File(targetDir+File.separatorChar+targetFile);
		BufferedInputStream bis =new BufferedInputStream(new FileInputStream(file));
		byte[] buffer = new byte[1024];
		int r=0;
		while ( (r=bis.read(buffer))!=-1){
			byte [] block= new byte[r];
			System.arraycopy(buffer, 0, block, 0, r);
			blocks.add(block);
		}
		
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte sha[] = null;
		for (int i=0;i<blocks.size();i++){
			byte[] block = blocks.get(blocks.size()-i-1);
			byte[] enc;
			if (sha==null){
				enc = block;
			}else{
				enc = new byte[block.length+sha.length];
				System.arraycopy(block, 0, enc, 0, block.length);
				System.arraycopy(sha, 0, enc,block.length, sha.length);
			}
			sha = digest.digest(enc);
		}
		System.out.println(Hex.encodeHex(sha));
	}
}


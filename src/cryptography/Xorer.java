package cryptography;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class Xorer {

	public static void main(String[] args) throws IOException, DecoderException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while(true){
			String x = br.readLine();
			if (x.length()==0){
				continue;
			}
			String y = br.readLine();
			if (y.length()==0){
				for (int i=0;i<x.length();i++){
					y=y+"F";
				}
			}
			byte[] xb = Hex.decodeHex(x.toCharArray());
			byte[] yb = Hex.decodeHex(y.toCharArray());
			
			byte xbi[]=  new byte[xb.length];
			for (int i=0;i<xbi.length;i++){
				xbi[i] = (byte) (xb[i] ^ yb[i]);
			}
			
			System.out.println(Hex.encodeHexString(xbi));
		}
		
	} 
}

package cryptography;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;

public class DiscreteLogModuloPrime{
	
	private BigInteger lside(BigInteger h,BigInteger g,int x1,BigInteger p){
		return h.multiply(g.modPow(BigInteger.valueOf(x1), p).modInverse(p)).mod(p);
		
	}
	
	private BigInteger rside(BigInteger gB,long x0,BigInteger p){
		return gB.modPow(BigInteger.valueOf(x0), p);
	}
	
	public long log(BigInteger p,BigInteger h,BigInteger g,int n){
		int B = (int)Math.pow(2, n/2);
		
		Map<BigInteger,Integer> map = new TreeMap<BigInteger,Integer>();
		
		long time = System.currentTimeMillis();
		for (int x1=0;x1<B;x1++){
			map.put(lside(h, g, x1, p),x1);
			if (x1 % 10000 == 0) System.out.print('.');
		}
		System.out.println ("\nIndex completed after:"+(System.currentTimeMillis()-time)+" ms");
		
		try {
			FileOutputStream fos = new FileOutputStream("indexMap.out");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(map);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		System.out.println("calc gB");
//		BigInteger gB = g.modPow(BigInteger.valueOf(B), p);
//		BigInteger gB = g.pow(B);
		
		System.out.println("Strating lookup");
		time = System.currentTimeMillis();
		
		for (int x0=0;x0<B;x0++){
			BigInteger rside = rside(g, (long)(B)*(long)x0, p);
			if (x0 % 10000 == 0) System.out.print('*');
			if (map.containsKey(rside)){
				System.out.println("\nLookup completed after:"+(System.currentTimeMillis()-time)+" ms");
				int x1 = map.get(rside);
				long x = x0*B+x1;
				System.out.println("\nx="+x+" x0="+x0+" x1="+x1);
				if (g.modPow(BigInteger.valueOf(x), p).equals(h)){
					return x;
				}
			}
		}
		return -1;
	}
	
	public static void main(String[] args) {
		DiscreteLogModuloPrime o = new DiscreteLogModuloPrime();
		int n=40;
		
		BigInteger p = new BigInteger("13407807929942597099574024998205846127479365820592393377723561443721764030073546976801874298166903427690031858186486050853753882811946569946433649006084171");
		BigInteger g = new BigInteger("11717829880366207009516117596335367088558084999998952205599979459063929499736583746670572176471460312928594829675428279466566527115212748467589894601965568");
		BigInteger h = new BigInteger("3239475104050450443565264378728065788649097520952449527834792452971981976143292558073856937958553180532878928001494706097394108577585732452307673444020333");

		
		
		
		
		
		int x = 1712063078 ;//(int)Math.floor(Math.random()*Math.pow(2, n));
		System.out.println("x = "+x);
		BigInteger hh = g.modPow(BigInteger.valueOf(x), p);
		long xx  = o.log(p, hh, g, n);
		System.out.println("xx = "+xx);
//		System.out.println("x = "+x+" "+(x==xx));

		
		
		
		
	}
}

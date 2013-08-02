package cryptography;

import java.math.BigInteger;
import java.security.KeyPair;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.math3.analysis.function.Sqrt;

public class RSABreak {
	
	public static BigInteger bigIntSqRootFloor(BigInteger x)
	        throws IllegalArgumentException {
	    if (x.compareTo(BigInteger.ZERO) < 0) {
	        throw new IllegalArgumentException("Negative argument.");
	    }
	    // square roots of 0 and 1 are trivial and
	    // y == 0 will cause a divide-by-zero exception
	    if (x == BigInteger.ZERO || x == BigInteger.ONE) {
	        return x;
	    } // end if
	    BigInteger two = BigInteger.valueOf(2L);
	    BigInteger y;
	    // starting with y = x / 2 avoids magnitude issues with x squared
	    for (y = x.divide(two);
	            y.compareTo(x.divide(y)) > 0;
	            y = ((x.divide(y)).add(y)).divide(two));
	    return y;
	} // end bigIntSqRootFloor

	public static BigInteger bigIntSqRootCeil(BigInteger x)
	        throws IllegalArgumentException {
	    if (x.compareTo(BigInteger.ZERO) < 0) {
	        throw new IllegalArgumentException("Negative argument.");
	    }
	    // square roots of 0 and 1 are trivial and
	    // y == 0 will cause a divide-by-zero exception
	    if (x == BigInteger.ZERO || x == BigInteger.ONE) {
	        return x;
	    } // end if
	    BigInteger two = BigInteger.valueOf(2L);
	    BigInteger y;
	    // starting with y = x / 2 avoids magnitude issues with x squared
	    for (y = x.divide(two);
	            y.compareTo(x.divide(y)) > 0;
	            y = ((x.divide(y)).add(y)).divide(two));
	    if (x.compareTo(y.multiply(y)) == 0) {
	        return y;
	    } else {
	        return y.add(BigInteger.ONE);
	    }
	} // end bigIntSqRootCeil
	
	public static BigInteger[] challenge1(BigInteger N) {
		
		BigInteger A = bigIntSqRootCeil(N);
		
		BigInteger xx = (A.pow(2)).subtract(N);
		
		BigInteger x = bigIntSqRootCeil(xx);
		
		BigInteger p = A.subtract(x);
		BigInteger q = A.add(x);
		
		System.out.println("A="+A);
		System.out.println("x="+x);
		System.out.println("p="+p);
		System.out.println("q="+q);
		
		if (p.multiply(q).equals(N)){
			return new BigInteger[]{p,q};
		}else{
			return null;
		}
	}
	
	
	public static BigInteger[] challenge2(BigInteger N){
		BigInteger rN = bigIntSqRootFloor(N);
		BigInteger A = rN,x,p,q;
		
		BigInteger limit = BigInteger.valueOf((long)Math.pow(2, 20));
		
		do{
			A = A.add(BigInteger.ONE);
			x = bigIntSqRootCeil(A.pow(2).subtract(N));
			p = A.subtract(x);
			q = A.add(x);
			
		}while (! ( p.multiply(q).equals(N) || A.compareTo(rN.add(limit))>0) );
		
		System.err.println("A="+A);
		System.err.println("x="+x);
		System.err.println("p="+p);
		System.err.println("q="+q);
		
		if (p.multiply(q).equals(N)){
			return new BigInteger[]{p,q};
		}else{
			return null;
		}
	}
	
	public static BigInteger[] challenge3(BigInteger N){
		//Since A = 3p+2q is odd we must cal in terms of 2A = 3p+2q and 2x = sqrt((2*A)^2-24*N)
		//Following this, 2A = ceil(sqrt(24*N))
		BigInteger A2 = bigIntSqRootCeil(N.multiply(BigInteger.valueOf(24)));
		BigInteger x2 = bigIntSqRootCeil(A2.pow(2).subtract(N.multiply(BigInteger.valueOf(24)))) ;
		BigInteger p = (A2.subtract(x2)).divide(BigInteger.valueOf(6));
		BigInteger q = (A2.add(x2)).divide(BigInteger.valueOf(4));
		
		
		System.out.println(p);
		System.out.println(q);
		
		
		return new BigInteger[]{p,q};
		
	}
	
	
	public static BigInteger challenge4(BigInteger pq[],BigInteger e,BigInteger c){
		BigInteger p = pq[0];
		BigInteger q = pq[1];
		
		BigInteger N = p.multiply(q);
		BigInteger phiN = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		System.out.println("fN="+phiN);
		
		BigInteger d = e.modInverse(phiN);
		System.out.println("d="+d);
		BigInteger m = c.modPow(d, N);
		return m;
		
	}
	
	public static void main(String[] args) throws DecoderException {
		
//		System.out.println("Challenge 1\n");
//		BigInteger pq1[] = challenge1(new BigInteger("179769313486231590772930519078902473361797697894230657273430081157732675805505620686985379449212982959585501387537164015710139858647833778606925583497541085196591615128057575940752635007475935288710823649949940771895617054361149474865046711015101563940680527540071584560878577663743040086340742855278549092581"));
		
		
		
//		System.out.println("Challenge 3\n");
//		BigInteger pq3[] =challenge3(new BigInteger("720062263747350425279564435525583738338084451473999841826653057981916355690188337790423408664187663938485175264994017897083524079135686877441155132015188279331812309091996246361896836573643119174094961348524639707885238799396839230364676670221627018353299443241192173812729276147530748597302192751375739387929"));
		
	
//		BigInteger N = new BigInteger("179769313486231590772930519078902473361797697894230657273430081157732675805505620686985379449212982959585501387537164015710139858647833778606925583497541085196591615128057575940752635007475935288710823649949940771895617054361149474865046711015101563940680527540071584560878577663743040086340742855278549092581");
//		BigInteger e = BigInteger.valueOf(65537L);
//		BigInteger c = new BigInteger("22096451867410381776306561134883418017410069787892831071731839143676135600120538004282329650473509424343946219751512256465839967942889460764542040581564748988013734864120452325229320176487916666402997509188729971690526083222067771600019329260870009579993724077458967773697817571267229951148662959627934791540"); 
//		String cipherString = Hex.encodeHexString( challenge4(challenge1(N),e,c).toByteArray() );
//		String target = cipherString.substring(cipherString.indexOf("00")+"00".length());
//		byte[] targetBytes = Hex.decodeHex(target.toCharArray());
//		
//		System.out.println(new String(targetBytes));

		System.out.println("Challenge 2\n");
		BigInteger pq2[] =challenge2(new BigInteger("648455842808071669662824265346772278726343720706976263060439070378797308618081116462714015276061417569195587321840254520655424906719892428844841839353281972988531310511738648965962582821502504990264452100885281673303711142296421027840289307657458645233683357077834689715838646088239640236866252211790085787877"));
		if (pq2 !=null){
			System.out.println("Ch2 p = "+pq2[0]);
		}
		
		
		/*
		 * Challenge 1

A=13407807929942597099574024998205846127479365820592393377723561443721764030073720664935730272460038784167544890048265955830752343658005756322445518700544291
x=57896044618658097711785492504343953926634992332820282019728792003956564819990
p=13407807929942597099574024998205846127479365820592393377723561443721764030073662768891111614362326998675040546094339320838419523375986027530441562135724301
q=13407807929942597099574024998205846127479365820592393377723561443721764030073778560980348930557750569660049234002192590823085163940025485114449475265364281

		 * Challenge 2 

A=25464796146996183438008816563973942229341454268524157846328581927885777970045810444817548800110193810869973985549488954023184312335376930269142469838897581
x=60587609673697726860620356762589523992295780718687067521305424478575876632477822
p=25464796146996183438008816563973942229341454268524157846328581927885777969985222835143851073249573454107384461557193173304497244814071505790566593206419759
q=25464796146996183438008816563973942229341454268524157846328581927885777970106398054491246526970814167632563509541784734741871379856682354747718346471375403


Challenge 3:
p=21909849592475533092273988531583955898982176093344929030099423584127212078126150044721102570957812665127475051465088833555993294644190955293613411658629209
			Challenge 4:
Factoring lets us break RSA.
		 */
	}
}

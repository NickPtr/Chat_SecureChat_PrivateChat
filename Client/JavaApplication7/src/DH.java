
import java.math.*;
import java.util.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import javax.crypto.spec.IvParameterSpec;

public class DH {

    NewJFrame newJF = new NewJFrame();

    NewJFrame1 newJF1 = new NewJFrame1();
    int User;
    int bitLength = 256;
    int certainty = 10;
    static byte[] key;
    private static final SecureRandom rnd = new SecureRandom();
    BigInteger publicKC, publicCK, publicB, publicA;

    public DH() throws Exception {
        Random randomGenerator = new Random();
        BigInteger generatorValue, primeValue, secretA, secretB, sharedKeyA, sharedKeyB;

        

        User=returnUser(User);
        // on machine 1
        if (User == 1) {
            primeValue = findPrime();
            System.out.println("the prime is " + primeValue);
            generatorValue = findPrimeRoot(primeValue);
            System.out.println("the generator of the prime is " + generatorValue);
            secretA = new BigInteger(bitLength - 2, randomGenerator);
            publicA = generatorValue.modPow(secretA, primeValue);
            Set_public(publicA);
            publicB = return_publicKC(publicB);
            sharedKeyA = publicB.modPow(secretA, primeValue);
            System.out.println("the public key of A is " + publicA);
            System.out.println("the shared key for A is " + sharedKeyA);
            System.out.println("The secret key for A is " + secretA);

            String getAValue = sharedKeyA.toString();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(getAValue.getBytes());

            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));// ??
            }

            String getHexValue = sb.toString();
            System.out.println("hex format in SHA-256 is " + getHexValue);

            key = getAValue.getBytes("UTF-8");

            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            // on machine 2
        } else if (User == 0) {
            primeValue = findPrime();
            System.out.println("the prime is " + primeValue);
            generatorValue = findPrimeRoot(primeValue);
            System.out.println("the generator of the prime is " + generatorValue);
            secretB = new BigInteger(bitLength - 2, randomGenerator);
            publicB = generatorValue.modPow(secretB, primeValue);
            Set_public(publicB);
            publicA = return_publicKC(publicA);
            sharedKeyB = publicA.modPow(secretB, primeValue);
            System.out.println("the public key of B is " + publicB);
            System.out.println("the shared key for B is " + sharedKeyB);
            System.out.println("The secret key for B is " + secretB);
            String getBValue = sharedKeyB.toString();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(getBValue.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));// ??
            }
            String getHexValue = sb.toString();
            System.out.println("hex format in SHA-256 is " + getHexValue);
            key = getBValue.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
        }
    }

    public static byte[] encrypt(String plainText) throws Exception {
        byte[] clean = plainText.getBytes();

        // Generating IV.
        int ivSize = 16;
        byte[] iv = new byte[ivSize];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

        // Encrypt.
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(clean);

        // Combine IV and encrypted part.
        byte[] encryptedIVAndText = new byte[ivSize + encrypted.length];
        System.arraycopy(iv, 0, encryptedIVAndText, 0, ivSize);
        System.arraycopy(encrypted, 0, encryptedIVAndText, ivSize, encrypted.length);

        return encryptedIVAndText;
    }

    public static String decrypt(byte[] encryptedIvTextBytes) throws Exception {

        int ivSize = 16;
        int keySize = 16;

        // Extract IV.
        byte[] iv = new byte[ivSize];
        System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Extract encrypted part.
        int encryptedSize = encryptedIvTextBytes.length - ivSize;
        byte[] encryptedBytes = new byte[encryptedSize];
        System.arraycopy(encryptedIvTextBytes, ivSize, encryptedBytes, 0, encryptedSize);

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

        // Decrypt.
        Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decrypted = cipherDecrypt.doFinal(encryptedBytes);

        return new String(decrypted);
    }

    private static boolean miller_rabin_pass(BigInteger a, BigInteger n) {
        BigInteger n_minus_one = n.subtract(BigInteger.ONE);
        BigInteger d = n_minus_one;
        int s = d.getLowestSetBit();
        d = d.shiftRight(s);
        BigInteger a_to_power = a.modPow(d, n);
        if (a_to_power.equals(BigInteger.ONE)) {
            return true;
        }
        for (int i = 0; i < s - 1; i++) {
            if (a_to_power.equals(n_minus_one)) {
                return true;
            }
            a_to_power = a_to_power.multiply(a_to_power).mod(n);
        }
        if (a_to_power.equals(n_minus_one)) {
            return true;
        }
        return false;
    }

    public static boolean miller_rabin(BigInteger n) {
        for (int repeat = 0; repeat < 20; repeat++) {
            BigInteger a;
            do {
                a = new BigInteger(n.bitLength(), rnd);
            } while (a.equals(BigInteger.ZERO));
            if (!miller_rabin_pass(a, n)) {
                return false;
            }
        }
        return true;
    }

    boolean isPrime(BigInteger r) {
        return miller_rabin(r);
    }

    public List<BigInteger> primeFactors(BigInteger number) {
        BigInteger n = number;
        BigInteger i = BigInteger.valueOf(2);
        BigInteger limit = BigInteger.valueOf(10000);
        List<BigInteger> factors = new ArrayList<BigInteger>();
        while (!n.equals(BigInteger.ONE)) {
            while (n.mod(i).equals(BigInteger.ZERO)) {
                factors.add(i);
                n = n.divide(i);
                if (isPrime(n)) {
                    factors.add(n);
                    return factors;
                }
            }
            i = i.add(BigInteger.ONE);
            if (i.equals(limit)) {
                return factors;
            }
        }
        System.out.println(factors);
        return factors;
    }

    boolean isPrimeRoot(BigInteger g, BigInteger p) {
        BigInteger totient = p.subtract(BigInteger.ONE);
        List<BigInteger> factors = primeFactors(totient);
        int i = 0;
        int j = factors.size();
        for (; i < j; i++) {
            BigInteger factor = factors.get(i);
            BigInteger t = totient.divide(factor);
            if (g.modPow(t, p).equals(BigInteger.ONE)) {
                return false;
            }
        }
        return true;
    }

    String download(String address) {
        String txt = "";
        URLConnection conn = null;
        InputStream in = null;
        try {
            URL url = new URL(address);
            conn = url.openConnection();
            conn.setReadTimeout(10000);
            in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            String encoding = "UTF-8";
            while ((numRead = in.read(buffer)) != -1) {
                txt += new String(buffer, 0, numRead, encoding);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return txt;
    }

    void compareWolfram(BigInteger p) {
        String url = "http://api.wolframalpha.com/v2/query?appid=&input=primitive+root+" + p;
        System.out.println(url);
        String g = download(url);;
        String[] vals = g.split(".plaintext>");
        if (vals.length < 3) {
            System.out.println(g);
        } else {
            System.out.println("wolframalpha generatorValue " + vals[3]);
        }
    }

    BigInteger findPrimeRoot(BigInteger p) {
        int start = 2001;
        if (start == 2) {
            compareWolfram(p);
        }

        for (int i = start; i < 100000000; i++) {
            if (isPrimeRoot(BigInteger.valueOf(i), p)) {
                return BigInteger.valueOf(i);
            }
        }
        return BigInteger.valueOf(0);
    }

    BigInteger findPrime() {
        Random rnd = new Random();
        BigInteger p = BigInteger.ZERO;
        p = new BigInteger(bitLength, certainty, rnd);// sufficiently NSA SAFE?!!
        return p;
    }
    
    void Set_public(BigInteger publicK)
    {
        
        publicKC=publicK;
    }

    BigInteger return_public()
    {
        return publicKC;
    }
    
    BigInteger return_publicKC(BigInteger publicCK)
    {
        this.publicCK=publicCK;
        return publicCK;
    }
    
    public int returnUser(int User)
    {
        this.User=User;
        System.out.println("User: "+User);
        return User; 
    }
    
}

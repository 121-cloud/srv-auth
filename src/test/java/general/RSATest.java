package general;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import otocloud.auth.common.RSAUtil;

import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;


/**
 * zhangyef@yonyou.com on 2016-01-12.
 */
public class RSATest {

    public static final Provider pro = new BouncyCastleProvider();

    public static void keyGen() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.genKeyPair();
        Key publicKey = kp.getPublic();
        Key privateKey = kp.getPrivate();

        System.out.println(publicKey.getFormat());
        System.out.println(privateKey.getFormat());

        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        System.out.println(publicKeyString);

        String plainText = "*123";
        try {
            String encryped = encrypt(publicKey, plainText);
            System.out.println(encryped);

            String decryped = decrypt(privateKey, encryped);
            System.out.println(decryped);
        } catch (Exception ignore) {

        }

        KeyFactory fact = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec pub = fact.getKeySpec(publicKey,
                RSAPublicKeySpec.class);


        RSAPrivateKeySpec priv = fact.getKeySpec(privateKey,
                RSAPrivateKeySpec.class);

        ////////
        saveToFile("public.key", pub.getModulus(),
                pub.getPublicExponent());
        saveToFile("private.key", priv.getModulus(),
                priv.getPrivateExponent());

    }

    private static String decrypt(Key priKey, String data) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(data);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        byte[] plainText = cipher.doFinal(bytes);

        return new String(plainText);
    }

    private static String encrypt(Key pubKey, String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] plainText = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(plainText);
    }

    public static void saveToFile(String fileName,
                                  BigInteger mod, BigInteger exp) throws IOException {
        ObjectOutputStream oout = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(fileName)));
        try {
            oout.writeObject(mod);
            oout.writeObject(exp);
        } catch (Exception e) {
            throw new IOException("Unexpected error", e);
        } finally {
            oout.close();
        }
    }

    public static Keys readFromFile(String fileName) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)));
        BigInteger mod;
        BigInteger exp;

        Keys keys = new Keys();

        try {
            mod = (BigInteger) ois.readObject();
            exp = (BigInteger) ois.readObject();


            keys.setExp(exp);
            keys.setMod(mod);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            ois.close();
        }

        return keys;
    }

    @Before
    public void setUp() {
        Security.addProvider(pro);
    }

//    @Test
    public void testKeyGen() {
        try {
            keyGen();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadPublicKey(){
        String file = "src/test/resources/rsa-key/foo_rsa.pub";

        try {
            PublicKey publicKey = RSAUtil.readPublicKey(file);
            String m = RSAUtil.getPublicSpec(publicKey).getModulus().toString(16);
            System.out.println(m);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testEncrypt(){
        try{
            String privateKeyFile = this.getClass().getResource("/rsa-key/id_rsa").getFile();
            String keyFile = this.getClass().getResource("/rsa-key/id_rsa.pub").getFile();

            String plainText = "*123";
            String encrypted = RSAUtil.encrypt(plainText, keyFile);

            String decrypted = RSAUtil.decrypt(encrypted, privateKeyFile, "ufsoft*123");

            Assert.assertEquals(plainText, decrypted);
        }catch (Exception e){
            Assert.fail();
        }
    }
    @Test
    public void testDecrypt() {

        try {
            //读取资源文件中的私钥, 即 "target/test-classes/rsa-key/foo_rsa"
            String keyFile = this.getClass().getResource("/rsa-key/id_rsa").getFile();

            InputStream is = this.getClass().getResourceAsStream("/rsa-key/id_rsa");

//            BufferedReader in
//                    = new BufferedReader(new InputStreamReader(is));

            System.out.println(keyFile);
            String encrypted =
                    "RSj/sz5mjAzgL5oGFihHoXHosg8SPeCwV/KcFCYEJj/VC68yDbkVe/+I7rT/vDla7TjRSXEbQnoPZOtBCxBt6T1jqWwCUQC5Ban/2EM//DzNnFKZ8T0q/uha0r9+wnfzlPcRel086KWDmfMvQJ2SslwyYfr/wudVP5FokLcm9oRmpYSl6ZDr4KTBMcUj/LsOsSb8oaRGeiufhIamzS+9TEtdEatkMnX3LXeEgLUzHRnZKAJZcnfdSMKqEXGTdqfJTUg7mKHQ2dbnSvWy3Ac124Xya/TypuO1iN96Qszkeo3FJ6iox3XNdgUaoSZtI0Hi27JCO5jYbFU98bS8IEddpg==";

            encrypted = "RYm4GSdq8CekzYbFFz+XNltbFAKR4uae68sl5Lx2EfuXq3ayparLiLZDDETTyFE22J+IqPeH2isudhDGTi" +
                    "/d4sfDnamrOrnrKKKRWtDRNAXXeNB4zDdhPB05t7M2+UTUqyejbrXsDL/eY2LdIR2RH16HC4VkT/eV8/FAyvV3M17Vmqi3+joOvow1mSpnkFHtWDG/h2Xcdisqr+LAxi+fJu9dKHagEo2PvutEHDGc3oGoIgbRuMRDcn/NtsNt09xbThLXY4kx+syp0y0dlom3Mxq1PkZx93ub0/qnbaE7rC5iGzF4KCHR3rEEpvLtzOEIf497/MBaKKvo3/XiYInxog==";
            String plainText = RSAUtil.decrypt(encrypted, is, "ufsoft*123");
            System.out.println(plainText);

        } catch (Exception ignore) {
            System.out.println(ignore);
        }
    }

    static class Keys {
        private BigInteger mod;
        private BigInteger exp;

        public BigInteger getExp() {
            return exp;
        }

        public void setExp(BigInteger exp) {
            this.exp = exp;
        }

        public BigInteger getMod() {
            return mod;
        }

        public void setMod(BigInteger mod) {
            this.mod = mod;
        }
    }
}

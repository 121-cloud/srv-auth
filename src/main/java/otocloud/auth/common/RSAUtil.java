package otocloud.auth.common;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

/**
 * zhangyef@yonyou.com on 2016-01-25.
 */
public class RSAUtil {
    public static final Provider pro = new BouncyCastleProvider();
    protected static final Logger logger = LoggerFactory.getLogger(RSAUtil.class);

    static {
        Security.addProvider(pro);
    }

    public static PrivateKey readPrivateKey(File file, String password) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            logger.warn(e.getLocalizedMessage());
        }

        return readPrivateKey(fileReader, password);
    }

    public static PrivateKey readPrivateKey(String file, String password) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            logger.warn(e.getLocalizedMessage());
        }

        return readPrivateKey(fileReader, password);
    }

    public static PrivateKey readPrivateKey(InputStream is, String password){
        BufferedReader in
                = new BufferedReader(new InputStreamReader(is));
        return readPrivateKey(in, password);
    }

    private static PrivateKey readPrivateKey(Reader fileReader, String password) {
        try {

            PEMParser pemParser = new PEMParser(fileReader);

            Object object = pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            KeyPair kp;
            if (object instanceof PEMEncryptedKeyPair) {
                // Encrypted key - we will use provided password
                PEMEncryptedKeyPair ckp = (PEMEncryptedKeyPair) object;
                PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder().build(password.toCharArray());

                kp = converter.getKeyPair(ckp.decryptKeyPair(decProv));

            } else {
                // Unencrypted key - no password needed
                PEMKeyPair ukp = (PEMKeyPair) object;
                kp = converter.getKeyPair(ukp);
            }


            return kp.getPrivate();
        } catch (Exception e) {
            logger.warn("无法读取私钥.");
        }

        return null;
    }


    public static PublicKey readPublicKey(String file) throws Exception {
        PEMParser pemParser = new PEMParser(new FileReader(file));
        Object object = pemParser.readObject();

        PublicKey pubKey = null;

        if (object instanceof SubjectPublicKeyInfo) {
            SubjectPublicKeyInfo pkInfo = (SubjectPublicKeyInfo) object;
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            pubKey = converter.getPublicKey(pkInfo);

        }

        return pubKey;
    }

    /**
     * 解密字符串.
     *
     * @param encryptedBase64 Base64编码的加密字符串.
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptedBase64, String privateKeyFile, String password) throws Exception {
//        String file = "src/test/resources/rsa-key/foo_rsa";
        PrivateKey privateKey = RSAUtil.readPrivateKey(privateKeyFile, password);

        return doDecrypt(encryptedBase64, privateKey);
    }

    private static String doDecrypt(String encryptedBase64, PrivateKey privateKey) throws Exception{
        byte[] bytes0 = Base64.getDecoder().decode(encryptedBase64);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", pro);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] re = cipher.doFinal(bytes0);

        String plainText = new String(re, "UTF-8");

        return plainText;
    }

    public static String decrypt(String encryptedBase64, InputStream privateKeyFile, String password) throws Exception {
        PrivateKey privateKey = readPrivateKey(privateKeyFile, password);
        return doDecrypt(encryptedBase64, privateKey);
    }


    /**
     * 默认读取 classpath 路径下 rsa-key/id_rsa 私钥文件
     *
     * @param encryptedBase64
     * @param password
     * @return 解密失败, 返回原有文本.
     * @throws Exception
     */
    public static String decrypt(String encryptedBase64, String password) {
        try {
            InputStream is = RSAUtil.class.getResourceAsStream("/rsa-key/id_rsa");
            return decrypt(encryptedBase64, is, password);
        }catch (Exception ignore){
            logger.warn(ignore.getMessage());
        }

        return encryptedBase64;
    }

    /**
     * 输出Base64编码的密文
     *
     * @param plainText
     * @param publicKeyFile
     * @return
     * @throws Exception
     */
    public static String encrypt(String plainText, String publicKeyFile) throws Exception {
        PublicKey publicKey = readPublicKey(publicKeyFile);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", pro);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] bytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 默认读取 classpath 路径下的 rsa-key/id_rsa.pub 公钥文件.
     *
     * @param plainText
     * @return 如果加密过程中发生异常, 则返回原文本.
     * @throws Exception
     */
    public static String encrypt(String plainText) {
        try {
            String keyFile = RSAUtil.class.getResource("/rsa-key/id_rsa.pub").getFile();
            return encrypt(plainText, keyFile);
        } catch (Exception ignore) {
            return plainText;
        }
    }


    /**
     * 获取私钥的详细信息.
     *
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static RSAPrivateCrtKeySpec getPrivateSpec(PrivateKey privateKey) throws Exception {
        KeyFactory keyFac = KeyFactory.getInstance("RSA");
        return keyFac.getKeySpec(privateKey, RSAPrivateCrtKeySpec.class);
    }

    public static RSAPublicKeySpec getPublicSpec(PublicKey publicKey) throws Exception {
        KeyFactory keyFac = KeyFactory.getInstance("RSA");
        return keyFac.getKeySpec(publicKey, RSAPublicKeySpec.class);
    }

}

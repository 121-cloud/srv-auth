package general;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.junit.Test;

/**
 * zhangyef@yonyou.com on 2016-01-12.
 */
public class EncryptPassword {
    @Test
    public void it_should_encrypt_a_password(){
        String userPassword = "*123";

        String encryptedPassword = encryptPassword(userPassword);

        StrongPasswordEncryptor checker = new StrongPasswordEncryptor();
        boolean passed = checker.checkPassword("*123", encryptedPassword);

        System.out.println(encryptedPassword);
        System.out.println(passed);
    }

    private String encryptPassword(String userPassword){
        //使用SHA-256加密算法
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        return passwordEncryptor.encryptPassword(userPassword);
    }
}

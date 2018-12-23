package com.cijianyouqing.traveler.util;

import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Base64;

import com.cijianyouqing.traveler.application.MyApplication;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;

/**
 * Created by xiangpengfei on 2018/11/9.
 */
public class CipherHelper {
    private static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnPCxDD6rWlzdezA7Jzqy4BHNK" +
            "OorGDZWNDsZj0q7mZxqB9X/saZ1IgmcfiguU82kZVhkRBd6TL9wOeCQLz59TqvJq" +
            "9saGKVyLkrp6T+9dn3RwVcgGNwDFsdbERMuYhaH+0080S+c9GE5+ePesz/Mmn6yQ" +
            "zwyJyNQNrf17tVDUDQIDAQAB";
    private static final String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKc8LEMPqtaXN17M" +
            "DsnOrLgEc0o6isYNlY0OxmPSruZnGoH1f+xpnUiCZx+KC5TzaRlWGREF3pMv3A54" +
            "JAvPn1Oq8mr2xoYpXIuSunpP712fdHBVyAY3AMWx1sREy5iFof7TTzRL5z0YTn54" +
            "96zP8yafrJDPDInI1A2t/Xu1UNQNAgMBAAECgYBZprNfu/1sjqIbSq8+1nHXMGOR" +
            "znJonA2pEdue1en3EBywwKg0V6X+ohGgYcO1tQuJjpNfgdMA+q4wf41rW4FgXhg1" +
            "Co+31U1/8UrOSCYNJAjfoHxNgJ1Uz+XWg252iqpeqrdmpzZTuMNrckJTxd6no80u" +
            "OUysruFHfssXPRHMoQJBANVZY2Km+QCkegbUt8gjHIWzdn3ab7aJAp3lWP3TYXaO" +
            "oqcX75/xtC8O2jUeLJjmsYRe+Xpf7h+97AZoeqScx4kCQQDIqssYIQU6Oe2wzlJv" +
            "DNcwdXAycVRkIac4NgMXbLpa6kh4GRd9h3EtkvDE9IbO1wVoNuSo7+v4JXPTNb0s" +
            "w4NlAkADPGHXgzTPn68CH4PSE0ZBeOFZM1Dycc2KW4on/2bBB5TL9/74bBARjqYc" +
            "nCKNByK2IUKjaoUQEi9cQYVqrb4RAkEAjp6tGCon3/laEUai2iCjNOLS6lcX5s7w" +
            "XYgt7FEvpfxLo2gaBPLlosGD9EfjJFE4m5ggLl3PNZ/Q0XWpTY5r7QJAYn4nJL6s" +
            "NFXGiqZtEUW1A6gXRNLM8rVLHZgO9NtjOAQ6tvsU6DebV06TEMWgBmxpPHM1oeH7" +
            "ExWYVRsA2BvHFQ==";

    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    private static final String SECRETKEY_ALIAS = "SecretKeyAlias"; // KeyStore中保存加解密秘钥的别名

    private static CipherHelper mCipherHelper;
    private KeyStore keyStore;

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;


    private CipherHelper() {

    }

    public static CipherHelper getInstence() {
        if (mCipherHelper == null) {
            mCipherHelper = new CipherHelper();
        }
        return mCipherHelper;
    }


    /**
     * 加密方法
     *
     * @param data 需要加密的字符串
     * @return
     */
    public String encrypt(String data) {
        return encryptString(data, SECRETKEY_ALIAS);
    }

    /**
     * 解密方法
     *
     * @param data 需要解密的字符串
     * @return
     */
    public String decrypt(String data) {
        return decryptString(data, SECRETKEY_ALIAS);
    }

    /**
     * 加密方法
     *
     * @param data  需要加密的字符串
     * @param alias KeyStore储存数据的别名
     * @return
     */
    public String encrypt(String data, String alias) {
        return encryptString(data, alias);
    }

    /**
     * 解密方法
     *
     * @param data  需要解密的字符串
     * @param alias KeyStore储存数据的别名
     * @return
     */
    public String decrypt(String data, String alias) {
        return decryptString(data, alias);
    }


    /**
     * 加密方法
     *
     * @param data  　需要加密的字符串
     * @param alias 　KeyStore储存数据的别名
     * @return
     */
    private String encryptString(String data, String alias) {
        if (!TextUtils.isEmpty(alias) && !TextUtils.isEmpty(data)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                initKeyStore(alias);
            }
            try {
                KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
                Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//                inCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

                inCipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                byte[] content = data.getBytes();
                byte[] single;
                int lenght = content.length;
                int offset = 0;
                int i = 0;
                // 非对称加密每次加密的内容长度有限制，所以此处进行分段加密
                while (lenght - offset > 0) {
                    if (lenght - offset > MAX_ENCRYPT_BLOCK) {
                        single = inCipher.doFinal(content, offset, MAX_ENCRYPT_BLOCK);
                    } else {
                        single = inCipher.doFinal(content, offset, lenght - offset);
                    }
                    outputStream.write(single, 0, single.length);
                    i++;
                    offset = i * MAX_ENCRYPT_BLOCK;
                }

//                CipherOutputStream cipherOutputStream = new CipherOutputStream(
//                        outputStream, inCipher);
//                cipherOutputStream.write(data.getBytes("UTF-8"));
//                cipherOutputStream.close();


                byte[] vals = outputStream.toByteArray();
                return Base64.encodeToString(vals, Base64.NO_WRAP);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }
        return "";
    }

    /**
     * 解密方法
     *
     * @param data  需要解密的字符串
     * @param alias KeyStore储存数据的别名
     * @return
     */
    private String decryptString(String data, String alias) {
        if (!TextUtils.isEmpty(alias) && !TextUtils.isEmpty(data)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                initKeyStore(alias);
            }
            String decryptStr = "";
            try {
                KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//                cipher.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());

                cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));

                byte[] content = Base64.decode(data, Base64.NO_WRAP);
                int length = content.length;
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int offset = 0;
                byte[] signal;
                int i = 0;
                // 非对称解密每次解密的内容长度有限制，所以此处进行分段解密
                while (length - offset > 0) {
                    if (length - offset > MAX_DECRYPT_BLOCK) {
                        signal = cipher.doFinal(content, offset, MAX_DECRYPT_BLOCK);
                    } else {
                        signal = cipher.doFinal(content, offset, length - offset);
                    }
                    outputStream.write(signal, 0, signal.length);
                    i++;
                    offset = i * MAX_DECRYPT_BLOCK;
                }
                byte[] decrypt = outputStream.toByteArray();
                outputStream.close();
                decryptStr = new String(decrypt);

//                CipherInputStream cipherInputStream = new CipherInputStream(
//                        new ByteArrayInputStream(Base64.decode(data, Base64.DEFAULT)), output);
//                ArrayList<Byte> values = new ArrayList<>();
//                int nextByte;
//                while ((nextByte = cipherInputStream.read()) != -1) {
//                    values.add((byte) nextByte);
//                }
//                byte[] bytes = new byte[values.size()];
//                for (int i = 0; i < bytes.length; i++) {
//                    bytes[i] = values.get(i).byteValue();
//                }
//                decryptStr = new String(bytes, 0, bytes.length, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return decryptStr;
        }
        return "";
    }

    /**
     * 初始化KeyStore
     *
     * @param alias KeyStore储存数据的别名
     */
    private void initKeyStore(String alias) {
        try {
            keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
            keyStore.load(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            createSecretKey(alias);
        }
    }

    /**
     * 创建秘钥并且储存在KeyStore秘钥库,只有这个应用程序将能够访问键
     *
     * @param alias KeyStore储存数据的别名
     */
    private void createSecretKey(String alias) {
        if (!"".equals(alias)) {
            try {
                if (keyStore != null && !keyStore.containsAlias(alias)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        KeyPairGenerator keyPairGenerator = KeyPairGenerator
                                .getInstance(KeyProperties.KEY_ALGORITHM_RSA, KEYSTORE_PROVIDER);
                        KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec
                                .Builder(alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                                .build();
                        keyPairGenerator.initialize(keyGenParameterSpec);
                        keyPairGenerator.generateKeyPair();
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        Calendar start = Calendar.getInstance();
                        Calendar end = Calendar.getInstance();
                        end.add(Calendar.YEAR, 100);
                        KeyPairGeneratorSpec keyPairGeneratorSpec = new KeyPairGeneratorSpec
                                .Builder(MyApplication.getInstance())
                                .setAlias(alias)
                                .setSubject(new X500Principal("CN=" + alias))
                                .setSerialNumber(BigInteger.TEN)
                                .setStartDate(start.getTime())
                                .build();
                        KeyPairGenerator keyPairGenerator = KeyPairGenerator
                                .getInstance(KeyProperties.KEY_ALGORITHM_RSA, KEYSTORE_PROVIDER);
                        keyPairGenerator.initialize(keyPairGeneratorSpec);
                        keyPairGenerator.generateKeyPair();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 将base64编码后的公钥字符串转成PublicKey实例
     *
     * @param publicKey 公钥字符
     * @return publicKEY
     * @throws Exception exception
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        byte[] keyBytes = Base64.decode(publicKey, Base64.NO_WRAP);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 将base64编码后的私钥字符串转成PrivateKey实例
     *
     * @param privateKey 私钥字符串
     * @return 私钥对象
     * @throws Exception exception
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey, Base64.NO_WRAP);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }


}

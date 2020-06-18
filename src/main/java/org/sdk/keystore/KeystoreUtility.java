package org.sdk.keystore;

import org.apache.commons.codec.binary.Hex;
import org.sdk.apiResult.APIResult;
import org.tdf.common.util.HexBytes;
import org.tdf.crypto.PrivateKey;
import org.tdf.crypto.PublicKey;
import org.tdf.crypto.keystore.KeyStoreImpl;
import org.tdf.crypto.keystore.SMKeystore;
import org.tdf.crypto.sm2.SM2PrivateKey;
import org.tdf.sunflower.state.Address;


public class KeystoreUtility {


    public static KeyStoreImpl generateKeyStore(String password) {
        return SMKeystore.generateKeyStore(password);
    }

    public static String decryptKeyStore(KeyStoreImpl keyStore, String password) {
        return HexBytes.fromBytes(SMKeystore.decryptKeyStore(keyStore, password)).toHex();
    }

    public String privateKeyToPublicKey(String privateKey) {
        PrivateKey sm2PrivateKey = new SM2PrivateKey(HexBytes.fromHex(privateKey).getBytes());
        PublicKey publicKey = sm2PrivateKey.generatePublicKey();
        return HexBytes.fromBytes(publicKey.getEncoded()).toHex();
    }

    public String publicKeyToAddress(String publicKey){
        return Address.fromPublicKey(HexBytes.fromHex(publicKey)).toHex();
    }

}

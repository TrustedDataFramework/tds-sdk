package org.sdk.transaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sdk.keystore.KeystoreUtility;
import org.sdk.transaction.contract.Accessory;
import org.sdk.transaction.contract.User;
import org.sdk.transaction.contract.WeldPayload;
import org.tdf.common.util.HexBytes;
import org.tdf.common.util.LittleEndian;
import org.tdf.rlp.RLPCodec;
import org.tdf.sunflower.state.Address;
import org.tdf.sunflower.types.CryptoContext;
import org.tdf.sunflower.types.Transaction;

public class TransactionUtility {

    public static final HexBytes ROOT_USER_ADDRESS = HexBytes.fromHex("9cbf30db111483e4b84e77ca0e39378fd7605e1b");
    public static final HexBytes WELD_STATE_ADDRESS = HexBytes.fromHex("0000000000000000000000000000000000000006");
    public static final HexBytes USER_STATE_ADDRESS = HexBytes.fromHex("0000000000000000000000000000000000000007");

    public static final HexBytes ZERO_BYTES = HexBytes.fromBytes(new byte[32]);
    public static final int BLOCK_VERSION = LittleEndian.decodeInt32(new byte[]{0, 'p', 'o', 'a'});
    public static final int TRANSACTION_VERSION = LittleEndian.decodeInt32(new byte[]{0, 'p', 'o', 'a'});

    private enum UserMethod {
        SAVE, UPDATE, DELETE, CHANGE_OWNER
    }

    private enum Method {
        SAVE, UPDATE
    }

    public static Transaction saveUser(long nonce, HexBytes privateKey, String address, String username, int role, String org, int orgType) {
        byte[] pk = CryptoContext.getPkFromSk(privateKey.getBytes());
        User user = new User(HexBytes.fromHex(address), username, role, org, orgType);
        HexBytes prefix = HexBytes.fromBytes(new byte[]{(byte) UserMethod.SAVE.ordinal()});

        Transaction transaction = new Transaction(
                TRANSACTION_VERSION,
                Transaction.Type.CONTRACT_CALL.code,
                System.currentTimeMillis() / 1000,
                ++nonce,
                HexBytes.fromBytes(pk),
                0L,
                0L,
                prefix.concat(HexBytes.fromBytes(RLPCodec.encode(user))),
                USER_STATE_ADDRESS,
                ZERO_BYTES);
        byte[] sig = CryptoContext.sign(privateKey.getBytes(), transaction.getSignaturePlain());
        transaction.setSignature(HexBytes.fromBytes(sig));
        return transaction;
    }

    public static Transaction updateUser(long nonce, HexBytes privateKey, String address, String username, int role, String org, int orgType) {
        byte[] pk = CryptoContext.getPkFromSk(privateKey.getBytes());
        User user = new User(HexBytes.fromHex(address), username, role, org, orgType);
        HexBytes prefix = HexBytes.fromBytes(new byte[]{(byte) UserMethod.UPDATE.ordinal()});

        Transaction transaction = new Transaction(
                TRANSACTION_VERSION,
                Transaction.Type.CONTRACT_CALL.code,
                System.currentTimeMillis() / 1000,
                ++nonce,
                HexBytes.fromBytes(pk),
                0L,
                0L,
                prefix.concat(HexBytes.fromBytes(RLPCodec.encode(user))),
                USER_STATE_ADDRESS,
                ZERO_BYTES);
        byte[] sig = CryptoContext.sign(privateKey.getBytes(), transaction.getSignaturePlain());
        transaction.setSignature(HexBytes.fromBytes(sig));
        return transaction;
    }

    public static Transaction deleteUser(long nonce, HexBytes privateKey, String address) {
        byte[] pk = CryptoContext.getPkFromSk(privateKey.getBytes());
        HexBytes prefix = HexBytes.fromBytes(new byte[]{(byte) UserMethod.DELETE.ordinal()});

        Transaction transaction = new Transaction(
                TRANSACTION_VERSION,
                Transaction.Type.CONTRACT_CALL.code,
                System.currentTimeMillis() / 1000,
                ++nonce,
                HexBytes.fromBytes(pk),
                0L,
                0L,
                prefix.concat(HexBytes.fromHex(address)),
                USER_STATE_ADDRESS,
                ZERO_BYTES);
        byte[] sig = CryptoContext.sign(privateKey.getBytes(), transaction.getSignaturePlain());
        transaction.setSignature(HexBytes.fromBytes(sig));
        return transaction;
    }

    public static Transaction changeOwnerUser(long nonce, HexBytes privateKey, String address) {
        byte[] pk = CryptoContext.getPkFromSk(privateKey.getBytes());
        HexBytes prefix = HexBytes.fromBytes(new byte[]{(byte) UserMethod.CHANGE_OWNER.ordinal()});

        Transaction transaction = new Transaction(
                TRANSACTION_VERSION,
                Transaction.Type.CONTRACT_CALL.code,
                System.currentTimeMillis() / 1000,
                ++nonce,
                HexBytes.fromBytes(pk),
                0L,
                0L,
                prefix.concat(HexBytes.fromHex(address)),
                USER_STATE_ADDRESS,
                ZERO_BYTES);
        byte[] sig = CryptoContext.sign(privateKey.getBytes(), transaction.getSignaturePlain());
        transaction.setSignature(HexBytes.fromBytes(sig));
        return transaction;
    }

    public static Transaction saveWeld(long nonce, HexBytes privateKey, String wpqr, HexBytes groupHash, int type, Accessory accessory) {
        byte[] pk = CryptoContext.getPkFromSk(privateKey.getBytes());
        HexBytes address = Address.fromPublicKey(pk);
        HexBytes prefix = HexBytes.fromBytes(new byte[]{(byte) Method.SAVE.ordinal()});
        WeldPayload weldPayload = new WeldPayload(
                wpqr,
                type,
                groupHash,
                accessory);

        Transaction transaction = new Transaction(
                TRANSACTION_VERSION,
                Transaction.Type.CONTRACT_CALL.code,
                System.currentTimeMillis() / 1000,
                ++nonce,
                HexBytes.fromBytes(pk),
                0L,
                0L,
                prefix.concat(HexBytes.fromBytes(RLPCodec.encode(weldPayload))),
                WELD_STATE_ADDRESS,
                ZERO_BYTES);
        byte[] sig = CryptoContext.sign(privateKey.getBytes(), transaction.getSignaturePlain());
        transaction.setSignature(HexBytes.fromBytes(sig));
        return transaction;
    }

    public static Transaction updateWeld(long nonce, HexBytes privateKey, String wpqr, HexBytes groupHash) {
        byte[] pk = CryptoContext.getPkFromSk(privateKey.getBytes());
        HexBytes address = Address.fromPublicKey(pk);
        HexBytes prefix = HexBytes.fromBytes(new byte[]{(byte) Method.UPDATE.ordinal()});
        WeldPayload weldPayload = new WeldPayload(
                wpqr,
                0,
                groupHash,
                null);

        Transaction transaction = new Transaction(
                TRANSACTION_VERSION,
                Transaction.Type.CONTRACT_CALL.code,
                System.currentTimeMillis() / 1000,
                ++nonce,
                HexBytes.fromBytes(pk),
                0L,
                0L,
                prefix.concat(HexBytes.fromBytes(RLPCodec.encode(weldPayload))),
                WELD_STATE_ADDRESS,
                ZERO_BYTES);
        byte[] sig = CryptoContext.sign(privateKey.getBytes(), transaction.getSignaturePlain());
        transaction.setSignature(HexBytes.fromBytes(sig));
        return transaction;
    }

    public static Transaction saveAccessory(long nonce, HexBytes privateKey, String wpqr, int type, String accessoryName, HexBytes accessoryHash) {
        byte[] pk = CryptoContext.getPkFromSk(privateKey.getBytes());
        HexBytes address = Address.fromPublicKey(pk);
        HexBytes prefix = HexBytes.fromBytes(new byte[]{(byte) Method.SAVE.ordinal()});
        Accessory accessory = new Accessory(
                type,
                accessoryName,
                accessoryHash);
        WeldPayload weldPayload = new WeldPayload(
                wpqr,
                1,
                ZERO_BYTES,
                accessory);

        Transaction transaction = new Transaction(
                TRANSACTION_VERSION,
                Transaction.Type.CONTRACT_CALL.code,
                System.currentTimeMillis() / 1000,
                ++nonce,
                HexBytes.fromBytes(pk),
                0L,
                0L,
                prefix.concat(HexBytes.fromBytes(RLPCodec.encode(weldPayload))),
                WELD_STATE_ADDRESS,
                ZERO_BYTES);
        byte[] sig = CryptoContext.sign(privateKey.getBytes(), transaction.getSignaturePlain());
        transaction.setSignature(HexBytes.fromBytes(sig));
        return transaction;
    }

    public static Transaction updateAccessory(long nonce, HexBytes privateKey, String wpqr, int type, String accessoryName, HexBytes accessoryHash) {
        byte[] pk = CryptoContext.getPkFromSk(privateKey.getBytes());
        HexBytes address = Address.fromPublicKey(pk);
        HexBytes prefix = HexBytes.fromBytes(new byte[]{(byte) Method.UPDATE.ordinal()});
        Accessory accessory = new Accessory(
                type,
                accessoryName,
                accessoryHash);
        WeldPayload weldPayload = new WeldPayload(
                wpqr,
                1,
                null,
                accessory);

        Transaction transaction = new Transaction(
                TRANSACTION_VERSION,
                Transaction.Type.CONTRACT_CALL.code,
                System.currentTimeMillis() / 1000,
                ++nonce,
                HexBytes.fromBytes(pk),
                0L,
                0L,
                prefix.concat(HexBytes.fromBytes(RLPCodec.encode(weldPayload))),
                WELD_STATE_ADDRESS,
                ZERO_BYTES);
        byte[] sig = CryptoContext.sign(privateKey.getBytes(), transaction.getSignaturePlain());
        transaction.setSignature(HexBytes.fromBytes(sig));
        return transaction;
    }
}

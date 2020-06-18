package org.sdk.transaction;

import org.sdk.transaction.contract.User;
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

    public static Transaction saveUser(long nonce, HexBytes privateKey, String username, int role, String org, int orgType) {
        byte[] pk = CryptoContext.getPkFromSk(privateKey.getBytes());
        HexBytes address = Address.fromPublicKey(pk);
        User user = new User(address, username, role, org, orgType);
        HexBytes prefix = HexBytes.fromBytes(new byte[]{(byte) UserMethod.SAVE.ordinal()});

        Transaction transaction = new Transaction(
                Transaction.Type.CONTRACT_CALL.code,
                TRANSACTION_VERSION, System.currentTimeMillis() / 1000
                ,
                nonce,
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
}

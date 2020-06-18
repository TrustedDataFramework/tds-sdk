package org.sdk.transaction;

import org.sdk.apiResult.APIResult;
import org.sdk.transaction.contract.User;
import org.sdk.util.ByteUtil;
import org.tdf.common.util.HexBytes;
import org.tdf.common.util.LittleEndian;
import org.tdf.crypto.PrivateKey;
import org.tdf.crypto.sm2.SM2PrivateKey;
import org.tdf.rlp.RLP;
import org.tdf.rlp.RLPCodec;
import org.tdf.rlp.RLPDecoder;
import org.tdf.sunflower.types.Transaction;

import java.util.Date;

public class TransactionUtility {

    public static final HexBytes ROOT_USER_ADDRESS = HexBytes.fromHex("9cbf30db111483e4b84e77ca0e39378fd7605e1b");
    public static final HexBytes WELD_STATE_ADDRESS = HexBytes.fromHex("0000000000000000000000000000000000000006");
    public static final HexBytes USER_STATE_ADDRESS = HexBytes.fromHex("0000000000000000000000000000000000000007");

    public static final HexBytes ZERO_BYTES = HexBytes.fromBytes(new byte[32]);
    public static final int BLOCK_VERSION = LittleEndian.decodeInt32(new byte[]{0, 'p', 'o', 'a'});
    public static final int TRANSACTION_VERSION = LittleEndian.decodeInt32(new byte[]{0, 'p', 'o', 'a'});

    public static Object CreateUserTransaction(Long nonce, String from, String address, String username, int role, String org, int orgType,String privatekey) {
        User user = new User(HexBytes.fromHex(address),username,role,org,orgType);
        Transaction transaction = new Transaction(Transaction.Type.CONTRACT_DEPLOY.code, TRANSACTION_VERSION,new Date().getTime(), nonce, HexBytes.fromHex(from) , 0L, 0L, HexBytes.fromBytes(ByteUtil.merge(new byte[]{0x00}, user.RLPdeserialization())), ROOT_USER_ADDRESS, ZERO_BYTES);
        PrivateKey sm2PrivateKey = new SM2PrivateKey(HexBytes.fromHex(privatekey).getBytes());
        transaction.setSignature(HexBytes.fromBytes(sm2PrivateKey.sign(transaction.getSignaturePlain())));
        return APIResult.newSuccess(HexBytes.fromBytes(RLPCodec.encode(transaction)).toHex(),transaction.getHash().toHex());
    }



}

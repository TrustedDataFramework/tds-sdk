import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.sdk.transaction.TransactionUtility;
import org.sdk.util.HttpclientUtil;
import org.tdf.common.util.HexBytes;
import org.tdf.sunflower.types.CryptoContext;
import org.tdf.sunflower.types.Transaction;

@RunWith(JUnit4.class)
public class SDKTests {
    private final ObjectMapper MAPPER = new ObjectMapper();
    static {
        try{
            Class.forName("org.sdk.util.Init");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int getNonce(String pubkeyOraddress){
        String result = HttpclientUtil.sendGet("http://localhost:7010/rpc/account/"+pubkeyOraddress,"");
        JSONObject json_result = JSONObject.parseObject(result);
        JSONObject json_data = (JSONObject) json_result.get("data");
        return (int) json_data.get("nonce");
    }

    @Test
    public void test0() throws Exception{
        String txJson = "{\"version\":1634693120,\"type\":3,\"createdAt\":\"2020-06-23T14:38:00+08:00\",\"nonce\":1,\"from\":\"0371d8cc7c2bb71001c2d711e9c24350ed1e75cbade981635d0c4fa4196f4c35e6\",\"gasPrice\":0,\"amount\":0,\"payload\":\"00ee94494bb64ffdf58a92c2d544fbdc1716e79d64a6708d757365726e616d655f7465737401886f72675f7465737402\",\"to\":\"0000000000000000000000000000000000000007\",\"signature\":\"2869d1da5686b33ba8bb024be479bffa0d82ded9d19947613b8c13c5f1031849e0eeac53b1fa57ca8123e47eadd5797d8441a34aa31043b7108ff3da8f18423d\",\"hash\":\"d56c1d3f41273c079acebfa86a695c7a4412bc7d9206ec9ab9b9e135961adca1\",\"size\":205} ";
        Transaction tx = MAPPER.readValue(txJson, Transaction.class);
        HexBytes sk = HexBytes.fromHex("650b2593dbf3cf7b8a92f5d7145ea3176db591c59967279d8e319ca896f946b9");
        tx.setNonce(1);
        tx.setFrom(HexBytes.fromBytes(CryptoContext.getPkFromSk(sk.getBytes())));
        tx.setSignature(HexBytes.fromBytes(CryptoContext.sign(sk.getBytes(), tx.getSignaturePlain())));
        System.out.println(MAPPER.writeValueAsString(tx));
    }

    @Test
    public void saveUser() throws ClassNotFoundException {
        Class.forName("org.sdk.util.Init");
        Transaction transaction = TransactionUtility.saveUser(getNonce("abb7bc8e0d9a651d4736e1a0ae0d02929b8d7eb0"), HexBytes.fromHex("f00df601a78147ffe0b84de1dffbebed2a6ea965becd5d0bd7faf54f1f29c6b5"), "abb7bc8e0d9a651d4736e1a0ae0d02929b8d7eb0","username_3", 2, "org_test_3", 3);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode appleNode = objectMapper.convertValue(transaction, JsonNode.class);
        HttpclientUtil.httpPostBody("http://192.168.1.167:7010/rpc/transaction",appleNode);
    }


}

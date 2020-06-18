package org.sdk.transaction.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.tdf.common.util.HexBytes;
import org.tdf.rlp.RLP;
import org.tdf.rlp.RLPCodec;
import org.tdf.rlp.RLPElement;
import org.tdf.sunflower.types.ValidateResult;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public static ValidateResult verify(@NonNull User user){
        if(user.address == null || user.address.isEmpty())
            return ValidateResult.fault("address is null or empty");
        if(user.username == null || user.username.isEmpty())
            return ValidateResult.fault("username is null or empty");
        if(user.role < 0 || user.role > 2)
            return ValidateResult.fault("invalid role " + user.role);
        return ValidateResult.success();
    }

    public enum Method {
        SAVE, UPDATE, DELETE, CHANGE_OWNER
    }

    /**
     * 用户 keystore 地址
     */
    @RLP(0)
    private HexBytes address;

    /**
     * 伟泰系统登录用户名
     */
    @RLP(1)
    private String username;

    /**
     * 用户角色
     * 0 root 超级管理员
     * 1 admin 管理员
     * 2 user 普通用户
     */
    @RLP(2)
    private int role;

    /**
     * 组织名称
     */
    @RLP(3)
    private String org;

    /**
     * 组织类型
     * 0 factory
     * 1 training institution
     * 2 testing organization
     * 3 other
     */
    @RLP(4)
    private int orgType;

    public User RLPdeserialization(byte[] payload) {
        try{
            User User= RLPCodec.decode(payload,User.class);
            this.address=address;
            this.org=org;
            this.orgType=orgType;
            this.username=username;
            this.role=role;
            return User;
        }catch (Exception e){
            throw e;
        }
    }
    public byte[] RLPdeserialization() {
        return RLPElement.readRLPTree(this).getEncoded();
    }


}

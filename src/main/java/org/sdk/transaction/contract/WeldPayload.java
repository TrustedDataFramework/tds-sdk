package org.sdk.transaction.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.tdf.common.util.HexBytes;
import org.tdf.rlp.RLP;
import org.tdf.sunflower.types.ValidateResult;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WeldPayload {
    public static ValidateResult verify(@NonNull WeldPayload p){
        if (p.wpqr == null || p.wpqr.isEmpty()) {
            return ValidateResult.fault("wpqr is empty");
        }
        if (p.type < 0 || p.type > 1) {
            return ValidateResult.fault("invalid type " + p.type);
        }
        if (p.type == 0) {
            if (p.groupHash.isEmpty() || p.accessory != null) {
                return ValidateResult.fault("group hash is null or accessory is null");
            }
            int type = p.groupHash.get(0);
            if (type < 0 || type > 6) {
                return ValidateResult.fault("invalid group hash type " + type);
            }
            HexBytes hash = p.groupHash.slice(1);
            if (hash.isEmpty()) {
                return ValidateResult.fault("empty group hash");
            }
        }
        if (p.type == 1) {
            if (p.accessory == null || !p.groupHash.isEmpty()) {
                return ValidateResult.fault("accessory is null or group hash is not null");
            }
        }

        return ValidateResult.success();
    }

    @RLP(0)
    private String wpqr;
    /**
     * 0 wdld
     * 1 accessory
     */
    @RLP(1)
    private int type;

    /**
     * The first byte is a type, ranging from 0 to 6
     * Followed by the hash
     */
    @RLP(2)
    private HexBytes groupHash;

    @RLP(3)
    private Accessory accessory;
}

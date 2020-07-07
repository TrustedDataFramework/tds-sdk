package org.sdk.transaction.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.tdf.common.util.HexBytes;
import org.tdf.rlp.RLP;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Accessory {
    public static boolean verify(@NonNull Accessory a) {
        return (a.getType() == 1 || a.getType() == 3 || a.getType() == 4 || a.getType() == 5) &&
                (a.accessoryName != null && !a.accessoryName.isEmpty()) &&
                (a.accessoryHash != null && !a.accessoryHash.isEmpty());
    }

    /**
     * Type can only be 1、3、4、5
     */
    @RLP(0)
    private int type;
    @RLP(1)
    private String accessoryName;
    @RLP(2)
    private HexBytes accessoryHash;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Accessory accessory = (Accessory) o;
        return type == accessory.type &&
                Objects.equals(accessoryName, accessory.accessoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, accessoryName);
    }
}

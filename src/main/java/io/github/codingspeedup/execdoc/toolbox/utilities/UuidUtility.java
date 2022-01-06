package io.github.codingspeedup.execdoc.toolbox.utilities;

import com.devskiller.friendly_id.FriendlyId;

import java.util.UUID;

public class UuidUtility {

    public static String nextUuid() {
        return FriendlyId.toFriendlyId(UUID.randomUUID());
    }

}

package org.seariver.kanbanboard.write.domain.application;

import org.seariver.kanbanboard.common.SelfValidating;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.UUID;

public class MoveBucketCommand extends SelfValidating<MoveBucketCommand> implements Command {

    @NotNull
    @Pattern(regexp = UUID_FORMAT, message = INVALID_UUID)
    private final String uuid;
    @Positive
    private final double position;

    public MoveBucketCommand(String uuid, double position) {
        this.uuid = uuid;
        this.position = position;
        validateSelf();
    }

    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    public double getPosition() {
        return position;
    }
}

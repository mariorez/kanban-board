package org.seariver.kanbanboard.write.domain.application;

import org.seariver.kanbanboard.common.SelfValidating;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.UUID;

public class MoveBucketCommand extends SelfValidating<MoveBucketCommand> implements Command {

    @NotNull
    @Pattern(regexp = UUID_FORMAT, message = INVALID_UUID)
    private final String exteranlId;
    @Positive
    private final double position;

    public MoveBucketCommand(String exteranlId, double position) {
        this.exteranlId = exteranlId;
        this.position = position;
        validateSelf();
    }

    public UUID getExteranlId() {
        return UUID.fromString(exteranlId);
    }

    public double getPosition() {
        return position;
    }
}

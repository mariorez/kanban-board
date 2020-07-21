package org.seariver.kanbanboard.write.domain.application;

import org.seariver.kanbanboard.common.SelfValidating;

import javax.validation.constraints.*;
import java.util.UUID;

public class CreateBucketCommand extends SelfValidating<CreateBucketCommand> implements Command {

    @NotNull
    @Pattern(regexp = UUID_FORMAT, message = INVALID_UUID)
    private final String externalId;
    @Positive
    private final double position;
    @NotBlank
    @Size(min = 1, max = 100)
    private final String name;

    public CreateBucketCommand(String externalId, double position, String name) {
        this.externalId = externalId;
        this.position = position;
        this.name = name;
        validateSelf();
    }

    public UUID getExternalId() {
        return UUID.fromString(externalId);
    }

    public double getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }
}

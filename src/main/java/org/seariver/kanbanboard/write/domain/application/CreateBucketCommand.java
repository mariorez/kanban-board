package org.seariver.kanbanboard.write.domain.application;

import org.seariver.kanbanboard.common.SelfValidating;

import javax.validation.constraints.*;
import java.util.UUID;

public class CreateBucketCommand extends SelfValidating<CreateBucketCommand> implements Command {

    @NotNull
    @Pattern(regexp = UUID_FORMAT, message = INVALID_UUID)
    private final String uuid;
    @Positive
    private final double position;
    @NotBlank
    @Size(min = 1, max = 100)
    private final String name;

    public CreateBucketCommand(String uuid, double position, String name) {
        this.uuid = uuid;
        this.position = position;
        this.name = name;
        validateSelf();
    }

    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    public double getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }
}

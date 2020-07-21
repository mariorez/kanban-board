package org.seariver.kanbanboard.write.domain.application;

import org.seariver.kanbanboard.common.SelfValidating;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.UUID;

public class CreateCardCommand extends SelfValidating<CreateCardCommand> implements Command {

    @NotNull
    @Pattern(regexp = UUID_FORMAT, message = INVALID_UUID)
    private final String bucketExternalId;
    @NotNull
    @Pattern(regexp = UUID_FORMAT, message = INVALID_UUID)
    private final String externalId;
    @Positive
    private final double position;
    @NotBlank
    @Size(min = 1, max = 100)
    private final String name;

    public CreateCardCommand(String bucketExternalId, String externalId, double position, String name) {
        this.bucketExternalId = bucketExternalId;
        this.externalId = externalId;
        this.position = position;
        this.name = name;
        validateSelf();
    }

    public UUID getBucketExternalId() {
        return UUID.fromString(bucketExternalId);
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

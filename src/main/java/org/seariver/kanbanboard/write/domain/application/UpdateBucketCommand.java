package org.seariver.kanbanboard.write.domain.application;

import org.seariver.kanbanboard.common.SelfValidating;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

public class UpdateBucketCommand extends SelfValidating<UpdateBucketCommand> implements Command {

    @NotNull
    @Pattern(regexp = UUID_FORMAT, message = INVALID_UUID)
    private final String externalId;
    @NotBlank
    @Size(min = 1, max = 100)
    private final String name;

    public UpdateBucketCommand(String externalId, String name) {
        this.externalId = externalId;
        this.name = name;
        validateSelf();
    }

    public UUID getExternalId() {
        return UUID.fromString(externalId);
    }

    public String getName() {
        return name;
    }
}

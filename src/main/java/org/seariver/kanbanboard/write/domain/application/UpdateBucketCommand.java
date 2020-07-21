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
    private final String uuid;
    @NotBlank
    @Size(min = 1, max = 100)
    private final String name;

    public UpdateBucketCommand(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        validateSelf();
    }

    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    public String getName() {
        return name;
    }
}

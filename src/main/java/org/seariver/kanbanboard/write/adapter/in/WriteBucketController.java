package org.seariver.kanbanboard.write.adapter.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.seariver.kanbanboard.write.CommandBus;
import org.seariver.kanbanboard.write.domain.application.CreateBucketCommand;
import org.seariver.kanbanboard.write.domain.application.UpdateBucketCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@Validated
public class WriteBucketController {

    private static final String BUCKETS_PATH = "v1/buckets";

    @Autowired
    private CommandBus commandBus;

    @PostMapping(path = BUCKETS_PATH)
    @Validated(OnCreate.class)
    public ResponseEntity<String> create(@Valid @RequestBody BucketInput dto) throws URISyntaxException {

        commandBus.execute(new CreateBucketCommand(dto.uuid, dto.position, dto.name));

        return ResponseEntity
            .created(new URI(String.format("/%s/%s", BUCKETS_PATH, dto.uuid)))
            .build();
    }

    @PutMapping(path = BUCKETS_PATH + "/{id}")
    @Validated(OnUpdate.class)
    public ResponseEntity<String> update(@PathVariable(name = "id") UUID uuid,
                                         @Valid @RequestBody BucketInput dto) {

        commandBus.execute(new UpdateBucketCommand(uuid, dto.position(), dto.name()));

        return ResponseEntity.noContent().build();
    }

    private record BucketInput(
        @NotNull(groups = OnCreate.class)
        @Null(groups = OnUpdate.class)
        @JsonProperty("id")
        UUID uuid,
        @Positive
        @JsonProperty("position")
        double position,
        @NotBlank
        @Size(min = 1, max = 100)
        @JsonProperty("name")
        String name) {
    }

    private interface OnCreate {
    }

    private interface OnUpdate {
    }
}

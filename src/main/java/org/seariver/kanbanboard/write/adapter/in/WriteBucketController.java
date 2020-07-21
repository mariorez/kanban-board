package org.seariver.kanbanboard.write.adapter.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.seariver.kanbanboard.write.CommandBus;
import org.seariver.kanbanboard.write.domain.application.CreateBucketCommand;
import org.seariver.kanbanboard.write.domain.application.UpdateBucketCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class WriteBucketController {

    private static final String BUCKETS_PATH = "v1/buckets";
    private CommandBus commandBus;

    public WriteBucketController(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @PostMapping(path = BUCKETS_PATH)
    public ResponseEntity<String> create(@RequestBody BucketInput dto) throws URISyntaxException {

        commandBus.execute(new CreateBucketCommand(dto.uuid, dto.position, dto.name));

        return ResponseEntity
                .created(new URI(String.format("/%s/%s", BUCKETS_PATH, dto.uuid)))
                .build();
    }

    @PutMapping(path = BUCKETS_PATH + "/{id}")
    public ResponseEntity<String> update(@Valid @NotNull @PathVariable(name = "id") String uuid,
                                         @RequestBody BucketInput dto) {

        commandBus.execute(new UpdateBucketCommand(uuid, dto.name()));

        return ResponseEntity.noContent().build();
    }

    record BucketInput(
            @JsonProperty("id")
            String uuid,
            double position,
            String name) {
        // silence sonarqube
    }
}

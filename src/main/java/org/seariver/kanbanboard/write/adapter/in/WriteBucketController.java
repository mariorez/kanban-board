package org.seariver.kanbanboard.write.adapter.in;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping
public class WriteBucketController {

    public static final String BUCKETS_PATH = "v1/buckets";

    @Autowired
    private CommandBus commandBus;

    @PostMapping(path = BUCKETS_PATH)
    public ResponseEntity create(@Validated @RequestBody CreateBucketCommand command) throws URISyntaxException {

        commandBus.execute(command);

        return ResponseEntity
            .created(new URI(String.format("/%s/%s", BUCKETS_PATH, command.id())))
            .build();
    }

    @PutMapping(path = BUCKETS_PATH + "/{uuid}")
    public ResponseEntity update(
        @PathVariable(name = "uuid") UUID uuid,
        @RequestBody Map<String, Object> payload) throws URISyntaxException {

        var command = new UpdateBucketCommand(uuid, (double) payload.get("position"), (String) payload.get("name"));

        commandBus.execute(command);

        return ResponseEntity.noContent().build();
    }
}

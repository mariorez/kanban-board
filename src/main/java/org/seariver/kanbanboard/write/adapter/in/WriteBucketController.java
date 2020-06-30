package org.seariver.kanbanboard.write.adapter.in;

import org.seariver.kanbanboard.write.CommandBus;
import org.seariver.kanbanboard.write.domain.application.CreateBucketCommand;
import org.seariver.kanbanboard.write.domain.application.UpdateBucketCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
public class WriteBucketController {

    private static final String BUCKETS_PATH = "v1/buckets";

    @Autowired
    private CommandBus commandBus;

    @PostMapping(path = BUCKETS_PATH)
    public ResponseEntity<String> create(@Valid @RequestBody CreateBucketDto dto) throws URISyntaxException {

        commandBus.execute(new CreateBucketCommand(dto.uuid(), dto.position(), dto.name()));

        return ResponseEntity
            .created(new URI(String.format("/%s/%s", BUCKETS_PATH, dto.uuid())))
            .build();
    }

    @PutMapping(path = BUCKETS_PATH + "/{id}")
    public ResponseEntity<String> update(@PathVariable(name = "id") UUID uuid,
                                         @Valid @RequestBody UpdateBucketDto dto) {

        commandBus.execute(new UpdateBucketCommand(uuid, dto.position(), dto.name()));

        return ResponseEntity.noContent().build();
    }
}

package org.seariver.kanbanboard.write.adapter.in;

import org.seariver.kanbanboard.write.CommandBus;
import org.seariver.kanbanboard.write.domain.application.CreateBucketCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping
public class BucketController {

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
}

package org.seariver.kanbanboard.write.adapter.in;

import org.seariver.kanbanboard.write.domain.application.CreateBucketCommand;
import org.seariver.kanbanboard.write.domain.application.CreateBucketCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("v1/buckets")
public class BucketController {

    @Autowired
    private CreateBucketCommandHandler handler;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody CreateBucketCommand command) throws URISyntaxException {

        handler.handle(command);

        return ResponseEntity.created(new URI("")).build();
    }
}

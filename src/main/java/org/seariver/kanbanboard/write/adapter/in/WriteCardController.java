package org.seariver.kanbanboard.write.adapter.in;

import org.seariver.kanbanboard.write.CommandBus;
import org.seariver.kanbanboard.write.domain.application.CreateCardCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class WriteCardController {

    private static final String CARDS_PATH = "v1/cards";

    @Autowired
    private CommandBus commandBus;

    @PostMapping(path = CARDS_PATH)
    public ResponseEntity<String> create(@Validated @RequestBody CardDto dto) throws URISyntaxException {

        commandBus.execute(new CreateCardCommand(dto.uuid(), dto.bucketUuid(), dto.position(), dto.name()));

        return ResponseEntity
            .created(new URI(String.format("/%s/%s", CARDS_PATH, dto.uuid())))
            .build();
    }
}

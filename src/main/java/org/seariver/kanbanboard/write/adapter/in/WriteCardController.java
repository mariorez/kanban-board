package org.seariver.kanbanboard.write.adapter.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.seariver.kanbanboard.write.CommandBus;
import org.seariver.kanbanboard.write.domain.application.CreateCardCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
public class WriteCardController {

    private static final String CARDS_PATH = "v1/cards";

    @Autowired
    private CommandBus commandBus;

    @PostMapping(path = CARDS_PATH)
    public ResponseEntity<String> create(@Validated @RequestBody CardDto dto) throws URISyntaxException {

        commandBus.execute(new CreateCardCommand(dto.externalId(), dto.bucketExternalId(), dto.position(), dto.name()));

        return ResponseEntity
            .created(new URI(String.format("/%s/%s", CARDS_PATH, dto.externalId())))
            .build();
    }

    record CardDto(
        @NotNull
        @JsonProperty("id")
        UUID externalId,
        @NotNull
        @JsonProperty("bucketId")
        UUID bucketExternalId,
        @Positive
        @JsonProperty("position")
        double position,
        @NotBlank
        @Size(min = 1, max = 100)
        @JsonProperty("name")
        String name) {
        // silence sonarqube
    }
}

package org.seariver.kanbanboard.write.adapter.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.seariver.kanbanboard.common.ServiceBus;
import org.seariver.kanbanboard.write.domain.application.CreateBucketCommand;
import org.seariver.kanbanboard.write.domain.application.MoveBucketCommand;
import org.seariver.kanbanboard.write.domain.application.UpdateBucketCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URISyntaxException;

@RestController
@RequestMapping("v1/buckets")
public class WriteBucketController {

    private ServiceBus serviceBus;

    public WriteBucketController(ServiceBus serviceBus) {
        this.serviceBus = serviceBus;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody BucketInput dto) throws URISyntaxException {

        serviceBus.execute(new CreateBucketCommand(dto.externalId(), dto.position(), dto.name()));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<String> update(@Valid @NotNull @PathVariable(name = "id") String externalId,
                                         @RequestBody BucketInput dto) {

        serviceBus.execute(new UpdateBucketCommand(externalId, dto.name()));

        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}/move")
    public ResponseEntity<String> move(@Valid @NotNull @PathVariable(name = "id") String externalId,
                                       @RequestBody BucketInput dto) {

        serviceBus.execute(new MoveBucketCommand(externalId, dto.position()));

        return ResponseEntity.noContent().build();
    }

    record BucketInput(
            @JsonProperty("id")
            String externalId,
            double position,
            String name) {
        // silence sonarqube
    }
}

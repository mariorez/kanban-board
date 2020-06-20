package org.seariver.kanbanboard.write.domain.application;

import org.seariver.kanbanboard.write.domain.core.WriteBucketRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateBucketCommandHandler {

    private WriteBucketRepository repository;

    public UpdateBucketCommandHandler(WriteBucketRepository repository) {
        this.repository = repository;
    }

    public void handle(UpdateBucketCommand command) {

        var bucket = repository.findByUuid(command.id()).get();

        bucket
            .setPosition(command.position())
            .setName(command.name());

        repository.update(bucket);
    }
}

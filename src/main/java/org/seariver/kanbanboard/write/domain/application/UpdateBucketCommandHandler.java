package org.seariver.kanbanboard.write.domain.application;

import org.seariver.kanbanboard.write.domain.core.Bucket;
import org.seariver.kanbanboard.write.domain.core.WriteBucketRepository;
import org.seariver.kanbanboard.write.domain.exception.BucketNotExistentException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.seariver.kanbanboard.write.domain.exception.DomainException.Error.*;

@Service
public class UpdateBucketCommandHandler {

    private WriteBucketRepository repository;

    public UpdateBucketCommandHandler(WriteBucketRepository repository) {
        this.repository = repository;
    }

    public void handle(UpdateBucketCommand command) {

        Optional<Bucket> bucketOptional = repository.findByUuid(command.id());

        if (!bucketOptional.isPresent()) {
            throw new BucketNotExistentException(BUCKET_NOT_EXIST);
        }

        var bucket = bucketOptional.get();

        bucket
            .setPosition(command.position())
            .setName(command.name());

        repository.update(bucket);
    }
}


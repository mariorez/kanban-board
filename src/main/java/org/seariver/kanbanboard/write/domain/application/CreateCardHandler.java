package org.seariver.kanbanboard.write.domain.application;

import org.seariver.kanbanboard.write.domain.core.Bucket;
import org.seariver.kanbanboard.write.domain.core.Card;
import org.seariver.kanbanboard.write.domain.core.WriteBucketRepository;
import org.seariver.kanbanboard.write.domain.core.WriteCardRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateCardHandler implements Handler<CreateCardCommand> {

    private WriteBucketRepository bucketRepository;
    private WriteCardRepository cardRepository;

    public CreateCardHandler(WriteBucketRepository bucketRepository, WriteCardRepository cardRepository) {
        this.bucketRepository = bucketRepository;
        this.cardRepository = cardRepository;
    }

    public void handle(CreateCardCommand command) {

        Optional<Bucket> bucketOptional = bucketRepository.findByExteranlId(command.bucketId());
        var bucket = bucketOptional.get();

        var card = new Card()
            .setBucketId(bucket.getId())
            .setExternalId(command.externalId())
            .setPosition(command.position())
            .setName(command.name());

        cardRepository.create(card);
    }
}

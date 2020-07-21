package org.seariver.kanbanboard.read.domain.application;

import org.seariver.kanbanboard.read.domain.core.BucketDto;
import org.seariver.kanbanboard.read.domain.core.ReadBucketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListAllBucketsResolver implements Resolver<ListAllBucketsQuery> {

    private ReadBucketRepository repository;

    public ListAllBucketsResolver(ReadBucketRepository repository) {
        this.repository = repository;
    }

    public void resolve(ListAllBucketsQuery query) {

        List<BucketDto> result = repository.findAll();
        query.setResult(result);
    }
}

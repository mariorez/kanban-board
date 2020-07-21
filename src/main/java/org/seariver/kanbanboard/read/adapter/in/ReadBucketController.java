package org.seariver.kanbanboard.read.adapter.in;

import org.seariver.kanbanboard.read.QueryBus;
import org.seariver.kanbanboard.read.domain.application.ListAllBucketsQuery;
import org.seariver.kanbanboard.read.domain.core.BucketDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/buckets")
public class ReadBucketController {

    private QueryBus queryBus;

    public ReadBucketController(QueryBus queryBus) {
        this.queryBus = queryBus;
    }

    @GetMapping
    public ResponseEntity<List<BucketDto>> listAll() {

        var query = new ListAllBucketsQuery();
        queryBus.execute(query);

        return ResponseEntity.ok(query.getResult());
    }
}

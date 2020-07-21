package org.seariver.kanbanboard.read.domain.application;

import org.seariver.kanbanboard.read.domain.core.BucketDto;

import java.util.List;

public class ListAllBucketsQuery implements Query {

    private List<BucketDto> result;

    public List<BucketDto> getResult() {
        return result;
    }

    public void setResult(List<BucketDto> result) {
        this.result = result;
    }
}

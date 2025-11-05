package ch.css.learning.jobrunr;

import org.jobrunr.jobs.lambdas.JobRequest;
import org.jobrunr.jobs.lambdas.JobRequestHandler;

import java.util.UUID;

public class MyJobRequest implements JobRequest {

    private UUID id;

    public MyJobRequest(UUID id) {
        this.id = id;
    }

    @Override
    public Class<MyJobRequestHandler> getJobRequestHandler() {
        return  MyJobRequestHandler.class;
    }

    public UUID getId() {
        return id;
    }

}
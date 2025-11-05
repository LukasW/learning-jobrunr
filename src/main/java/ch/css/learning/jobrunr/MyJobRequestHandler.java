package ch.css.learning.jobrunr;

import jakarta.enterprise.context.ApplicationScoped;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.lambdas.JobRequestHandler;

@ApplicationScoped
public class MyJobRequestHandler implements JobRequestHandler<MyJobRequest> {


    @Override
    @Job(name = "Some neat Job Display Name", retries = 2)
    public void run(MyJobRequest jobRequest) {
        // do your background work here
    }
}
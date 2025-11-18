package ch.css.learning.jobrunr.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jobrunr.jobs.Job;
import org.jobrunr.storage.JobSearchRequestBuilder;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.navigation.AmountRequest;

import java.util.List;

@ApplicationScoped
@Path("/jobs")
@Produces(MediaType.TEXT_PLAIN)
public class ControllerResource {
    @Inject
    StorageProvider storageProvider;

    @GET
    public String listJobs() {
        JobSearchRequestBuilder builder = JobSearchRequestBuilder
                .aJobSearchRequest()
                .withJobName("Send E-Mails to all subscribers");


        List<Job> jobs = storageProvider.getJobList(builder.build(), new AmountRequest("", 4));
        jobs.stream().map(j -> j.getJobDetails().getJobParameters().toString()).forEach(System.out::println);
        jobs.forEach(j -> j.delete("just because"));
        return "Stopped";
    }

}

package ch.css.learning.jobrunr;

import org.jobrunr.scheduling.JobScheduler;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.time.Instant;

@Path("/jobs")
@Produces(MediaType.TEXT_PLAIN)
public class JobResource {

    @Inject
    JobScheduler jobScheduler; // JobRunr's Scheduler

    @Inject
    MyJobService myJobService; // Unser Service mit den @Job-Methoden

    @GET
    @Path("/trigger-simple")
    public String triggerSimpleJob(@QueryParam("name") String name) {
        if (name == null || name.isBlank()) {
            name = "Standard-Input";
        }
        var jobName = name;

        // Job sofort einreihen (via Lambda)
        // JobRunr serialisiert diesen Aufruf und führt ihn im Hintergrund aus.
        jobScheduler.enqueue(() -> myJobService.doSimpleJob(jobName));

        return "Einfacher Job für '" + jobName + "' eingereiht!";
    }

    @GET
    @Path("/trigger-long")
    public String triggerLongJob() {
        // JobRunr findet die Methode "doLongJob" im myJobService
        jobScheduler.enqueue(myJobService::doLongJob);
        return "Langer Job eingereiht!";
    }

    @GET
    @Path("/trigger-delayed")
    public String triggerDelayedJob() {
        // Job 30 Sekunden in der Zukunft planen
        jobScheduler.schedule(Instant.now().plusSeconds(30),
                () -> myJobService.doSimpleJob("Verspäteter Job"));
        return "Verspäteter Job in 30 Sekunden geplant!";
    }
}

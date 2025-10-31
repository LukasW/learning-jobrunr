package ch.css.learning.jobrunr;

import jakarta.enterprise.context.ApplicationScoped;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.jobs.context.JobDashboardProgressBar;

import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class MyJobService {

    /**
     * Ein einfacher Job.
     * @param input Ein String-Parameter
     */
    @Job(name = "Mein einfacher Job", retries = 2)
    public void doSimpleJob(String input) {
        System.out.println("JOB GESTARTET: Verarbeite '" + input + "'");
        try {
            // Simuliere Arbeit
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("JOB BEENDET: '" + input + "'");
    }

    /**
     * Ein Job mit JobContext, um Details im Dashboard anzuzeigen.
     */
    @Job(name = "Langer Job mit Status")
    public void doLongJob(JobContext jobContext) {
        JobDashboardProgressBar jobDashboardProgressBar = jobContext.progressBar(100);
        System.out.println("Langer Job gestartet...");

        try {
            TimeUnit.SECONDS.sleep(10);
            jobDashboardProgressBar.setProgress(50);
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        jobContext.progressBar( 100);
        System.out.println("Langer Job beendet.");
    }
}
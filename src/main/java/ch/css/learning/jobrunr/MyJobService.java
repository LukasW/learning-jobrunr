package ch.css.learning.jobrunr;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
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
    public void doSimpleJob(SimpleJobParameters input) {
        System.out.println("JOB GESTARTET: Verarbeite '" + input.getName() + " -" + input.getDescription() +  "-'");
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
    @Transactional
    public void doLongJob(JobContext jobContext) {
        JobDashboardProgressBar jobDashboardProgressBar = jobContext.progressBar(100);
        System.out.println("Langer Job gestartet...");


        try {
            for (int i = 1; i < 100; i++) {
                TimeUnit.MILLISECONDS.sleep(100);
                jobDashboardProgressBar.incrementSucceeded();
                jobContext.logger().info("Fortschritt: " + i + "%" );
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        jobContext.progressBar( 100);
        System.out.println("Langer Job beendet.");
    }
}
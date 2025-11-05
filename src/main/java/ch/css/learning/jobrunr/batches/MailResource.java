package ch.css.learning.jobrunr.batches;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/mail")
@Produces(MediaType.TEXT_PLAIN)
public class MailResource {

    @Inject
    NewsletterService newsletterService;

    @GET
    @Path("/trigger")
    public String triggerLongJob(@DefaultValue("general-template") @QueryParam("template") String mailTemplateKey) {
        newsletterService.sendEmailsToAllSubscribers(mailTemplateKey);
        return "Batch Job eingereiht!";
    }
}

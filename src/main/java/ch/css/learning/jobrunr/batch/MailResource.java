package ch.css.learning.jobrunr.batch;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/mail")
@Produces(MediaType.TEXT_PLAIN)
public class MailResource {

    @Inject
    SendBulkMailBatch sendBulkMailBatch;

    @GET
    @Path("/start")
    public String triggerLongJob(@DefaultValue("general-template") @QueryParam("template") String mailTemplateKey) {
        sendBulkMailBatch.startSendBulkMailBatch(mailTemplateKey);
        return "Batch Job eingereiht!";
    }
}

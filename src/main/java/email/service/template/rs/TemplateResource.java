package email.service.template.rs;

import com.codahale.metrics.annotation.Timed;
import email.service.template.api.Template;
import email.service.template.db.TemplateDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/templates")
@Produces(MediaType.APPLICATION_JSON)
@Api("Email Templates")
@Singleton
public class TemplateResource {

  private final TemplateDao dao;

  @Inject
  public TemplateResource(TemplateDao dao) {
    this.dao = dao;
  }

  @Timed
  @GET
  @ApiOperation("Get All Templates")
  public List<Template> getAllTemplates(
          @DefaultValue("EN") @QueryParam("language") String language) {
    return dao.findAll(language);
  }
}
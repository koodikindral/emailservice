package email.service.template.rs;

import com.codahale.metrics.annotation.Timed;
import email.service.email.EmailService;
import email.service.template.api.Template;
import email.service.template.db.TemplateDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.antlr.stringtemplate.StringTemplate;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/templates")
@Produces(MediaType.APPLICATION_JSON)
@Api("Email Templates")
@Singleton
public class TemplateResource {

  private final TemplateDao dao;
  private final EmailService emailService;

  @Inject
  public TemplateResource(TemplateDao dao, EmailService emailService) {
    this.dao = dao;
    this.emailService = emailService;
  }

  @Timed
  @GET
  @ApiOperation("Get All Templates")
  public List<Template> findAll(
          @DefaultValue("EN") @QueryParam("language") String language) {
    return dao.findAll(language);
  }

  @Timed
  @GET
  @ApiOperation("Get Template by ID")
  @Path("{id}")
  public Template find(
          @PathParam("id") Long id,
          @DefaultValue("EN") @QueryParam("language") String language) {
    return dao.find(id, language);
  }

  @Timed
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation("Create Template")
  public Template create(
          @Valid @ApiParam(required = true) Template template,
          @DefaultValue("EN") @QueryParam("language") String language) {
    final Long id = dao.create(template);
    template.setId(id);
    dao.updateTranslation(template, language);
    return dao.find(id, language);
  }

  @Timed
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation("Modify Template")
  @Path("{id}")
  public Template update(
          @PathParam("id") Long id,
          @Valid @ApiParam(required = true) Template template,
          @DefaultValue("EN") @QueryParam("language") String language) {
    dao.update(id, template);
    dao.updateTranslation(template, language);
    return dao.find(id, language);
  }

  @Timed
  @DELETE
  @ApiOperation("Delete Template")
  @Path("{id}")
  public Response update(
          @PathParam("id") Long id) {
    dao.deleteTranslation(id);
    dao.delete(id);
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  @Timed
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation("Test Template")
  @Path("{id}/test")
  public Response test(
          @PathParam("id") Long id,
          @DefaultValue("EN") @QueryParam("language") String language,
          HashMap<String, String> tags) {
    final Template template = dao.find(id, language);
    StringTemplate text = new StringTemplate(template.getBodyText());
    StringTemplate html = new StringTemplate(template.getBodyHtml());

    for (Map.Entry<String, String> entry : tags.entrySet()) {
      text.setAttribute(entry.getKey(), entry.getValue());
      html.setAttribute(entry.getKey(), entry.getValue());
    }

    emailService.sendEmail("gert.vesterberg@gmail.com", template.getTitle(), text.toString(), html.toString());
    return Response.status(Response.Status.OK).build();
  }
}
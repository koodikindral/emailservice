package email.service.template.rs;

import com.codahale.metrics.annotation.Timed;
import email.service.template.api.Template;
import email.service.template.db.TemplateDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    dao.delete(id);
    dao.deleteTranslation(id);
    return Response.status(Response.Status.NO_CONTENT).build();
  }
}
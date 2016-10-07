package email.service.hello.rs;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import email.service.hello.api.Hello;
import email.service.hello.db.HelloDao;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
@Api("HelloWorld")
@Singleton
public class HelloResource {

  private final HelloDao dao;

  @Inject
  public HelloResource(HelloDao dao) {
    this.dao = dao;
  }

  @Timed
  @GET
  @ApiOperation("Get All Saying")
  public List<Hello> getAllSaying() {
    return dao.findAll();
  }
}
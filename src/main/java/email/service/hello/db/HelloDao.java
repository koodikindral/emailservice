package email.service.hello.db;

import com.hubspot.rosetta.jdbi.RosettaMapperFactory;
import email.service.hello.api.Hello;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import java.util.List;

@RegisterMapperFactory(RosettaMapperFactory.class)
@UseStringTemplate3StatementLocator
public interface HelloDao {

    @SqlQuery("SELECT id, saying FROM sayings")
    List<Hello> findAll();
}
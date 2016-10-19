package email.service.template.db;

import com.hubspot.rosetta.jdbi.RosettaMapperFactory;
import email.service.template.api.Template;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import java.util.List;

@RegisterMapperFactory(RosettaMapperFactory.class)
@UseStringTemplate3StatementLocator
public interface TemplateDao {

  @SqlQuery("SELECT tpl.id, tpl.code, tpl_t.body_text, tpl_t.body_html FROM emailtemplates tpl " +
            "LEFT JOIN emailtemplate_translations tpl_t ON tpl_t.emailtemplate_id = tpl.id AND tpl_t.country_code = :language " +
            "ORDER BY tpl.id ASC")
  List<Template> findAll(@Bind("language") String language);
}
package email.service.template.db;

import com.hubspot.rosetta.jdbi.RosettaMapperFactory;
import email.service.template.api.Template;
import org.skife.jdbi.v2.sqlobject.*;
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

  @SqlUpdate("INSERT INTO emailtemplates (code) values (:code)")
  @GetGeneratedKeys
  long create(@BindBean Template template);

  @SqlUpdate("UPDATE emailtemplates SET code = COALESCE(:code, code) WHERE id = :id")
  void update(@BindBean Template template);

  @SqlUpdate("UPDATE emailtemplate_translations SET body_text = :bodyText, body_html = :bodyHtml WHERE country_code = :language AND emailtemplate_id = :id; " +
             "INSERT INTO emailtemplate_translations (country_code, emailtemplate_id, body_text, body_html) SELECT :language, :id, :bodyText, :bodyHtml WHERE NOT EXISTS (SELECT 1 FROM emailtemplate_translations WHERE country_code = :language AND emailtemplate_id = :id)")
  void updateTranslation(@BindBean Template template, @Bind("language") String language);

  @SqlUpdate("DELETE FROM emailtemplates WHERE id = :id")
  void delete(@Bind("id") Long id);

  @SqlUpdate("DELETE FROM emailtemplate_translations WHERE emailtemplate_id = :id")
  void deleteTranslation(@Bind("id") Long id);
}
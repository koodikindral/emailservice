CREATE SEQUENCE application.emailtemplates_id_seq
    INCREMENT 1
    START 2
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE application.emailtemplates_id_seq
    OWNER TO postgres;

CREATE TABLE application.emailtemplates
(
    id integer NOT NULL DEFAULT nextval('application.emailtemplates_id_seq'::regclass),
    code text NOT NULL,
    CONSTRAINT emailtemplates_pkey PRIMARY KEY (id),
    CONSTRAINT emailtemplate_code_key UNIQUE (code)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE application.emailtemplates
    OWNER to postgres;


CREATE TABLE application.emailtemplate_translations
(
    emailtemplate_id integer,
    country_code text NOT NULL,
    title text,
    body_text text,
    body_html text,
    CONSTRAINT emailtemplate_key UNIQUE (country_code, emailtemplate_id),
    CONSTRAINT emailtemplate_id_fkey FOREIGN KEY (emailtemplate_id)
        REFERENCES application.emailtemplates (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE application.emailtemplate_translations
    OWNER to postgres;


CREATE INDEX fki_emailtemplate_id_fkey
    ON application.emailtemplate_translations USING btree
    (emailtemplate_id)
    TABLESPACE pg_default;
CREATE TABLE admin_tab
(
  id  INT AUTO_INCREMENT
    PRIMARY KEY,
  psd VARCHAR(32) NOT NULL,
  CONSTRAINT admin_tab_id_uindex
  UNIQUE (id),
  CONSTRAINT admin_tab_psd_uindex
  UNIQUE (psd)
)
  ENGINE = InnoDB;

CREATE TABLE format_processor_tab
(
  id                          INT AUTO_INCREMENT
    PRIMARY KEY,
  format_processor_name       VARCHAR(64)  NOT NULL,
  format_processor_class_name VARCHAR(256) NOT NULL,
  CONSTRAINT format_processor_tab_id_uindex
  UNIQUE (id),
  CONSTRAINT format_processor_tab_format_processor_name_uindex
  UNIQUE (format_processor_name),
  CONSTRAINT format_processor_tab_format_processor_class_name_uindex
  UNIQUE (format_processor_class_name)
)
  ENGINE = InnoDB;

CREATE TABLE resource_tab
(
  id            INT AUTO_INCREMENT
    PRIMARY KEY,
  resource_name VARCHAR(64)  NOT NULL,
  resource_size DOUBLE       NOT NULL,
  resource_url  VARCHAR(512) NOT NULL,
  CONSTRAINT resource_tab_id_uindex
  UNIQUE (id),
  CONSTRAINT resource_tab_resource_name_uindex
  UNIQUE (resource_name),
  CONSTRAINT resource_tab_resource_url_uindex
  UNIQUE (resource_url)
)
  ENGINE = InnoDB;

CREATE TABLE subject_tab
(
  id                     INT AUTO_INCREMENT
    PRIMARY KEY,
  subject_name           VARCHAR(64)  NOT NULL,
  teacher_name           VARCHAR(32)  NOT NULL,
  email_address          VARCHAR(128) NOT NULL,
  recommend_processor_id INT          NOT NULL,
  CONSTRAINT subject_tab_subject_id_uindex
  UNIQUE (id),
  CONSTRAINT subject_tab_subject_name_uindex
  UNIQUE (subject_name)
)
  ENGINE = InnoDB;

CREATE TABLE submission_tab
(
  id          INT AUTO_INCREMENT
    PRIMARY KEY,
  task_id     INT          NOT NULL,
  user_id     INT          NOT NULL,
  team_id     INT          NULL,
  submit_data DATETIME     NOT NULL,
  former_name VARCHAR(128) NOT NULL,
  save_name   VARCHAR(128) NOT NULL,
  size        DOUBLE       NOT NULL,
  path        VARCHAR(512) NOT NULL,
  CONSTRAINT submission_tab_id_uindex
  UNIQUE (id),
  CONSTRAINT submission_tab_path_uindex
  UNIQUE (path)
)
  ENGINE = InnoDB;

CREATE INDEX submission_tab_task_tab_id_fk
  ON submission_tab (task_id);

CREATE INDEX submission_tab_user_tab_id_fk
  ON submission_tab (user_id);

CREATE INDEX submission_tab_team_tab_id_fk
  ON submission_tab (team_id);

CREATE TABLE task_tab
(
  id                  INT AUTO_INCREMENT
    PRIMARY KEY,
  subject_id          INT             NOT NULL,
  title               VARCHAR(128)    NOT NULL,
  content             VARCHAR(2048)   NOT NULL,
  is_team_task        TINYINT(1)      NOT NULL,
  deadline            DATETIME        NOT NULL,
  type                VARCHAR(32)     NOT NULL,
  format_processor_id INT DEFAULT '1' NOT NULL,
  format              VARCHAR(128)    NULL,
  CONSTRAINT task_tab_id_uindex
  UNIQUE (id),
  CONSTRAINT task_tab_subject_tab_id_fk
  FOREIGN KEY (subject_id) REFERENCES subject_tab (id)
    ON UPDATE CASCADE
)
  ENGINE = InnoDB;

CREATE INDEX task_tab_subject_tab_id_fk
  ON task_tab (subject_id);

ALTER TABLE submission_tab
  ADD CONSTRAINT submission_tab_task_tab_id_fk
FOREIGN KEY (task_id) REFERENCES task_tab (id)
  ON UPDATE CASCADE;

CREATE TABLE team_tab
(
  id                   INT AUTO_INCREMENT
    PRIMARY KEY,
  subject_id           INT         NOT NULL,
  team_index           INT         NOT NULL,
  team_name            VARCHAR(64) NULL,
  member_user_id_array VARCHAR(64) NOT NULL,
  leader_user_id_array VARCHAR(64) NOT NULL,
  CONSTRAINT team_tab_id_uindex
  UNIQUE (id),
  CONSTRAINT team_tab_subject_tab_id_fk
  FOREIGN KEY (subject_id) REFERENCES subject_tab (id)
    ON UPDATE CASCADE
)
  ENGINE = InnoDB;

CREATE INDEX team_tab_subject_tab_id_fk
  ON team_tab (subject_id);

ALTER TABLE submission_tab
  ADD CONSTRAINT submission_tab_team_tab_id_fk
FOREIGN KEY (team_id) REFERENCES team_tab (id)
  ON UPDATE CASCADE;

CREATE TABLE user_tab
(
  id              INT AUTO_INCREMENT
    PRIMARY KEY,
  user_code       VARCHAR(64)            NOT NULL,
  user_name       VARCHAR(64)            NOT NULL,
  theme           VARCHAR(32)            NULL,
  access_by_token TINYINT(1) DEFAULT '0' NOT NULL,
  access_token    VARCHAR(128)           NULL,
  CONSTRAINT user_tab_id_uindex
  UNIQUE (id),
  CONSTRAINT user_tab_user_code_uindex
  UNIQUE (user_code)
)
  ENGINE = InnoDB;

ALTER TABLE submission_tab
  ADD CONSTRAINT submission_tab_user_tab_id_fk
FOREIGN KEY (user_id) REFERENCES user_tab (id)
  ON UPDATE CASCADE;


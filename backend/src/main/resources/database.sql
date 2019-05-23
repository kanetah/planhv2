create table admin_tab
(
  id int auto_increment
    primary key,
  word varchar(32) not null,
  allow_new_key tinyint(1) null,
  access_keys text null,
  constraint admin_tab_id_uindex
  unique (id),
  constraint admin_tab_psd_uindex
  unique (word)
)
  engine=InnoDB
;

create table auth_tab
(
  id int auto_increment
    primary key,
  admin_id int not null,
  authorized varchar(2048) not null,
  constraint auth_intfo_tab_id_uindex
  unique (id),
  constraint auth_intfo_tab_admin_id_uindex
  unique (admin_id),
  constraint auth_intfo_tab_admin_tab_id_fk
  foreign key (admin_id) references admin_tab (id)
    on update cascade on delete cascade
)
  engine=InnoDB
;

create table format_processor_tab
(
  id int auto_increment
    primary key,
  format_processor_name varchar(64) not null,
  format_processor_class_name varchar(256) not null,
  constraint format_processor_tab_id_uindex
  unique (id),
  constraint format_processor_tab_format_processor_name_uindex
  unique (format_processor_name),
  constraint format_processor_tab_format_processor_class_name_uindex
  unique (format_processor_class_name)
)
  engine=InnoDB
;

create table resource_tab
(
  id int auto_increment
    primary key,
  resource_name varchar(64) not null,
  resource_size double not null,
  resource_url varchar(512) not null,
  constraint resource_tab_id_uindex
  unique (id),
  constraint resource_tab_resource_url_uindex
  unique (resource_url)
)
  engine=InnoDB
;

create table subject_tab
(
  id int auto_increment
    primary key,
  subject_name varchar(64) not null,
  teacher_name varchar(32) not null,
  email_address varchar(128) not null,
  team_limit varchar(32) null,
  recommend_processor_id int not null,
  constraint subject_tab_subject_id_uindex
  unique (id),
  constraint subject_tab_subject_name_uindex
  unique (subject_name)
)
  engine=InnoDB
;

create table submission_tab
(
  id int auto_increment
    primary key,
  task_id int not null,
  user_id int not null,
  team_id int null,
  submit_date datetime not null,
  resource_id int not null,
  former_name varchar(128) not null,
  save_name varchar(128) not null,
  size double not null,
  path varchar(512) not null,
  constraint submission_tab_id_uindex
  unique (id),
  constraint submission_tab_resource_id_uindex
  unique (resource_id),
  constraint submission_tab_path_uindex
  unique (path),
  constraint submission_tab_resource_tab_id_fk
  foreign key (resource_id) references resource_tab (id)
    on update cascade
)
  engine=InnoDB
;

create index submission_tab_task_tab_id_fk
  on submission_tab (task_id)
;

create index submission_tab_user_tab_id_fk
  on submission_tab (user_id)
;

create index submission_tab_team_tab_id_fk
  on submission_tab (team_id)
;

create table task_tab
(
  id int auto_increment
    primary key,
  subject_id int not null,
  title varchar(128) not null,
  content varchar(2048) not null,
  is_team_task tinyint(1) not null,
  deadline datetime not null,
  type varchar(32) not null,
  format_processor_id int default '1' not null,
  format varchar(128) null,
  constraint task_tab_id_uindex
  unique (id),
  constraint task_tab_subject_tab_id_fk
  foreign key (subject_id) references subject_tab (id)
    on update cascade
)
  engine=InnoDB
;

create index task_tab_subject_tab_id_fk
  on task_tab (subject_id)
;

alter table submission_tab
  add constraint submission_tab_task_tab_id_fk
foreign key (task_id) references task_tab (id)
  on update cascade
;

create table team_tab
(
  id int auto_increment
    primary key,
  subject_id int not null,
  team_index int not null,
  team_name varchar(64) null,
  member_user_id_array varchar(128) not null,
  leader_user_id_array varchar(64) not null,
  constraint team_tab_id_uindex
  unique (id),
  constraint team_tab_subject_tab_id_fk
  foreign key (subject_id) references subject_tab (id)
    on update cascade
)
  engine=InnoDB
;

create index team_tab_subject_tab_id_fk
  on team_tab (subject_id)
;

alter table submission_tab
  add constraint submission_tab_team_tab_id_fk
foreign key (team_id) references team_tab (id)
  on update cascade
;

create table token_tab
(
  id int auto_increment
    primary key,
  user_id int not null,
  token varchar(2048) not null,
  constraint token_table_id_uindex
  unique (id),
  constraint token_table_user_id_uindex
  unique (user_id)
)
  engine=InnoDB
;

create table user_tab
(
  id int auto_increment
    primary key,
  user_code varchar(64) not null,
  user_name varchar(64) not null,
  theme varchar(32) null,
  enable_access_token tinyint(1) default '0' not null,
  access_token varchar(128) null,
  constraint user_tab_id_uindex
  unique (id),
  constraint user_tab_user_code_uindex
  unique (user_code)
)
  engine=InnoDB
;

alter table submission_tab
  add constraint submission_tab_user_tab_id_fk
foreign key (user_id) references user_tab (id)
  on update cascade
;

alter table token_tab
  add constraint token_table_user_tab_id_fk
foreign key (user_id) references user_tab (id)
;


CREATE TABLE user_details (id bigint NOT NULL auto_increment, aadhar_number varchar(12), backup_email varchar(64), created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, email varchar(50), mobile_number varchar(10), password varchar(20), user_name varchar(30), PRIMARY KEY (id)) engine=InnoDB;
CREATE TABLE posts (id bigint NOT NULL auto_increment, content TEXT, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, updated_at datetime(6), user_id bigint NOT NULL, PRIMARY KEY (id)) engine=InnoDB;
CREATE TABLE comments (id bigint NOT NULL auto_increment, content TEXT, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, updated_at datetime(6), post_id bigint NOT NULL, PRIMARY KEY (id)) engine=InnoDB;
CREATE TABLE post_likes (post_id bigint NOT NULL, user_id bigint NOT NULL, PRIMARY KEY (post_id, user_id)) engine=InnoDB;
alter TABLE user_details ADD CONSTRAINT unique_userName_constraint unique (user_name);
alter TABLE user_details ADD CONSTRAINT unique_email_constraint unique (email);
alter TABLE user_details ADD CONSTRAINT unique_mobileNumber_constraint unique (mobile_number);
alter TABLE user_details ADD CONSTRAINT unique_aadharNumber_constraint unique (aadhar_number);
alter TABLE comments ADD CONSTRAINT post_reference_in_comments foreign key (post_id) references posts (id);
alter TABLE post_likes ADD CONSTRAINT user_reference_in_post_likes foreign key (user_id) references user_details (id);
alter TABLE post_likes ADD CONSTRAINT post_reference_in_post_likes foreign key (post_id) references posts (id);
alter TABLE posts ADD CONSTRAINT user_reference_in_posts foreign key (user_id) references user_details (id);
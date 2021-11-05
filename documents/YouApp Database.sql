  create database youapp;

  use youapp;

  create table oauth2_registration_type(
    id int primary key auto_increment,
    registration_type varchar(64) not null unique
  );

  CREATE TABLE oauth2_registered_client (
    id varchar(100) NOT NULL,
    client_id varchar(100) NOT NULL,
    client_id_issued_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret varchar(512) DEFAULT NULL,
    client_secret_expires_at timestamp DEFAULT NULL,
    client_name varchar(200) NOT NULL,
    client_authentication_methods varchar(1000) NOT NULL,
    authorization_grant_types varchar(1000) NOT NULL,
    redirect_uris varchar(1000) DEFAULT NULL,
    scopes varchar(1000) NOT NULL,
    client_settings varchar(2000) NOT NULL,
    token_settings varchar(2000) NOT NULL,
    PRIMARY KEY (id)
  );

  CREATE TABLE oauth2_authorization (
    id varchar(100) NOT NULL,
    registered_client_id varchar(100) NOT NULL,
    principal_name varchar(200) NOT NULL,
    authorization_grant_type varchar(100) NOT NULL,
    attributes varchar(4000) DEFAULT NULL,
    state varchar(500) DEFAULT NULL,
    authorization_code_value blob DEFAULT NULL,
    authorization_code_issued_at timestamp DEFAULT NULL,
    authorization_code_expires_at timestamp DEFAULT NULL,
    authorization_code_metadata varchar(2000) DEFAULT NULL,
    access_token_value blob DEFAULT NULL,
    access_token_issued_at timestamp DEFAULT NULL,
    access_token_expires_at timestamp DEFAULT NULL,
    access_token_metadata varchar(2000) DEFAULT NULL,
    access_token_type varchar(100) DEFAULT NULL,
    access_token_scopes varchar(1000) DEFAULT NULL,
    oidc_id_token_value blob DEFAULT NULL,
    oidc_id_token_issued_at timestamp DEFAULT NULL,
    oidc_id_token_expires_at timestamp DEFAULT NULL,
    oidc_id_token_metadata varchar(2000) DEFAULT NULL,
    refresh_token_value blob DEFAULT NULL,
    refresh_token_issued_at timestamp DEFAULT NULL,
    refresh_token_expires_at timestamp DEFAULT NULL,
    refresh_token_metadata varchar(2000) DEFAULT NULL,
    PRIMARY KEY (id)
  );

  insert into oauth2_registration_type values (null,"YOUAPP"),(null,"GOOGLE"), (null,"OTHER");

  create table role(
    id int primary key auto_increment,
    name varchar(32) not null unique
  );  

  insert into role values (null,"ROLE_ADMIN"),(null,"ROLE_MANTAINER"),(null,"ROLE_USER");

  create table privilege(
    id int primary key auto_increment,
    name varchar(32) not null unique
  );

  insert into privilege values (null,"READ_PRIVILEGE"),(null,"WRITE_PRIVILEGE"),(null,"PERMISSIONS_PRIVILEGE");

  create table roles_privileges(
    role_id int not null,
    privilege_id int not null,
    foreign key(role_id) references role(id) 
    ON DELETE CASCADE ON UPDATE CASCADE,
    foreign key(privilege_id) references privilege(id)
    ON UPDATE CASCADE ON DELETE CASCADE 
  );

  insert into roles_privileges values (1,1),(1,2),(1,3),(3,1);
  
  create table user(
      id varchar(56) primary key,
      given_name varchar(32) not null,
      family_name varchar(32) not null,
      email varchar(48) not null unique,
      username varchar(32) not null unique,
      password varchar(512) null,
      birthday date null,
      description varchar(256) default '',
      photo varchar(512) null,
      registration_date date null,
      email_confirmed boolean default false,
      registration_type_id int not null, 
      foreign key(registration_type_id) references oauth2_registration_type(id) 
  );

  DELIMITER $$
    CREATE TRIGGER `before_user_insert` 
    BEFORE INSERT ON `user` 
    FOR EACH ROW BEGIN 
     IF (new.id IS null) THEN
       SET new.id = uuid();
    END IF;
  END $$
  DELIMITER ;

  insert into user(id,given_name,family_name,email,username,password,birthday,registration_date,email_confirmed,registration_type_id) 
  values("4fafcfa3-bf1c-4c5f-b5b8-51a10b389f5f","Alejandro","Alejo","alejandroalejo714@gmail.com","alejandro","KdNiQ6GAvFdPEaPaerJ9f9l/kLPXkybvLSQOX+rXInEQFNBA+x0aj07C/yhfxbAhlv1EFS+MooI0O6YZlkHLkbFB0sjGf2Rocr5zY92dhGZdLmTEldvi92qfR40DZqWPkBFwVMdPD2GcZIJSEhFNcKrlj7DeCF3iG8VGF55ogW7qTZvrBJCjFZlMqoQSgnwZiyxwcNQfnPAO4NR+IhKXy28BBRd6dNy/31esyurdCwk22AipxLskoex/Yg7rXuzHEA6M9xuvub8nUfoSigL6SwRjsJ4w9x1kgzeR6W2iVWqCNeVctZObIIRk2A6ayURXcAhfYjHtceSdCf70VI65KQ==",
  "2020-07-14",null,true,1);

  create table email_token(
    id int primary key auto_increment,
    token varchar(48) not null unique,
    expiration_date timestamp not null,
    user_id varchar(56),
    foreign key(user_id) references user(id)
    ON UPDATE CASCADE ON DELETE CASCADE 
  );

  create table users_roles(
    user_id varchar(56) not null,
    role_id int not null,
    foreign key(user_id) references user(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
    foreign key(role_id) references role(id)
    ON UPDATE CASCADE ON DELETE CASCADE
  );

  insert into users_roles values("4fafcfa3-bf1c-4c5f-b5b8-51a10b389f5f" ,1);

  create table genre(
      id int primary key auto_increment,
      title varchar(32) not null
  );

  insert into genre(title) values ("Rock"),("Pop"),("Rap"),("Electronica")
                           ,("Metal"),("Salsa"),("Reggaeton"),("Banda");

  create table status(
     id int primary key auto_increment,
     status varchar(32) not null
  );

  insert into status(status) values ("PENDING"),("UPLOADING"),("FAILED"),("READY");

  create table music(
      id int primary key auto_increment,
      title varchar(128) not null,
      duration int,
      song_url varchar(256) not null ,
      status_id int,
      genre_id int,
      user_id varchar(56),
      foreign key (status_id) references status(id),
      foreign key (genre_id) references genre(id),
      foreign key (user_id) references user(id)
  );

  create table favorites(
      id int primary key auto_increment,
      music_id int not null ,
      foreign key (music_id) references music(id),
      user_id varchar(56) not null ,
      foreign key (user_id) references user(id)
  );

  create table playlist(
      id int primary key auto_increment,
      title varchar(64) not null,
      user_id varchar(56) not null ,
      foreign key (user_id) references user(id)
      ON DELETE CASCADE
  );

  create table playlist_song(
      id int primary key auto_increment,
      music_id int not null ,
      foreign key (music_id) references music(id),
      playlist_id int not null ,
      foreign key (playlist_id) references playlist(id)
  );

  SET @@global.time_zone = '-06:00';

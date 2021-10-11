  create database youapp;

  use youapp;

  create table oauth_registration_type(
    id int primary key auto_increment,
    registration_type varchar(64) not null unique
  );

  insert into oauth_registration_type values (null,"YOUAPP"),(null,"GOOGLE"), (null,"OTHER");

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
    role_id int,
    privilege_id int,
    foreign key(role_id) references role(id),
    foreign key(privilege_id) references privilege(id)
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
      last_login datetime default CURRENT_TIMESTAMP,
      email_confirmed boolean default false,
      registration_type_id int not null, 
      foreign key(registration_type_id) references oauth_registration_type(id) 
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

  insert into user(given_name,family_name,email,username,password,birthday,last_login,email_confirmed,registration_type_id) values("Alejandro","Alejo","alejandroalejo714@gmail.com","alejandro","KdNiQ6GAvFdPEaPaerJ9f9l/kLPXkybvLSQOX+rXInEQFNBA+x0aj07C/yhfxbAhlv1EFS+MooI0O6YZlkHLkbFB0sjGf2Rocr5zY92dhGZdLmTEldvi92qfR40DZqWPkBFwVMdPD2GcZIJSEhFNcKrlj7DeCF3iG8VGF55ogW7qTZvrBJCjFZlMqoQSgnwZiyxwcNQfnPAO4NR+IhKXy28BBRd6dNy/31esyurdCwk22AipxLskoex/Yg7rXuzHEA6M9xuvub8nUfoSigL6SwRjsJ4w9x1kgzeR6W2iVWqCNeVctZObIIRk2A6ayURXcAhfYjHtceSdCf70VI65KQ==",
  "2020-07-14",null,true,1),
  ("Victor","Alejo","alejoalejandro714@gmail.com","alejo","KdNiQ6GAvFdPEaPaerJ9f9l/kLPXkybvLSQOX+rXInEQFNBA+x0aj07C/yhfxbAhlv1EFS+MooI0O6YZlkHLkbFB0sjGf2Rocr5zY92dhGZdLmTEldvi92qfR40DZqWPkBFwVMdPD2GcZIJSEhFNcKrlj7DeCF3iG8VGF55ogW7qTZvrBJCjFZlMqoQSgnwZiyxwcNQfnPAO4NR+IhKXy28BBRd6dNy/31esyurdCwk22AipxLskoex/Yg7rXuzHEA6M9xuvub8nUfoSigL6SwRjsJ4w9x1kgzeR6W2iVWqCNeVctZObIIRk2A6ayURXcAhfYjHtceSdCf70VI65KQ==",
  "2020-07-14",null,true,1);

  create table users_roles(
    user_id varchar(56),
    role_id int,
    foreign key(user_id) references user(id)
    ON DELETE CASCADE,
    foreign key(role_id) references role(id)
  );

  insert into users_roles values((SELECT (id) FROM user limit 1) ,1);

  create table artist(
      id int primary key auto_increment,
      photo blob,
      age int,
      descripction varchar(512),
      user_id varchar(56) not null unique ,
      foreign key (user_id) references user(id) 
      ON DELETE CASCADE
  );

  create table genre(
      id int primary key auto_increment,
      title varchar(128) not null
  );

  create table music(
      id int primary key auto_increment,
      title varchar(128) not null,
      duration time,
      song_url varchar(256) not null ,
      genre_id int,
      foreign key (genre_id) references genre(id),
      artist_id int,
      foreign key (artist_id) references artist(id)
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
      title varchar(64) default 'Playlist',
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

  create table album(
      id int primary key auto_increment,
      title varchar(64) not null ,
      duration time,
      genre_id int,
      foreign key (genre_id) references genre(id),
      artist_id int,
      foreign key (artist_id) references artist(id)
  );

  create table album_songs(
      id int primary key auto_increment,
      album_id int not null ,
      foreign key (album_id) references album(id),
      music_id int not null unique ,
      foreign key (music_id) references music(id)
  );
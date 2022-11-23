  create database if not exists youapp;

  use youapp;

  create table if not exists oauth2_registration_type(
    id int primary key auto_increment,
    registration_type varchar(64) not null unique
  );

  create table if not exists oauth2_registered_client (
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

  create table if not exists oauth2_authorization (
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

  create table if not exists role(
    id int primary key auto_increment,
    name varchar(32) not null unique
  );  

  insert into role values (null,"ROLE_ADMIN"),(null,"ROLE_MAINTAINER"),(null,"ROLE_USER");

  create table if not exists privilege(
    id int primary key auto_increment,
    name varchar(32) not null unique
  );

  insert into privilege values (null,"READ_PRIVILEGE"),(null,"WRITE_PRIVILEGE"),(null,"PERMISSIONS_PRIVILEGE");

  create table if not exists roles_privileges(
    id int not null auto_increment,
    role_id int not null,
    privilege_id int not null,
    foreign key(role_id) references role(id) 
    ON DELETE CASCADE ON UPDATE CASCADE,
    foreign key(privilege_id) references privilege(id)
    ON UPDATE CASCADE ON DELETE CASCADE 
  );

  insert into roles_privileges values (1,1),(1,2),(1,3),(3,1);
  
  create table if not exists user(
      id varchar(56) primary key,
      given_name varchar(32) not null,
      family_name varchar(32) not null,
      email varchar(48) not null unique,
      username varchar(32) not null unique,
      password varchar(512) null,
      birthday date null,
  --  description varchar(256) default '',
      photo varchar(512) null,
      registration_date date null,
      email_confirmed boolean default false,
      registration_type_id int not null,
      role_id int not null,
      foreign key(registration_type_id) references oauth2_registration_type(id)
      ON UPDATE CASCADE ON DELETE CASCADE,
      foreign key(role_id) references role(id) ON UPDATE CASCADE ON DELETE CASCADE
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

  insert into user(id,given_name,family_name,email,username,password,birthday,registration_date,email_confirmed,registration_type_id,role_id)
  values
      ("4fafcfa3-bf1c-4c5f-b5b8-51a10b389f5f","Alejandro","Alejo","alejandroalejo715@gmail.com","alejandro","KdNiQ6GAvFdPEaPaerJ9f9l/kLPXkybvLSQOX+rXInEQFNBA+x0aj07C/yhfxbAhlv1EFS+MooI0O6YZlkHLkbFB0sjGf2Rocr5zY92dhGZdLmTEldvi92qfR40DZqWPkBFwVMdPD2GcZIJSEhFNcKrlj7DeCF3iG8VGF55ogW7qTZvrBJCjFZlMqoQSgnwZiyxwcNQfnPAO4NR+IhKXy28BBRd6dNy/31esyurdCwk22AipxLskoex/Yg7rXuzHEA6M9xuvub8nUfoSigL6SwRjsJ4w9x1kgzeR6W2iVWqCNeVctZObIIRk2A6ayURXcAhfYjHtceSdCf70VI65KQ==",
        "2000-07-14",null,true,1,1),
      ("77503902-813b-42d2-afe2-66e475c9da6b","Matias","Gaitan","matias25@gmail.com","matias","KdNiQ6GAvFdPEaPaerJ9f9l/kLPXkybvLSQOX+rXInEQFNBA+x0aj07C/yhfxbAhlv1EFS+MooI0O6YZlkHLkbFB0sjGf2Rocr5zY92dhGZdLmTEldvi92qfR40DZqWPkBFwVMdPD2GcZIJSEhFNcKrlj7DeCF3iG8VGF55ogW7qTZvrBJCjFZlMqoQSgnwZiyxwcNQfnPAO4NR+IhKXy28BBRd6dNy/31esyurdCwk22AipxLskoex/Yg7rXuzHEA6M9xuvub8nUfoSigL6SwRjsJ4w9x1kgzeR6W2iVWqCNeVctZObIIRk2A6ayURXcAhfYjHtceSdCf70VI65KQ==",
        "1999-11-15",null,true,1,2),
      ("809877e9-ccc6-4a75-a008-11087f8f4c84","Denys","Cruz","denys7u7@gmail.com","denys","KdNiQ6GAvFdPEaPaerJ9f9l/kLPXkybvLSQOX+rXInEQFNBA+x0aj07C/yhfxbAhlv1EFS+MooI0O6YZlkHLkbFB0sjGf2Rocr5zY92dhGZdLmTEldvi92qfR40DZqWPkBFwVMdPD2GcZIJSEhFNcKrlj7DeCF3iG8VGF55ogW7qTZvrBJCjFZlMqoQSgnwZiyxwcNQfnPAO4NR+IhKXy28BBRd6dNy/31esyurdCwk22AipxLskoex/Yg7rXuzHEA6M9xuvub8nUfoSigL6SwRjsJ4w9x1kgzeR6W2iVWqCNeVctZObIIRk2A6ayURXcAhfYjHtceSdCf70VI65KQ==",
        "1999-03-04",null,true,1,3),
      ("3feab42f-3092-4357-9ce6-60f17b4c0ffb","Misaki","Kun","alvaromisaki@gmail.com","misaki","KdNiQ6GAvFdPEaPaerJ9f9l/kLPXkybvLSQOX+rXInEQFNBA+x0aj07C/yhfxbAhlv1EFS+MooI0O6YZlkHLkbFB0sjGf2Rocr5zY92dhGZdLmTEldvi92qfR40DZqWPkBFwVMdPD2GcZIJSEhFNcKrlj7DeCF3iG8VGF55ogW7qTZvrBJCjFZlMqoQSgnwZiyxwcNQfnPAO4NR+IhKXy28BBRd6dNy/31esyurdCwk22AipxLskoex/Yg7rXuzHEA6M9xuvub8nUfoSigL6SwRjsJ4w9x1kgzeR6W2iVWqCNeVctZObIIRk2A6ayURXcAhfYjHtceSdCf70VI65KQ==",
       "1999-12-30",null,true,1,3);

  create table if not exists email_token(
    id int primary key auto_increment,
    token varchar(48) not null unique,
    expiration_date timestamp not null,
    user_id varchar(56),
    foreign key(user_id) references user(id)
    ON UPDATE CASCADE ON DELETE CASCADE 
  );

--   create table if not exists users_roles(
--     id int primary key auto_increment,
--     user_id varchar(56) not null,
--     role_id int not null,
--     foreign key(user_id) references user(id)
--     ON UPDATE CASCADE ON DELETE CASCADE,
--     foreign key(role_id) references role(id)
--     ON UPDATE CASCADE ON DELETE CASCADE
--   );

--   insert into users_roles(user_id,role_id) values("4fafcfa3-bf1c-4c5f-b5b8-51a10b389f5f" ,1),
--                                 ("77503902-813b-42d2-afe2-66e475c9da6b" ,3);

  create table if not exists genre(
      id int primary key auto_increment,
      title varchar(32) not null
  );

  insert into genre(title) values ("Rock"),("Pop"),("Rap"),("Electronica")
                           ,("Metal"),("Salsa"),("Reggaeton"),("Banda");

  create table if not exists status(
     id int primary key auto_increment,
     status varchar(32) not null
  );

  insert into status(status) values ("PENDING"),("UPLOADING"),("INCOMPLETE"),("READY"),("FAILED");

  create table if not exists music(
      id int primary key auto_increment,
      title varchar(128) not null,
      duration int,
      song_url varchar(256) not null ,
      photo varchar(512) null,
      status_id int,
      genre_id int,
      user_id varchar(56),
      foreign key (status_id) references status(id) ON UPDATE CASCADE,
      foreign key (genre_id) references genre(id) ON UPDATE CASCADE,
      foreign key (user_id) references user(id) ON UPDATE CASCADE
  );

  -- must change ip
  DELIMITER $$
    CREATE FUNCTION getURI () RETURNS VARCHAR(32) DETERMINISTIC
    BEGIN
      RETURN 'http://192.168.101.17:9090';
    END $$
  DELIMITER ;

  -- This table requires having assets already in minio to work properly
  -- Assets can be found https://drive.google.com/drive/folders/1-eXAO95B4abEIg5hj4B0yGOR0zseSN37?usp=sharing
  INSERT INTO `music` (`id`, `title`, `duration`, `song_url`, `photo`, `status_id`, `genre_id`, `user_id`) VALUES
    (1, 'Sweather Weather', 252, CONCAT(getUri(),'/youapp/sweather_weather.mp3'), CONCAT(getUri(),'/youapp/sweather_weather_artwork.jpg'), 4, 1, '4fafcfa3-bf1c-4c5f-b5b8-51a10b389f5f'),
    (2, 'Lets Kill Tonight', 212, CONCAT(getUri(),'/youapp/lets_kill_tonigth.mp3'), CONCAT(getUri(),'/youapp/lets_kill_tonigth_artwork.jpg'), 4, 1, '4fafcfa3-bf1c-4c5f-b5b8-51a10b389f5f'),
    (3, 'Good Day', 212, CONCAT(getUri(),'/youapp/good_day.mp3'), CONCAT(getUri(),'/youapp/good_day_artwork.jpg'), 4, 2, '77503902-813b-42d2-afe2-66e475c9da6b'),
    (4, '21 Guns', 315, CONCAT(getUri(),'/youapp/21_guns.mp3'), CONCAT(getUri(),'/youapp/21_guns_artwork.jpg'), 4, 1, '3feab42f-3092-4357-9ce6-60f17b4c0ffb'),
    (5, 'Life Goes On', 144, CONCAT(getUri(),'/youapp/life_goes_on.mp3'), CONCAT(getUri(),'/youapp/life_goes_on_artwork.jpg'), 4, 3, '77503902-813b-42d2-afe2-66e475c9da6b'),
    (6, 'Thunderstruck', 240, CONCAT(getUri(),'/youapp/thunderstruck.mp3'), CONCAT(getUri(),'/youapp/ac_dc.jpg'), 4, 1, '3feab42f-3092-4357-9ce6-60f17b4c0ffb'),
    (7, 'Swae Lee', 161, CONCAT(getUri(),'/youapp/swae_lee.mp3'), CONCAT(getUri(),'/youapp/swae_lee_artwork.jpg'), 4, 4, '77503902-813b-42d2-afe2-66e475c9da6b'),
    (8, 'De mi de mi', 229, CONCAT(getUri(),'/youapp/de_mi.mp3'), CONCAT(getUri(),'/youapp/de_mi.png'), 4, 3, '809877e9-ccc6-4a75-a008-11087f8f4c84'),
    (9, 'Hasta Que Aguante', 273, CONCAT(getUri(),'/youapp/hasta_que_el_cuerpo.mp3'), CONCAT(getUri(),'/youapp/hasta_que_el_cuerpo_artwork.jpg'), 4, 1, '3feab42f-3092-4357-9ce6-60f17b4c0ffb'),
    (10, 'I Took A Pill In Ibiza', 237, CONCAT(getUri(),'/youapp/i_took_a_pill.mp3'), CONCAT(getUri(),'/youapp/i_took_a_pill_artwork.jpg'), 4, 5, '77503902-813b-42d2-afe2-66e475c9da6b'),
    (11, 'Come ang get your love', 206, CONCAT(getUri(),'/youapp/come_and_get.mp3'), CONCAT(getUri(),'/youapp/come_and_get_artwork.jpg'), 4, 4, '3feab42f-3092-4357-9ce6-60f17b4c0ffb'),
    (12, 'Blinding Lights', 203, CONCAT(getUri(),'/youapp/blinding_lights.mp3'), CONCAT(getUri(),'/youapp/blinding_lights_artwork.jpg'), 4, 2, '77503902-813b-42d2-afe2-66e475c9da6b'),
    (13, 'She dont give', 230, CONCAT(getUri(),'/youapp/she_dont_give.mp3'), CONCAT(getUri(),'/youapp/she_dont_give_artwork.jpg'), 4, 5, '809877e9-ccc6-4a75-a008-11087f8f4c84'),
    (14, 'Californication', 314, CONCAT(getUri(),'/youapp/californication.mp3'), CONCAT(getUri(),'/youapp/californication_artwork.jpg'), 4, 5, '3feab42f-3092-4357-9ce6-60f17b4c0ffb'),
    (15, 'Golden Days', 255, CONCAT(getUri(),'/youapp/golden_days.mp3'), CONCAT(getUri(),'/youapp/lets_kill_tonigth_artwork.jpg'), 4, 5, '4fafcfa3-bf1c-4c5f-b5b8-51a10b389f5f');

  create table if not exists favorites(
      id int primary key auto_increment,
      music_id int not null ,
      foreign key (music_id) references music(id),
      user_id varchar(56) not null ,
      foreign key (user_id) references user(id)
  );

  insert into favorites (music_id, user_id) values (1,'4fafcfa3-bf1c-4c5f-b5b8-51a10b389f5f'),
                                                   (2, '4fafcfa3-bf1c-4c5f-b5b8-51a10b389f5f');

  create table if not exists playlist(
      id int primary key auto_increment,
      title varchar(64) not null,
      user_id varchar(56) not null ,
      foreign key (user_id) references user(id)
      ON DELETE CASCADE
  );

  insert into playlist (title, user_id) values ('Playlist 1','4fafcfa3-bf1c-4c5f-b5b8-51a10b389f5f');

  create table if not exists playlist_song(
      id int primary key auto_increment,
      music_id int not null ,
      foreign key (music_id) references music(id),
      playlist_id int not null ,
      foreign key (playlist_id) references playlist(id)
  );

  SET @@global.time_zone = '-06:00';

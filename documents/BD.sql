create database youapp;

use youapp;

create table user(
    id int primary key auto_increment,
    name varchar(128),
    surname varchar(128),
    email varchar(64) not null unique ,
    username varchar(32) not null unique ,
    password varchar(32) not null,
    is_artist int
);

create table artist(
    id int primary key auto_increment,
    photo blob,
    age int,
    descripction varchar(512),
    user_id int not null unique ,
    foreign key (user_id) references user(id)
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
    user_id int not null ,
    foreign key (user_id) references user(id)
);

create table playlist(
    id int primary key auto_increment,
    title varchar(64) default 'Playlist',
    user_id int not null ,
    foreign key (user_id) references user(id)
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

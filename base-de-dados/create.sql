-- CREATE.SQL - Base de Dados RunUp
DROP DATABASE IF EXISTS RunUp;
CREATE DATABASE RunUp;
USE RunUp;

create table usuario (
    user_id int not null auto_increment,
    user_nome varchar(100) not null,
    user_email varchar(150) not null,
    user_senha varchar(200) not null,
    user_data_de_nascimento date,
    user_sexo varchar(20),
    user_peso decimal(5,2),
    user_altura decimal(5,2),
    user_experiencia varchar(50),
    primary key (user_id)
);

create table meta (
    meta_id int not null auto_increment,
    meta_nome varchar(100),
    meta_distancia decimal(10,2),
    primary key (meta_id)
);

create table mu (
    mu_id int not null auto_increment,
    mu_meta_id int,
    mu_user_id int,
    mu_corrida_id int,
    primary key (mu_id)
);

create table corrida (
    corrida_id int not null auto_increment,
    corrida_data date,
    corrida_tempo time,
    corrida_ritmo decimal(5,2),
    corrida_kcal int,
    corrida_distancia decimal(10,2),
    corrida_tipo_id int,
    corrida_rota_id int,
    primary key (corrida_id)
);

create table post (
    post_id int not null auto_increment,
    post_corrida_id int,
    post_comentario text,
    primary key (post_id)
);

create table tipo (
    tipo_id int not null auto_increment,
    tipo_nome varchar(100),
    primary key (tipo_id)
);

create table rota (
    rota_id int not null auto_increment,
    rota_nome varchar(100),
    rota_elevacao decimal(10,2),
    primary key (rota_id)
);

create table cr (
    cr_id int not null auto_increment,
    cr_rota_id int,
    cr_caract_id int,
    primary key (cr_id)
);

create table caracteristica (
    caract_id int not null auto_increment,
    caract_tipo varchar(100),
    primary key (caract_id)
);

create table lr (
    lr_id int not null auto_increment,
    lr_rota_id int,
    lr_local_id int,
    lr_ordem int,
    primary key (lr_id)
);

create table local (
    local_id int not null auto_increment,
    local_nome varchar(100),
    primary key (local_id)
);

-- Foreign Keys

alter table mu
add constraint mu_fk_meta
foreign key (mu_meta_id) references meta(meta_id)
on delete no action on update no action;

alter table mu
add constraint mu_fk_user
foreign key (mu_user_id) references usuario(user_id)
on delete no action on update no action;

alter table mu
add constraint mu_fk_corrida
foreign key (mu_corrida_id) references corrida(corrida_id)
on delete no action on update no action;

alter table corrida
add constraint corrida_fk_tipo
foreign key (corrida_tipo_id) references tipo(tipo_id)
on delete no action on update no action;

alter table corrida
add constraint corrida_fk_rota
foreign key (corrida_rota_id) references rota(rota_id)
on delete no action on update no action;

alter table post
add constraint post_fk_corrida
foreign key (post_corrida_id) references corrida(corrida_id);


alter table cr
add constraint cr_fk_rota
foreign key (cr_rota_id) references rota(rota_id)
on delete no action on update no action;

alter table cr
add constraint cr_fk_caract
foreign key (cr_caract_id) references caracteristica(caract_id)
on delete no action on update no action;

alter table lr
add constraint lr_fk_rota
foreign key (lr_rota_id) references rota(rota_id)
on delete no action on update no action;

alter table lr
add constraint lr_fk_local
foreign key (lr_local_id) references local(local_id)
on delete no action on update no action;

/****************** DOMAIN ******************/
create table numerador(
    id bigint not null,
    keynumerador varchar(256) not null,
    numeroactual bigint,
primary key(id));

create table tipocontacto (
    id bigint not null,
    descripcion varchar(64) not null,
primary key(id));

create table persona (
    id bigint not null,
primary key(id));

create table keywordpersona(
    id bigint not null,
    keyword varchar(128) not null,
    persona_id bigint not null,
    foreign key(persona_id) references persona(id),
primary key(id));
create index idx_keywordpersona_persona_id on keywordpersona (persona_id);

create table personafisica(
    id bigint not null,
    nombre varchar(64) not null,
    apellido varchar(64) not null,
    fechanacimiento date not null,
    sexo int not null,
    foreign key (id) references persona(id),
primary key(id));
create index idx_personafisica_id on personafisica (id);

create table contactopersona (
    id bigint not null,
    tipocontacto_id bigint not null,
    descripcion varchar(128),
    valor varchar(128) not null,
    persona_id bigint not null,
    foreign key(tipocontacto_id) references tipocontacto(id),
    foreign key(persona_id) references persona(id),
primary key(id));
create index idx_contactopersona_tipocontacto_id on contactopersona (tipocontacto_id);
create index idx_contactopersona_persona_id on contactopersona (persona_id);

create table institucion (
    id bigint not null,
    nombre varchar(64),
    foreign key(id) references persona(id),
primary key(id));
create index idx_institucion_id on institucion (id);

create table keywordinstitucion (
    id bigint not null,
    nombre varchar(128) not null,
    institucion_id bigint not null,
    foreign key (institucion_id) references institucion(id),
primary key(id));
create index idx_keywordinstitucion_institucion_id on keywordinstitucion (institucion_id);

create table profesor (
    id bigint not null,
    personafisica_id bigint not null,
    institucion_id bigint not null,
    foreign key (personafisica_id) references personafisica(id),
    foreign key (institucion_id) references institucion(id),
primary key(id));
create index idx_profesor_personafisica_id on profesor (personafisica_id);
create index idx_profesor_institucion_id on profesor (institucion_id);

create table alumno(
    id bigint not null,
    personafisica_id bigint not null,
    institucion_id bigint not null,
    foreign key (personafisica_id) references personafisica(id),
    foreign key (institucion_id) references institucion(id),
primary key(id));
create index idx_alumno_personafisica_id on alumno (personafisica_id);
create index idx_alumno_institucion_id on alumno(institucion_id);

/****************** SECURITY ******************/
create table funcionalidad(
    id bigint not null,
    descripcion varchar(64) not null,
primary key(id));

create table rol(
    id bigint not null,
    descripcion varchar(64) not null,
primary key(id));

create table funcionalidadrol(
    id bigint not null,
    funcionalidad_id bigint not null,
    rol_id bigint not null,
    alta boolean not null,
    baja boolean not null,
    modificacion boolean not null,
    consulta boolean not null,
    foreign key (funcionalidad_id) references funcionalidad(id),
    foreign key (rol_id) references rol(id),
primary key(id));
create index idx_funcionalidadrol_funcionalidad_id on funcionalidadrol (funcionalidad_id);
create index idx_funcionalidadrol_rol_id on funcionalidadrol (rol_id);

create table usuario(
    id bigint not null,
    personafisica_id bigint not null,
    username varchar(64) not null,
    password varchar(15) not null,
    vigencia_id bigint not null,
    rol_id bigint not null,
    foreign key (personafisica_id) references personafisica(id),
    foreign key (vigencia_id) references vigencia(id),
    foreign key (rol_id) references rol(id),
primary key(id));
create index idx_usuario_personafisica_id on usuario (personafisica_id);
create index idx_usuario_vigencia_id on usuario (vigencia_id);
create index idx_usuario_rol_id on usuario (rol_id);

create table keywordusuario(
    id bigint not null,
    keyword varchar(128) not null,
    usuario_id bigint not null,
    foreign key(usuario_id) references usuario(id),
primary key(id));
create index idx_keywordusuario_usuario_id on keywordusuario (usuario_id);

create table funcionalidadusuario(
    id bigint not null,
    funcionalidad_id bigint not null,
    usuario_id bigint not null,
    alta boolean not null,
    baja boolean not null,
    modificacion boolean not null,
    consulta boolean not null,
    foreign key (funcionalidad_id) references funcionalidad(id),
    foreign key (usuario_id) references usuario(id),
primary key(id));
create index idx_funcionalidadusuario_usuario_id on funcionalidadusuario (usuario_id);
create index idx_funcionalidadusuario_funcionalidad_id on funcionalidadusuario (funcionalidad_id);
/****************** TORNEO ******************/
create table estadotorneo(
    id bigint not null,
    fechadesde timestamp not null,
    tipoestadotorneo int not null,
    torneo_id bigint null,
primary key(id));
create index idx_estadotorneo_id on estadotorneo(id);
create index idx_torneo_id on estadotorneo(torneo_id);

create table torneo (
    id bigint not null,
    nombre varchar(64),
    fechadesde timestamp not null,
    estadotorneo_id bigint not null,
    foreign key(estadotorneo_id) references estadotorneo(id),
primary key(id));
create index idx_torneo_estadotorneo_id on torneo (estadotorneo_id);

alter table estadotorneo add constraint foreign key(torneo_id) references torneo(id);

create table cinturon(
    id bigint not null,
    descripcion varchar(64) not null,
primary key(id));

create table categoria(
    id bigint not null,
    descripcion varchar(64) not null,
    edadinferior int not null,
    edadsuperior int not null,
primary key(id));

create table categoriaforma(
    id bigint not null,
    foreign key(id) references categoria(id),
primary key(id));
create index idx_categoriaforma_id on categoriaforma (id);

create table categorialucha(
    id bigint not null,
    sexo int not null,
    foreign key(id) references categoria(id),
primary key(id));
create index idx_categorialucha_id on categorialucha (id);

create table peso(
    id bigint not null,
    categorialucha_id bigint not null,
    pesoinferior int not null,
    pesosuperior int not null,
    foreign key (categorialucha_id) references categorialucha(id),
primary key(id));
create index idx_peso_categorialucha_id on peso(categorialucha_id);

create table estadocompetidor(
    id bigint not null,
    fechadesde timestamp not null,
    tipoestadocompetidor int not null,
    competidorcategoria_id bigint null,
primary key(id));
create index idx_estadocompetidor_id on estadocompetidor (id);

create table torneoinstitucion(
    id bigint not null,
    torneo_id bigint not null,
    institucion_id bigint not null,
    foreign key(torneo_id) references torneo(id),
    foreign key(institucion_id) references institucion(id),
primary key(id));
create index idx_torneoinstitucion_institucion_id on torneoinstitucion (institucion_id);
create index idx_torneoinstitucion_torneo_id on torneoinstitucion(torneo_id);

create table competidorcategoria(
    id bigint not null,
    estadocompetidor_id bigint not null,
    foreign key(estadocompetidor_id) references estadocompetidor(id),
primary key(id));
create index idx_competidorcategoria_estadocompetidor_id on competidorcategoria(estadocompetidor_id);

alter table estadocompetidor add constraint estadocompetidor_competidorcategoria_id_fkey
foreign key(competidorcategoria_id) references competidorcategoria(id);


create table competidorcategoriaforma(
    id bigint not null,
    categoriaforma_id bigint not null,
    foreign key(id) references competidorcategoria(id),
    foreign key(categoriaforma_id) references categoriaforma(id),
primary key(id));
create index idx_competidorcategoriaforma_categoriaforma_id on competidorcategoriaforma(categoriaforma_id);
create index idx_competidorcategoriaforma_id on competidorcategoriaforma(id);

create table competidorcategorialucha(
    id bigint not null,
    peso_id bigint not null,
    foreign key(id) references competidorcategoria(id),
    foreign key(peso_id) references peso(id),
primary key(id));
create index idx_competidorcategorialucha_peso_id on competidorcategorialucha(peso_id);
create index idx_competidorcategorialucha_id on competidorcategorialucha(id);

create table competidor (
    id bigint not null,
    torneoinstitucion_id bigint not null,
    alumno_id bigint not null,
    competidorcategorialucha_id bigint null,
    competidorcategoriaforma_id bigint null,
    cinturon_id bigint not null,
    foreign key(torneoinstitucion_id) references torneoinstitucion(id),
    foreign key(alumno_id) references alumno(id),
    foreign key(cinturon_id) references cinturon(id),
    foreign key(competidorcategorialucha_id) references competidorcategorialucha(id),
    foreign key(competidorcategoriaforma_id) references competidorcategoriaforma(id),
primary key (id));
create index idx_competidor_torneoinstitucion_id on competidor(torneoinstitucion_id);
create index idx_competidor_alumno_id on competidor(alumno_id);
create index idx_competidor_competidorcategorialucha_id on competidor(competidorcategorialucha_id);
create index idx_competidor_competidorcategoriaforma_id on competidor(competidorcategoriaforma_id);
create index idx_competidor_cinturon_id on competidor(cinturon_id);

/****************** COMPETENCIA ******************/
create table arbitro (
    id bigint not null,
    personafisica_id bigint not null,
    fechadesed timestamp not null,
    foreign key(personafisica_id) references personafisica(id),
primary key(id));
create index idx_arbitro_personafisica_id on arbitro(personafisica_id);

create table juez (
    id bigint not null,
    personafisica_id bigint not null,
    fechadesde timestamp not null,
    foreign key(personafisica_id) references personafisica(id),
    foreign key(vigencia_id) references vigencia(id),
primary key(id));
create index idx_juez_personafisica_id on juez(personafisica_id);

drop table competencia;
create table competencia(
    id bigint not null,
    numero bigint not null,
    competencialeft bigint null,
    competenciaright bigint null,
    competenciapadre bigint null,
    competidorrojo bigint null,
    competidorazul bigint null,
    competidorganador bigint null,
    estadoactual bigint,
    foreign key(competidorrojo) references competidor(id),
    foreign key(competidorazul) references competidor(id),
    foreign key(competidorganador) references competidor(id),
primary key(id));

alter table competencia add constraint competencia_competencialeft_fkey
foreign key(competencialeft) references competencia(id);

alter table competencia add constraint competencia_competenciaright_fkey
foreign key(competenciaright) references competencia(id);

alter table competencia add constraint competencia_competenciapadre_fkey
foreign key(competenciapadre) references competencia(id);

create index idx_competencia_competidorrojo on competencia(competidorrojo);
create index idx_competencia_competidorazul on competencia(competidorazul);
create index idx_competencia_competencialeft on competencia(competencialeft);
create index idx_competencia_competenciaright on competencia(competenciaright);
create index idx_competencia_competenciapadre on competencia(competenciapadre);

create table estadocompetencia(
    id bigint not null,
    fechadesde timestamp not null,
    tipoestadocompetencia int not null,
    competencia_id bigint null,
    foreign key(competencia_id) references competencia(id),
primary key(id));
create index idx_estadcompetencia_competencia_id_fkey on estadocompetencia(competencia_id);

alter table competencia add column estadoactual bigint;
alter table competencia add constraint competencia_estadoactual_fkey
foreign key(estadoactual) references estadocompetencia(id);

alter table competencia add column competidorganador bigint;
alter table competencia add constraint competencia_competidorganador_fkey
foreign key(competidorganador) references competidor(id);

create table llave(
    id bigint not null,
    torneo_id bigint not null,
    root bigint not null,
    cinturon_id bigint not null,
    foreign key(root) references competencia(id),
    foreign key(cinturon_id) references cinturon(id),
    foreign key(torneo_id) references torneo(id),
primary key(id));

/*drop table llavelucha;
drop table llaveforma;
drop table llave;
*/
create table llavelucha(
    id bigint not null,
    categorialucha bigint not null,
    foreign key(categorialucha) references categorialucha(id),
    foreign key(id) references llave(id),
primary key(id));

create table llaveforma(
    id bigint not null,
    categoriaforma bigint not null,
    foreign key(categoriaforma) references categoriaforma(id),
    foreign key(id) references llave(id),
primary key(id));


/****************** COMPETENCIA ******************/
/*create sequence hibernate_sequence start with 1;*/
CREATE SEQUENCE hibernate_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE hibernate_sequence OWNER TO postgres;
insert into persona (id) values (1);
insert into personafisica (id, nombre, apellido, fechanacimiento, sexo) 
values (1, 'Fernando', 'Cambarieri', now(), 1);
insert into keywordpersona (id, keyword, persona_id) values (14,'Cambarieri Fernando',1);

insert into rol(id, descripcion) values(2, 'admin');
insert into usuario (id, personafisica_id, username, password, fechadesde, rol_id)
values(4,1,'root','root',now(),2); 

insert into funcionalidad (id, descripcion) values (5, 'Usuarios');
insert into funcionalidad (id, descripcion) values (6, 'Funcionalidades');
insert into funcionalidad (id, descripcion) values (7, 'Roles');
insert into funcionalidad (id, descripcion) values (8, 'Permisos');


insert into funcionalidadrol (id, funcionalidad_id, rol_id, alta, baja, modificacion, consulta) 
values (9, 5, 2, true, true, true, true);
insert into funcionalidadrol (id, funcionalidad_id, rol_id, alta, baja, modificacion, consulta) 
values (10, 6, 2, true, true, true, true);
insert into funcionalidadrol (id, funcionalidad_id, rol_id, alta, baja, modificacion, consulta) 
values (11, 7, 2, true, true, true, true);
insert into funcionalidadrol (id, funcionalidad_id, rol_id, alta, baja, modificacion, consulta) 
values (12, 8, 2, true, true, true, true);

insert into keywordusuario (id, keyword, usuario_id) values (13,'root',4);

insert into tipocontacto  (id, descripcion) values (15,'CEL');
insert into tipocontacto  (id, descripcion) values (16,'Telefono');
insert into tipocontacto  (id, descripcion) values (17,'Mail');
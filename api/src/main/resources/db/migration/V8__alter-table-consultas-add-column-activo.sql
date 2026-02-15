alter table consultas add activo tinyint;
update consultas set activo = 1;
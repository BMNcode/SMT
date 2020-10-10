CREATE TABLE IF NOT EXISTS Public.shape (
	id_shape serial8 not null,
  	shape_name varchar(255) not null,
	width real,
	height real,
	set_stroke varchar(255),
	set_fill varchar(255),
  	PRIMARY KEY (id_shape)
) WITHOUT OIDS;

CREATE TABLE IF NOT EXISTS Public.shape_nickname (
	id_shape_nickname serial8 not null,
	id_shape int8 not null references shape(id_shape),
	shape_nickname varchar(255) not null,
	PRIMARY KEY (id_shape_nickname)
) WITHOUT OIDS;

CREATE TABLE IF NOT EXISTS Public.components (
	id_component serial8 not null,
	id_shape_nickname int8 not null references shape_nickname(id_shape_nickname),
	part_name varchar(255) not null,
	PRIMARY KEY (id_component)
) WITHOUT OIDS;


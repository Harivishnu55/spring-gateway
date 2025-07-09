
CREATE TABLE public.tbl_user (
	id int8 NOT NULL,
	"password" varchar(255) NULL,
	username varchar(255) NULL,
	CONSTRAINT tbl_user_pkey PRIMARY KEY (id),
	CONSTRAINT uk_username UNIQUE (username)
);
drop table if exists link;

CREATE TABLE "link" (
	"id"	BIGSERIAl PRIMARY KEY,
	"url" TEXT NOT NULL UNIQUE,
	"creation_date" timestamp NOT NULL default now()/*,
	"hash" CHAR NOT NULL UNIQUE, //MD5
	"last_access" timestamp,
	"ttl" smallint default 365,
	"subdomain" varchar(2)*/

);

CREATE INDEX idx_link_creation_date ON "link" (
	creation_date desc
);






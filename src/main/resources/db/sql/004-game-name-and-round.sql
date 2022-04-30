alter table cards.game add column name text not null default 'Дефолтная игра';
alter table cards.game add column round integer not null default 0;
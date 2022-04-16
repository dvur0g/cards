create table cards.suggested_question (
    id bigint not null primary key,
    text varchar(255) not null unique,
    username text not null
        constraint player_suggested_question
            references cards.player (username),
    date timestamp not null,
    deleted bool not null default false
);

create table cards.suggested_answer (
    id bigint not null primary key,
    text varchar(255) not null unique,
    username text not null
      constraint player_suggested_question
          references cards.player (username),
    date timestamp not null,
    deleted bool not null default false
);

insert into cards.player values (nextval('seq_player'), 0, 'admin', null);

alter table cards.question add column date timestamp not null default (current_timestamp);
alter table cards.question add column username text not null
    constraint player_question
        references cards.player (username)
    default ('admin');

alter table cards.card add column date timestamp not null default (current_timestamp);
alter table cards.card add column username text not null
    constraint player_question
        references cards.player (username)
    default ('admin');

create sequence cards.seq_suggested_question;
create sequence cards.seq_suggested_answer;
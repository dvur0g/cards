CREATE SCHEMA IF NOT EXISTS cards;

create table cards.card
(
    id   bigint       not null
        constraint card_pkey
            primary key,
    text varchar(255),
    type varchar(255) not null
);

create table cards.player
(
    id                 bigint  not null
        constraint player_pkey
            primary key,
    score              integer,
    username           varchar(255) unique,
    selected_answer_id bigint
        constraint player_selected_answer
            references cards.card
);

create table cards.player_cards
(
    player_id bigint not null
        constraint player_id_card
            references cards.player,
    cards_id  bigint not null
        constraint card_id_player
            references cards.card
);

create table cards.question
(
    id   bigint       not null
        constraint question_pkey
            primary key,
    text varchar(255),
    type varchar(255) not null
);

create table cards.game
(
    id                   bigint       not null
        constraint game_pkey
            primary key,
    current_player_index integer,
    min_players_amount   integer,
    state                varchar(255) not null,
    current_player_id    bigint
        constraint game_current_player
            references cards.player,
    current_question_id  bigint
        constraint game_current_question
            references cards.question,
    victorious_answer_id bigint
        constraint game_victorious_answer
            references cards.card,
    victorious_player_id bigint
        constraint game_victorious_player
            references cards.player,
    winner_id            bigint
        constraint game_winner
            references cards.player
);

create table cards.game_players
(
    game_id    bigint not null
        constraint game_id_player
            references cards.game,
    players_id bigint not null unique
        constraint player_id_game
            references cards.player
);

create sequence cards.seq_card;
create sequence cards.seq_game;
create sequence cards.seq_player;
create sequence cards.seq_question;

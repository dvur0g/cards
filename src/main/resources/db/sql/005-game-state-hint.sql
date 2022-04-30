create table game_state_hint (
    game_state text primary key not null,
    hint text not null
);

insert into game_state_hint values ('CREATED', 'Ожидание подключения игроков');
insert into game_state_hint values ('PROCESSING', 'Ожидайте...');
insert into game_state_hint values ('SELECTING_ANSWERS', 'Игроки выбирают карты');
insert into game_state_hint values ('SELECTING_VICTORIOUS_ANSWER', 'Ведущий выбирает карту');
insert into game_state_hint values ('SHOW_VICTORIOUS_ANSWER', 'Раунд выигрывает ____.');
insert into game_state_hint values ('CANCELLED', 'Игра отменена.');
insert into game_state_hint values ('FINISHED', 'Игра окончена. Победил ____.');


TRUNCATE TABLE cards.game_players;
TRUNCATE TABLE cards.game;
TRUNCATE TABLE cards.player_cards;
TRUNCATE TABLE  cards.player;

ALTER SEQUENCE cards.seq_game RESTART;
ALTER SEQUENCE cards.seq_player RESTART;
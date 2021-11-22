DELETE FROM cards.game_players;
DELETE FROM cards.game;
DELETE FROM cards.player_cards;
DELETE FROM cards.player;

ALTER SEQUENCE cards.seq_game RESTART;
ALTER SEQUENCE cards.seq_player RESTART;
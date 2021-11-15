package com.chess.engine;

import com.chess.engine.user.BlackPlayer;
import com.chess.engine.user.Player;
import com.chess.engine.user.WhitePlayer;

public enum Color {
    WHITE{
        public int getDirection(){
            return -1;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,
                                   final BlackPlayer blackPlayer) {
            return whitePlayer;
        }

        @Override
        public int getFacingDirection() {
            return DOWN_ORIENTAL;
        }
    },
    BLACK{
        public int getDirection(){
            return 1;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,
                                   final BlackPlayer blackPlayer) {
            return blackPlayer;
        }

        @Override
        public int getFacingDirection() {
            return UP_ORIENTAL;
        }
    };

    public abstract int getDirection();
    public abstract boolean isWhite();
    public abstract boolean isBlack();

    public abstract Player choosePlayer(final WhitePlayer whitePlayer,
                                        final BlackPlayer blackPlayer);

    public abstract int getFacingDirection();

    private static final int UP_ORIENTAL = -1;

    private static final int DOWN_ORIENTAL = 1;
}

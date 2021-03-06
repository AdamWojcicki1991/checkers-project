package com.checkers.engine.move;

import java.util.Objects;

public abstract class Move {
    public final int initialRow, initialColumn;
    public final int destinationRow, destinationColumn;

    public Move(final int initialRow, final int initialColumn, final int destinationRow, final int destinationColumn) {
        this.initialRow = initialRow;
        this.initialColumn = initialColumn;
        this.destinationRow = destinationRow;
        this.destinationColumn = destinationColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Move move = (Move) o;

        return Objects.equals(initialRow, move.initialRow) &&
                Objects.equals(initialColumn, move.initialColumn) &&
                Objects.equals(destinationRow, move.destinationRow) &&
                Objects.equals(destinationColumn, move.destinationColumn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initialRow, initialColumn, destinationRow, destinationColumn);
    }

    public boolean isPawnMove() {
        return (initialRow - destinationRow == 1 || initialRow - destinationRow == -1);
    }

    abstract public int getEnemyDestinationRow();

    abstract public int getEnemyDestinationColumn();

    abstract public boolean isQueenAttackMove();

    abstract public boolean isPawnAttackMove();

    abstract public boolean isQueenMajorMove();

    abstract public boolean isPawnMajorMove();

    public enum MoveStatus {
        DONE {
            @Override
            public boolean isDone() {
                return true;
            }
        },
        ILLEGAL_MOVE {
            @Override
            public boolean isDone() {
                return false;
            }
        };

        public abstract boolean isDone();
    }

    public static class QueenAttackMove extends Move {
        private final int enemyDestinationRow;
        private final int enemyDestinationColumn;

        public QueenAttackMove(final int initialRow, final int initialColumn, final int destinationRow, final int destinationColumn, final int enemyDestinationRow, final int enemyDestinationColumn) {
            super(initialRow, initialColumn, destinationRow, destinationColumn);
            this.enemyDestinationRow = enemyDestinationRow;
            this.enemyDestinationColumn = enemyDestinationColumn;
        }

        @Override
        public int getEnemyDestinationRow() {
            return this.enemyDestinationRow;
        }

        @Override
        public int getEnemyDestinationColumn() {
            return this.enemyDestinationColumn;
        }

        @Override
        public boolean isQueenAttackMove() {
            return true;
        }

        @Override
        public boolean isPawnAttackMove() {
            return false;
        }

        @Override
        public boolean isQueenMajorMove() {
            return false;
        }

        @Override
        public boolean isPawnMajorMove() {
            return false;
        }

        @Override
        public String toString() {
            return "QueenAttackMove{" +
                    "initialRow=" + initialRow +
                    ", initialColumn=" + initialColumn +
                    ", destinationRow=" + destinationRow +
                    ", destinationColumn=" + destinationColumn +
                    ", enemyDestinationRow=" + enemyDestinationRow +
                    ", enemyDestinationColumn=" + enemyDestinationColumn +
                    '}';
        }
    }

    public static class PawnAttackMove extends Move {
        private final int enemyDestinationRow;
        private final int enemyDestinationColumn;

        public PawnAttackMove(final int initialRow, final int initialColumn, final int destinationRow, final int destinationColumn, final int enemyDestinationRow, final int enemyDestinationColumn) {
            super(initialRow, initialColumn, destinationRow, destinationColumn);
            this.enemyDestinationRow = enemyDestinationRow;
            this.enemyDestinationColumn = enemyDestinationColumn;
        }

        @Override
        public int getEnemyDestinationRow() {
            return this.enemyDestinationRow;
        }

        @Override
        public int getEnemyDestinationColumn() {
            return this.enemyDestinationColumn;
        }

        @Override
        public boolean isQueenAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnAttackMove() {
            return true;
        }

        @Override
        public boolean isQueenMajorMove() {
            return false;
        }

        @Override
        public boolean isPawnMajorMove() {
            return false;
        }

        @Override
        public String toString() {
            return "PawnAttackMove{" +
                    "initialRow=" + initialRow +
                    ", initialColumn=" + initialColumn +
                    ", destinationRow=" + destinationRow +
                    ", destinationColumn=" + destinationColumn +
                    ", enemyDestinationRow=" + enemyDestinationRow +
                    ", enemyDestinationColumn=" + enemyDestinationColumn +
                    '}';
        }
    }

    public static class QueenMajorMove extends Move {

        public QueenMajorMove(final int initialRow, final int initialColumn, final int destinationRow, final int destinationColumn) {
            super(initialRow, initialColumn, destinationRow, destinationColumn);
        }

        @Override
        public int getEnemyDestinationRow() {
            return -1;
        }

        @Override
        public int getEnemyDestinationColumn() {
            return -1;
        }

        @Override
        public boolean isQueenAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnAttackMove() {
            return false;
        }

        @Override
        public boolean isQueenMajorMove() {
            return true;
        }

        @Override
        public boolean isPawnMajorMove() {
            return false;
        }

        @Override
        public String toString() {
            return "QueenMajorMove{" +
                    "initialRow=" + initialRow +
                    ", initialColumn=" + initialColumn +
                    ", destinationRow=" + destinationRow +
                    ", destinationColumn=" + destinationColumn +
                    '}';
        }
    }

    public static class PawnMajorMove extends Move {

        public PawnMajorMove(final int initialRow, final int initialColumn, final int destinationRow, final int destinationColumn) {
            super(initialRow, initialColumn, destinationRow, destinationColumn);
        }

        @Override
        public int getEnemyDestinationRow() {
            return -1;
        }

        @Override
        public int getEnemyDestinationColumn() {
            return -1;
        }

        @Override
        public boolean isQueenAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnAttackMove() {
            return false;
        }

        @Override
        public boolean isQueenMajorMove() {
            return false;
        }

        @Override
        public boolean isPawnMajorMove() {
            return true;
        }

        @Override
        public String toString() {
            return "PawnMajorMove{" +
                    "initialRow=" + initialRow +
                    ", initialColumn=" + initialColumn +
                    ", destinationRow=" + destinationRow +
                    ", destinationColumn=" + destinationColumn +
                    '}';
        }
    }
}

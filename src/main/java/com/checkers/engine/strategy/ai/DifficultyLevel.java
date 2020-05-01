package com.checkers.engine.strategy.ai;

public enum DifficultyLevel {
    EASY {
        @Override
        public boolean isEasy() {
            return true;
        }

        @Override
        public boolean isMedium() {
            return false;
        }

        @Override
        public boolean isHard() {
            return false;
        }
    },
    MEDIUM {
        @Override
        public boolean isEasy() {
            return false;
        }

        @Override
        public boolean isMedium() {
            return true;
        }

        @Override
        public boolean isHard() {
            return false;
        }
    },
    HARD {
        @Override
        public boolean isEasy() {
            return false;
        }

        @Override
        public boolean isMedium() {
            return false;
        }

        @Override
        public boolean isHard() {
            return true;
        }
    };

    public abstract boolean isEasy();

    public abstract boolean isMedium();

    public abstract boolean isHard();
}

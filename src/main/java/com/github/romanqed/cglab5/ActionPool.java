package com.github.romanqed.cglab5;

import java.util.LinkedList;
import java.util.List;

public class ActionPool {
    private final int limit;
    private final List<Action> actions;
    private final List<Action> redos;

    public ActionPool(int limit) {
        this.actions = new LinkedList<>();
        this.redos = new LinkedList<>();
        this.limit = limit;
    }

    public ActionPool() {
        this(10);
    }

    public void add(Action action) {
        if (actions.size() >= limit) {
            actions.clear();
            redos.clear();
        }
        action.perform();
        actions.add(action);
    }

    public void undo() {
        if (actions.isEmpty()) {
            return;
        }
        Action action = actions.remove(actions.size() - 1);
        redos.add(action);
        action.undo();
    }

    public void redo() {
        if (redos.isEmpty()) {
            return;
        }
        Action action = redos.remove(redos.size() - 1);
        add(action);
    }

    public void clear() {
        this.actions.clear();
        this.redos.clear();
    }
}

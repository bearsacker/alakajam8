package com.guillot.engine.utils;

import java.util.LinkedList;

public class LinkedNonBlockingQueue<E> extends LinkedList<E> {

    private static final long serialVersionUID = 2352128506263657615L;

    private int maxSize;

    public LinkedNonBlockingQueue(int maxSize) {
        super();

        if (maxSize <= 0) {
            throw new IllegalStateException("Size must be greater than 0");
        }

        this.maxSize = maxSize;
    }

    @Override
    public boolean add(E e) {
        if (size() >= maxSize) {
            super.removeFirst();
        }

        return super.add(e);
    }

    @Override
    public void push(E e) {
        if (size() >= maxSize) {
            super.removeLast();
        }

        super.push(e);
    }
}

package com.example.tetrisgame.presenters;

public interface PresenterObserver<T> {
    void observe(T t);
}
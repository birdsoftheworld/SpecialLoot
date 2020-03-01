package com.github.birdsoftheworld.specialloot.util;

public class SpecialtyProperty<T> {
    private T value;

    public SpecialtyProperty() {}
    public SpecialtyProperty(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public SpecialtyProperty clone() {
        return new SpecialtyProperty(value);
    }
}

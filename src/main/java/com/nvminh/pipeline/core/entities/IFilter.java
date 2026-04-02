package com.nvminh.pipeline.core.entities;

public interface IFilter<T> {

    T filter(T message);
}

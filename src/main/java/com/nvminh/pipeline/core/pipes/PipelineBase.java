package com.nvminh.pipeline.core.pipes;

import com.nvminh.pipeline.core.entities.IFilter;
import java.util.ArrayList;
import java.util.List;

public abstract class PipelineBase<T> {

    protected final List<IFilter<T>> filters = new ArrayList<>();

    public PipelineBase<T> addFilter(IFilter<T> filter) {
        filters.add(filter);
        return this;
    }

    public T execute(T message) {
        T currentMessage = message;
        for (IFilter<T> filter : filters) {
            currentMessage = filter.filter(currentMessage);
        }
        return currentMessage;
    }
}

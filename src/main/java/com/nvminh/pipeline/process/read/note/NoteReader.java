package com.nvminh.pipeline.process.read.note;

import com.nvminh.pipeline.core.entities.IFilter;
import com.nvminh.pipeline.core.entities.Message;

public class NoteReader implements IFilter<Message> {

    @Override
    public Message filter(Message message) {
        return message;
    }
}

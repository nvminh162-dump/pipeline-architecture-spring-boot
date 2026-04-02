package com.nvminh.pipeline.process.read.note;

import com.nvminh.pipeline.core.entities.IFilter;
import com.nvminh.pipeline.core.entities.Message;
import com.nvminh.pipeline.core.entities.Note;

import java.util.List;

public class NoteReader implements IFilter<Message> {

    @Override
    public Message filter(Message message) {
        System.out.println("[NoteReader] Processing notes from: " + message.getRawData());

        List<Note> notes = List.of(
                new Note("Invoice verified"),
                new Note("Payment matched")
        );

        message.getInvoiceInfo().setNotes(notes);

        System.out.println("[NoteReader] Found " + notes.size() + " notes");
        return message;
    }
}

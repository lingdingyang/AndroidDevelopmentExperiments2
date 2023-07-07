package com.ldy.ex2.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.ldy.ex2.Activities.AddActivity;
import com.ldy.ex2.POJO.Content;
import com.ldy.ex2.POJO.ParagraphContent;

import java.util.List;

public class EditTextWatcher implements TextWatcher {
    private EditText editText;
    private AddActivity addActivity;

    public EditTextWatcher(EditText editText, AddActivity addActivity) {
        this.editText = editText;
        this.addActivity = addActivity;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String s = editText.getText().toString();
        Content content = (Content) editText.getTag();
        List<Content> contents = addActivity.getContents();
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).equals(content)) {
                ((ParagraphContent) contents.get(i)).setParagraph(s);
            }
        }
    }
}

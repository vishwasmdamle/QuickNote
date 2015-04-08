package com.example.vishwasdamle.quicknote.service;

import android.content.Context;

import com.example.vishwasdamle.quicknote.repository.AutoCompleteMapper;

import java.util.ArrayList;

/**
 * Created by vishwasdamle on 07/04/15.
 */
public class AutoCompleteService {
    private Context context;
    private AutoCompleteMapper autoCompleteMapper;
    static boolean enabled = true;

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean value) {
        AutoCompleteService.enabled = value;
    }

    public AutoCompleteService(Context context) {
        this.context = context;
        autoCompleteMapper = new AutoCompleteMapper(context);
    }


    public void add(String wordList) {
        String[] words = wordList.split("\\s*,\\s*|\\s+");
        autoCompleteMapper.update(words);
        autoCompleteMapper.listAll();
    }

    public ArrayList<String> listAll() {
        return autoCompleteMapper.listAll();
    }
}

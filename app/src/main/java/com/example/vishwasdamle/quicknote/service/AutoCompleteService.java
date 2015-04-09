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

    public void deleteAll() {
        autoCompleteMapper.deleteAll();
    }
}

package com.uki121.pooni;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;


public class SettingBook extends AppCompatActivity {
    final int MAX_BOOKS = 100;
    private List < Book > books;
    private int numOfbooks = 0;

    public SettingBook() { books = new ArrayList<>();}
    public SettingBook(String[] _onebook) {
        books = new ArrayList<>();
        books.add(new Book(_onebook));
        numOfbooks++;
    }
    public void AddBooks(Book bs) {
        try {
            if (books.size() > MAX_BOOKS) {
                Log.d("Out of books", "Books too much now");
                return;
            }
            if (IsDupBooks(bs)) {
                books.add(new Book(bs));
                numOfbooks++;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(">> Out of Books size");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return;
    }
    /* Todo : saving multi-books*/
    public void AddBooks(Book[] bs) {
        for (int i = 0; i < bs.length; ++i) {
            AddBooks(bs[i]);
        }
    }
    public void AddBooks(String[] _bsData) {
        Book b = new Book(_bsData);
        AddBooks(b);
    }
    public void AddBooks(View view) {
        try {
            if (view != null) {
                String[] dia = {((EditText) view.findViewById(R.id.setting_name)).getText().toString(),
                        ((EditText) view.findViewById(R.id.setting_totime)).getText().toString(),
                        ((EditText) view.findViewById(R.id.setting_maxtime)).getText().toString(),
                        ((EditText) view.findViewById(R.id.setting_count)).getText().toString(),
                        ((EditText) view.findViewById(R.id.setting_rest)).getText().toString()};
                AddBooks(dia);
            }
        } catch(Exception e) {
            Log.d("AddBook_fail from ", "View is null!!!");
        } catch(ExceptionInInitializerError exception) {
            Log.d("Initialziing_variable", exception.getMessage());
        }
    }
    public boolean IsDupBooks(Book bs) {
        return IsDupName(bs.category[0]);
    }

    private boolean IsDupName(String _bTitle) {
        for (Book b : books) {
            if (b.getBook().equals(_bTitle) == true) {
                return false;
            }
        }
        return true;
    }
    public void printBooks() {
        try {
            if (books.size() == 0) {
                Log.d("book_size", " 0 ");
            }
            for (Book b : books) {
                b.getBook();
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }
}

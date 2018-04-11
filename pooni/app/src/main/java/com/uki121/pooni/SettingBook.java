package com.uki121.pooni;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Pattern;


public class SettingBook extends AppCompatActivity {
    private Book[] books;
    private int numOfbooks = 0;

    public SettingBook() {
    }

    public SettingBook(String[] _onebook) {
        books[numOfbooks++] = new Book(_onebook);
    }

    public void AddBooks(Book bs) {
        try {
            if (books.length > 100) {
                Log.d("Out of books", "Books too much now");
                return;
            }
            if (IsDupBooks(bs)) {
                books[numOfbooks++] = new Book(bs);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(">> Out of Books size");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return;
    }

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
        if ( view != null) {
            String[] dia = {((EditText) view.findViewById(R.id.setting_name)).getText().toString(),
                    ((EditText) view.findViewById(R.id.setting_totime)).getText().toString(),
                    ((EditText) view.findViewById(R.id.setting_maxtime)).getText().toString(),
                    ((EditText) view.findViewById(R.id.setting_count)).getText().toString(),
                    ((EditText) view.findViewById(R.id.setting_rest)).getText().toString()};
            System.out.println(dia);
            AddBooks(dia);
        }
        Log.d("AddBook_fail","View is null!!!");
    }
    public boolean IsDupBooks(Book bs) {
        return IsDupName(bs.category[0]);
    }

    private boolean IsDupName(String _name) {
        for (int i = 0; i < numOfbooks; ++i) {
            if (books[numOfbooks].category[0].equals(_name) == true) {
                //Duplication occurs
                return false;
            }
        }
        return true;
    }
    public void printBooks() {
        try {
            for (int i = 0; i < books.length; ++i) {
                Log.d("문제집이름", books[i].category[0]);
                Log.d("총 풀이시간", books[i].category[1]);
                Log.d("문제당 시간", books[i].category[2]);
                Log.d("휴식시간", books[i].category[3]);
                Log.d("문제개수", books[i].category[4]);
            }
        } catch (NullPointerException e)
        {
            System.out.println(e.getMessage());
        }
    }
}

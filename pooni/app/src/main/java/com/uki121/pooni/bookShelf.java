package com.uki121.pooni;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class bookShelf extends AppCompatActivity {
    //def
    private final String TAG = "bookshelf";
    final int MAX_BOOKS = 100;
    private List < Book > books;
    private int numOfbooks = 0;

    public bookShelf() { books = new ArrayList<>();}
    public bookShelf(String[] _onebook) {
        books = new ArrayList<>();
        books.add(new Book(_onebook));
        numOfbooks++;
    }
    //Add book by Book
    public boolean AddBooks(Book bs) {
        try {
            if (IsDupBooks(bs)) {
                Log.w(TAG, "AddBooks failed : cause by duplication");
                return false;
            } else {
              Log.d(TAG, "AddBooks success: size and dup passed");
            }
            if (bs.IsBookValid()) {
                Log.i("Book_Valid"," passed");
                books.add(bs);
                numOfbooks++;
                return true;
            } else {
                Log.w(TAG, " valid failed");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e("ADD_BBOK_ARR", e.getMessage());
        } catch (Exception e) {
            Log.e("ADD_BOOK_EXC", e.getMessage());
        }
        return false;
    }
    //Add book by Book[]
    public void AddBooks(Book[] bs) {
        for (int i = 0; i < bs.length; ++i) {
            AddBooks(bs[i]);
        }
    }
    //Add book by String[]
    public void AddBooks(String[] _bsData) {
        Book b = new Book(_bsData)  ;
        AddBooks(b);
    }
    //Check whether new books is duplicated
    public boolean IsDupBooks(Book bs) {
        return IsDupName(bs.getTitle());
    }
    //Check whether newtitle is duplicated
    private boolean IsDupName(String _bTitle) {
        for (Book b : books) {
            if (b.getTitle().equals(_bTitle) == true) {
                Log.d(TAG, "This book is duplicated");
                return true;
            }
        }
        return false;
    }
    //Get the number of books
    final public int getNumOfBooks() {
        return books.size();
    }
    //Print and log this bookShelf info
    public void printBooks() {
        try {
            if (books.size() == 0) {
                Log.w("book_size", " 0 ");
            }
            Log.i("BOOK_COUNT", String.valueOf(getNumOfBooks()));
            for (Book b : books) {
                b.getBook();
            }
        } catch (NullPointerException e) {
            Log.e("BOOK_LIST", e.getMessage());
        }
    }
    //Get index 'i' of bookshelf
    public Book getBook(int _index) {
        try {
            if (_index >= numOfbooks) {
                Log.w("Book Shelf","Empty");
                throw new Exception();
            }
            Book target = books.get(_index);
            return target;
        } catch(Exception e) {
            Log.e("Book_getBook", e.getMessage());
        }
        return null;
    }
}

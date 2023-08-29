package com.tuan1611pupu.appbansach.model;

import java.util.ArrayList;
import java.util.List;

public class AuthorList {
    private static List<Author> authorList = new ArrayList<>();

    public static List<Author> getAuthorList() {
        return authorList;
    }

    public static void addAuthor(Author author) {
        authorList.add(author);
    }

    public static Author getAuthorById(int authorId) {
        for (Author author : authorList) {
            if (author.getAuthorId() == authorId) {
                return author;
            }
        }
        return null;
    }
}

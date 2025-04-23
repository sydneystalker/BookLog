package com.cst338.booklog.database;



@Dao
public interface BookDAO {
    @Insert
    void insert(Book book);

    @Update
    void update(Book book);

    @Delete
    void delete(Book book);

    @Query("SELECT * FROM " + BookLogDatabase.BOOK_TABLE + " ORDER by author")
    LiveData<List<Book>> getAllBooks();

//    @Query("SELECT * FROM " + BookLogDatabase.BOOK_TABLE + " WHERE genre == :bookGenre")
//    LiveData<List<Book>> getBooksByGenre(String bookGenre);

    @Query("SELECT * FROM " + BookLogDatabase.BOOK_TABLE + " WHERE title == :bookTitle")
    LiveData<List<Book>> getBookByTitle(String bookTitle);

    @Query("SELECT * FROM " + BookLogDatabase.BOOK_TABLE + " WHERE title == :bookId ")
    LiveData<List<Book>> getBookById(String bookId);

//    @Query("SELECT * FROM " + BookLogDatabase.BOOK_TABLE + " WHERE title == :bookAuthor")
//    LiveData<List<Book>> getBooksByAuthor(String bookAuthor);
//


}
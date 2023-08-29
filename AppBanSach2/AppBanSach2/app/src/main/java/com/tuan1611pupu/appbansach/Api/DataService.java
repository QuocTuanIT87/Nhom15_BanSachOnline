package com.tuan1611pupu.appbansach.Api;

import com.azure.core.annotation.Delete;
import com.tuan1611pupu.appbansach.model.Author;
import com.tuan1611pupu.appbansach.model.Book;
import com.tuan1611pupu.appbansach.model.BookAdd;
import com.tuan1611pupu.appbansach.model.BookInfo;
import com.tuan1611pupu.appbansach.model.CartItem;
import com.tuan1611pupu.appbansach.model.Category;
import com.tuan1611pupu.appbansach.model.Invoice;
import com.tuan1611pupu.appbansach.model.InvoiceDetail;
import com.tuan1611pupu.appbansach.model.LoginRequest;
import com.tuan1611pupu.appbansach.model.Order;
import com.tuan1611pupu.appbansach.model.Publisher;
import com.tuan1611pupu.appbansach.model.SignInRequest;
import com.tuan1611pupu.appbansach.model.User;
import com.tuan1611pupu.appbansach.model.UserExprech;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface DataService {

    // account
    @POST("api/Account/Register")
    Call<SignInRequest> createUser(@Query("role") String role, @Body SignInRequest signInRequest);

    @POST("api/Account/login")
    Call<Void> login(@Body LoginRequest loginRequest);

    @POST("api/Account/login-2FA")
    Call<UserExprech> login2FA(@Query("code") String code, @Query("username") String username);

    @POST("api/Account/logOut")
    Call<Void> logOut(@Header("Authorization") String token);

    @POST("api/Account/forgot-password")
    Call<Void> getPassword(@Query("email") String email, @Query("Password") String Password);

    @GET("api/Account/testToken")
    Call<Void> testToken(@Header("Authorization") String token);

    @GET("api/Account/getUser")
    Call<User> getUser(@Query("memberId") String memberId, @Header("Authorization") String token);

    // book
    @GET("api/Book/getAllBook")
    Call<List<Book>> getAllBooks(@Query("page") int page);

    @GET("api/Book/searchOfTitle")
    Call<List<Book>> getBookOfTitle(@Query("search") String search,@Query("page") int page);

    @POST("api/Book/addBook")
    Call<Book> addBook(@Body BookAdd bookAdd, @Header("Authorization") String token);

    @PUT("api/Book/updateBook")
    Call<Void> updateBook(@Query("bookId") int bookId,@Body Book book, @Header("Authorization") String token);

    @DELETE("api/Book/deleteBook")
    Call<Void> deleteBook(@Query("bookId") int bookId, @Header("Authorization") String token);

    @GET("api/Book/getBookInfo")
    Call<BookInfo> getBookInfo(@Query("bookId") int bookId);

    @GET("api/Book/randomBook")
    Call<List<Book>> getRandomBook();

    @GET("api/Book/bookOfcategory")
    Call<List<Book>> getBookOfCategory(@Query("categoryId") int categoryId,@Query("page") int page);


    // author
    @GET("api/Author/getAllAuthor")
    Call<List<Author>> getAllAuthor();

    @GET("api/Author/getAuthorById")
    Call<Author> getAuthor(@Query("authorId") int authorId);

    @PUT("api/Author/updateAuthor")
    Call<Author> updateAuthor(@Query("authorId") int authorId,@Query("authorName") String authorName, @Header("Authorization") String token);

    @POST("api/Author/addAuthor")
    Call<Author> addAuthor(@Query("authorName") String authorName, @Header("Authorization") String token);

    @DELETE("api/Author/deleteAuthor")
    Call<Void> deleteAuthor(@Query("authorId") int authorId, @Header("Authorization") String token);

    // category

    @GET("api/Category/allCategory")
    Call<List<Category>> getAllCategory();

    @POST("api/Category/addCategory")
    Call<Category> addCategory(@Query("categoryName") String categoryName, @Header("Authorization") String token);

    @PUT("api/Category/updateCategory")
    Call<Category> updateCategory(@Query("categoryId") int categoryId,@Query("categoryName") String categoryName, @Header("Authorization") String token);

    @DELETE("api/Category/deleteCategory")
    Call<Void> deleteCategory(@Query("categoryId") int categoryId, @Header("Authorization") String token);


    // Publisher
    @GET("api/Publisher/getAllPublisher")
    Call<List<Publisher>> getAllPublisher();

    @POST("api/Publisher/addPublisher")
    Call<Publisher> addPublisher(@Query("publisherName") String publisherName, @Header("Authorization") String token);

    @PUT("api/Publisher/updatePublisher")
    Call<Publisher> updatePublisher(@Query("publisherId") int publisherId,@Query("publisherName") String publisherName, @Header("Authorization") String token);

    @DELETE("api/Publisher/deletePublisher")
    Call<Void> deletePublisher(@Query("publisherId") int publisherId, @Header("Authorization") String token);

    //Invoice
    @GET("api/Invoice/GetAllInvoice")
    Call<List<Order>> getAllInvoice(@Header("Authorization") String token);

    @GET("api/Invoice/GetAllInvoiceOfStatus")
    Call<List<Order>> getInvoiceOfStatus(@Query("statusId") int statusId, @Header("Authorization") String token);

    @PUT("api/Invoice/updateStatusOfInvoice")
    Call<Void> updateStatus(@Query("invoiceId") int invoiceId,@Query("statusId") int statusId, @Header("Authorization") String token);

    // detail
    @POST("api/Detail/getDetaiByInvoiceId")
    Call<List<InvoiceDetail>> getDetail(@Query("invoiceId") int invoiceId, @Header("Authorization") String token);

    //  cartItem

    @POST("api/CartItem/getcartItemOfcart")
    Call<List<CartItem>> getCartItem(@Query("memberId") String memberId, @Header("Authorization") String token);

    @DELETE("api/CartItem/deleteCartItem")
    Call<Void> deleteCartItem(@Query("Id") int Id, @Header("Authorization") String token);

    @POST("api/CartItem/addCartItem")
    Call<Void> addCartItem(@Query("quantity") int quantity,@Query("bookId") int bookId,@Query("memberId") String memberId, @Header("Authorization") String token);

    @PUT("api/CartItem/updateCartItem")
    Call<Void> updateCartItem(@Query("cartItemId") int cartItemId,@Query("quantity") int quantity ,@Header("Authorization") String token);

    @POST("api/Invoice/addInvoice")
    Call<Invoice> addInvoice(@Body Invoice invoice, @Header("Authorization") String token);

    @GET("api/Invoice/searchOfmemberId")
    Call<List<Order>> getInvoiceUser(@Query("memberId") String memberId,@Header("Authorization") String token);

    @DELETE("api/Invoice/deleteInvoice")
    Call<Void> deleteInvoice(@Query("invoiceId") int invoiceId, @Header("Authorization") String token);

}

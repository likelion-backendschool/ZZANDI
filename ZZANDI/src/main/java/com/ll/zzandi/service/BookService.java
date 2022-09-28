package com.ll.zzandi.service;

import com.ll.zzandi.domain.Book;
import com.ll.zzandi.dto.BookDto;
import com.ll.zzandi.dto.BookInfoDto;
import com.ll.zzandi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book save(BookInfoDto bookDto) {
        Book b1 = new Book();
        b1.setBookName(bookDto.getItem().get(0).title);
        b1.setBookPage(bookDto.getItem().get(0).subInfo.getItemPage());
        b1.setBookAuthor(bookDto.getItem().get(0).author);
        b1.setBookPublisher(bookDto.getItem().get(0).publisher);
        b1.setBookIsbn(bookDto.getItem().get(0).isbn13);
        return bookRepository.save(b1);
    }

    public Optional<Book> findByid(Long bookId) {
        return bookRepository.findById(bookId);
    }

    public Book modify(Long bookId, BookDto bookDto) {
        Book b1 = new Book();
        b1.setId(bookId);
        b1.setBookName(bookDto.getBookName());
        b1.setBookPage(bookDto.getBookPage());
        b1.setBookAuthor(bookDto.getBookAuthor());
        b1.setBookPublisher(bookDto.getBookPublisher());
        b1.setBookIsbn(bookDto.getBookIsbn());
        return bookRepository.save(b1);
    }

    public void delete(Book books) {
        bookRepository.delete(books);
    }
}

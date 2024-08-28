package com.example.CRUDApplication.controller;
import java.io.*;
import java.util.*;

import com.example.CRUDApplication.model.Book;
import com.example.CRUDApplication.repo.BookRepo;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BookController {

    @Autowired
    private BookRepo bookRepo;


    @GetMapping("/getAllBooks")
    public ResponseEntity<Map<String, Object>> getAllBooks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort) {
        try {
            // Determine sorting order
            Sort sorting = Sort.unsorted();
            if (sort != null) {
                if ("desc".equalsIgnoreCase(sort)) {
                    sorting = Sort.by(Sort.Order.desc("id"));
                } else if ("asc".equalsIgnoreCase(sort)) {
                    sorting = Sort.by(Sort.Order.asc("id"));
                }
            }

            // Create Pageable instance with sorting
            Pageable pageable = PageRequest.of(page, size, sorting);
            Page<Book> bookPage = bookRepo.findAll(pageable);

            // Prepare response with pagination metadata
            Map<String, Object> response = new HashMap<>();
            response.put("content", bookPage.getContent());
            response.put("totalPages", bookPage.getTotalPages());
            response.put("totalElements", bookPage.getTotalElements());
            response.put("currentPage", bookPage.getNumber());
            response.put("pageSize", bookPage.getSize());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/getBookById/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {

        Optional<Book> bookData = bookRepo.findById(id);

        if(bookData.isPresent()) {
            return new ResponseEntity<>(bookData.get(),HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/addBook")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
            Book bookObj = bookRepo.save(book);

            return new ResponseEntity<>(bookObj, HttpStatus.OK);
    }

    @PostMapping("/updateBookById/{id}")
    public ResponseEntity<Book> updateBookById(@PathVariable Long id, @RequestBody Book newBookData) {
        Optional<Book> oldBookData = bookRepo.findById(id);

        if(oldBookData.isPresent()) {
            Book updatedBookData = oldBookData.get();
            updatedBookData.setTitle(newBookData.getTitle());
            updatedBookData.setAuthor(newBookData.getAuthor());

            Book bookObj = bookRepo.save(updatedBookData);
            return new ResponseEntity<>(bookObj, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteBookById/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        if (!bookRepo.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        bookRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/getBooksByTitle")
    public ResponseEntity<List<Book>> getBooksByTitle(@RequestParam String title) {
        try {
            List<Book> bookList = bookRepo.findByTitleContainingIgnoreCase(title);

            if (bookList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(bookList, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/exportCsv", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportBooksToCsv(HttpServletResponse response) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=books.csv");

        String filePath = "C:/Users/aanagal/Downloads/books.csv";

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath))) {

            csvWriter.writeNext(new String[]{"ID", "Title", "Author"});

            bookRepo.findAll().forEach(book -> {
                csvWriter.writeNext(new String[]{
                        String.valueOf(book.getId()),
                        book.getTitle(),
                        book.getAuthor()
                });
            });

        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return;
        }

        try (InputStream inputStream = new FileInputStream(filePath);
             OutputStream outputStream = response.getOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @PostMapping("/importCsv")
    public ResponseEntity<?> importBooksFromCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a CSV file.");
        }

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVReader csvReader = new CSVReader(reader);
            String[] nextRecord;

            boolean isHeader = true;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String title = nextRecord[0].trim();
                String author = nextRecord[1].trim();

                bookRepo.save(new Book(title, author));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing CSV file.");
        }

        return ResponseEntity.ok("CSV file imported successfully.");
    }

}


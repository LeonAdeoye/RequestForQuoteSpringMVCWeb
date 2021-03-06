package com.leon.rfq.controllers;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.leon.rfq.domains.BookDetailImpl;
import com.leon.rfq.services.BookService;
import com.leon.rfq.validators.BookValidatorImpl;

@Controller
@RequestMapping("/books")
public class BookControllerImpl
{
	private static final Logger logger = LoggerFactory.getLogger(BookControllerImpl.class);
	
	@Autowired(required=true)
	BookService bookService;
	
	
	@Autowired(required=true)
	private BookValidatorImpl bookValidator;
	
	@InitBinder
	public void initialiseBinder(WebDataBinder binder)
	{
		binder.setAllowedFields("bookCode", "description", "isValid");
		binder.setValidator(this.bookValidator);
	}
	
	@RequestMapping()
	public String getAll(Model model)
	{
		return "books";
	}
	
	@RequestMapping(value="/ajaxGetListOfAllBooks", method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Set<BookDetailImpl> ajaxGetListOfAllBooks()
	{
		return this.bookService.getAllFromCacheOnly();
	}
	
	@RequestMapping(value = "/ajaxUpdateValidity", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxUpdateValidity(@RequestBody BookDetailImpl newBook)
	{
		return this.bookService.updateValidity(newBook.getBookCode(), newBook.getIsValid(), newBook.getLastUpdatedBy());
	}
	
	@RequestMapping(value = "/ajaxAddNewBook", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxAddNewBook(@RequestBody BookDetailImpl newBook)
	{
		return this.bookService.insert(newBook.getBookCode(), newBook.getEntity(), newBook.getIsValid(), newBook.getLastUpdatedBy());
	}
	
	@RequestMapping(value = "/matchingBookTags", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object getBookMatches(@RequestParam String pattern)
	{
		return this.bookService.getMatchingBookTags(pattern.toUpperCase());
	}
}

package com.leon.rfq.controllers;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	
	@RequestMapping(value="/all", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Set<BookDetailImpl> getAllBook()
	{
		return this.bookService.getAllFromCacheOnly();
	}
		
	@RequestMapping()
	public String getAll(Model model)
	{
		model.addAttribute("books", this.bookService.getAll());
		return "books";
	}
	
	@RequestMapping("/book")
	public String get(@RequestParam String bookCode, Model model)
	{
		model.addAttribute("book", this.bookService.get(bookCode));
		return "book";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String getNewBookForm(Model model)
	{
		BookDetailImpl book = new BookDetailImpl();
		model.addAttribute("newBook", book);
		
		return "addBook";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String processNewBookForm(@ModelAttribute("newBook") @Valid BookDetailImpl newBook,
			BindingResult result, HttpServletRequest request)
	{
		String[] suppressedFields = result.getSuppressedFields();
		if(suppressedFields.length > 0)
		{
			throw new RuntimeException("Attempting to bind disallowed fields: " +
					StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		
		if(result.hasErrors())
			return "addBook";
		
		this.bookService.insert(newBook.getBookCode(), newBook.getEntity(), newBook.getIsValid(), "ladeoye"); //TODO
		
		return "redirect:/books";
	}
	
	@RequestMapping("/delete")
	public String delete(@RequestParam String bookCode, Model model)
	{
		this.bookService.delete(bookCode);
		
		return "redirect:/books";
	}

	@RequestMapping("/updateValidity")
	public String update(@RequestParam String bookCode, @RequestParam String entity, @RequestParam boolean isValid, @RequestParam String updatedByUser, Model model)
	{
		this.bookService.updateValidity(bookCode, isValid, updatedByUser);
		
		return "redirect:/books";
	}
	
	@RequestMapping(value = "/ajaxUpdateValidity", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxUpdateValidity(@RequestParam String bookCode,
			@RequestParam boolean isValid, @RequestParam String updatedByUser)
	{
		return this.bookService.updateValidity(bookCode, isValid, updatedByUser);
	}
	
	@RequestMapping(value = "/matchingBookTags", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object getBookMatches(@RequestParam String pattern)
	{
		return this.bookService.getMatchingBookTags(pattern.toUpperCase());
	}
	

}

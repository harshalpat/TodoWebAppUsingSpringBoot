package com.harshal.springboot.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.validation.Valid;

import com.harshal.springboot.web.model.Todo;
import com.harshal.springboot.web.service.TodoService;

@Controller
@SessionAttributes("name")
public class TodoController { 
	
	@Autowired
	TodoService service;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// Date - dd/MM/yyyy
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
	}
	@RequestMapping(value="/list-todos", method=RequestMethod.GET)
	// to return to the browser as it is!
	//@ResponseBody
	public String showTodos(ModelMap model) {
		// search for view named "login" and coz of view resolver it returns login.jsp
		String name = getLoggedInUserName(model); 
		//System.out.println(name);
		model.put("todos", service.retrieveTodos(name));
		return "list-todos";
	}
	private String getLoggedInUserName(ModelMap model) {
		return (String)model.get("name");
	}
	@RequestMapping(value="/add-todos",method=RequestMethod.GET)
	public String showAddTodoPage(ModelMap model) {
		//model.addAttribute("todos", new Todo(0, (String) model.get("name"),"",new Date(), false));
	model.addAttribute("todo", new Todo(0, getLoggedInUserName(model), "Default Desc",
				new Date(), false));
		return "todos";
	 
	}
	@RequestMapping(value="/add-todos",method=RequestMethod.POST)
	public String addTodo(ModelMap model,@Valid @ModelAttribute Todo todo, BindingResult result) {
		System.out.println(result.hasErrors());
		if(result.hasErrors()) { 
			return "todos";
		}
		service.addTodo(getLoggedInUserName(model), todo.getDesc(),todo.getTargetDate(), false);
		return "redirect:/list-todos"; // redirects to .jsp file 
	 
	} 
	
	@RequestMapping(value="/delete-todos",method=RequestMethod.GET)
	public String deleteTodo(ModelMap model, @RequestParam int id) {
		service.deleteTodo(id);
		return "redirect:/list-todos"; 
	 
	}
	@RequestMapping(value="/update-todos",method=RequestMethod.GET)
	public String showUpdateTodoPage(ModelMap model, @RequestParam int id,ModelMap model1) {
		Todo todo = service.retrieveTodo(id);
		model1.put("todo", todo);
		return "todos"; 
	 
	}
	@RequestMapping(value="/update-todos",method=RequestMethod.POST)
	public String updateTodo(Todo todo, ModelMap model1) {
		todo.setUser(getLoggedInUserName(model1));
		service.updateTodo(todo);
		return "redirect:/list-todos"; 
	 
	}
} 

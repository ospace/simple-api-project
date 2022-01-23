package com.tistory.ospace.api.controller;

import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tistory.ospace.api.controller.model.ListRS;
import com.tistory.ospace.api.controller.model.ModelUtils;
import com.tistory.ospace.api.controller.model.SearchKeyword;
import com.tistory.ospace.api.controller.model.User;
import com.tistory.ospace.api.repository.dto.SearchDto;
import com.tistory.ospace.api.repository.dto.UserDto;
import com.tistory.ospace.api.service.UserService;
import com.tistory.ospace.api.util.SpringUtils;


@RestController
@RequestMapping("/api/users")
public class UserController{
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	/**
	 * 검색
	 * @param code
	 * @return
	 * @throws  
	 */
	@GetMapping
	public ListRS<?> get(SearchKeyword searchKeyword) {
		logger.info("get begin: searchKeyword[{}]", searchKeyword);
		
		SearchDto search = ModelUtils.convert(searchKeyword, new SearchDto());
		SpringUtils.offsetPage(searchKeyword);
		List<UserDto> res = userService.search(search);
		ListRS<User> ret = ListRS.of(res, it->ModelUtils.convert(it, new User()));
		
		logger.info("get end:");
		return ret;
	}
	
	/**
	 * 상세조회
	 * @param code
	 * @return
	 * @throws  
	 */
	@ResponseBody
	@GetMapping(value="/{id}")
	public User getOne(@PathVariable("id") Integer id) {
		logger.info("getOne begin: id[{}]", id);
		
		User ret = ModelUtils.convert(userService.getById(id), new User());

		logger.info("getOne end: ret[{}]", ret);
		return ret;
	}
	
	/**
	 * 등록
	 * @param code
	 * @return
	 * @throws  
	 */
	@PostMapping
	public void create(@Valid @RequestBody User user) {
		User userClone = (User) user.clone();
		userClone.setPassword("[Hidden]");
		logger.info("create begin: user[{}]", userClone);
		
		userService.save(ModelUtils.convert(user, new UserDto()));
		
		logger.info("create end:");
	}
	
	/**
	 * 수정
	 * @param code
	 * @return
	 * @throws  
	 */
	@PutMapping
	public void update(@RequestBody User user) {
		User userClone = (User) user.clone();
		userClone.setPassword("[Hidden]");
		logger.info("update begin: user[{}]", userClone);
		
		userService.save(ModelUtils.convert(user, new UserDto()));
		
		logger.info("update end:");
	}
	
	/**
	 * User 삭제
	 * @param code
	 * @return
	 * @throws ParseException 
	 */
	@DeleteMapping(value="/{id}")
	public void delete(@PathVariable("id") Integer id) {
		logger.info("delete begin: id[{}]", id);
		
		userService.deleteById(id);
		
		logger.info("delete end:");
	} 
}
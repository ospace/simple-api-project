package com.tistory.ospace.api.controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tistory.ospace.annotation.timelog.TimeLog;
import com.tistory.ospace.api.controller.model.ModelUtils;
import com.tistory.ospace.api.controller.model.User;
import com.tistory.ospace.api.util.SpringUtils;


@Controller
public class MainController {
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);


	@TimeLog
	@GetMapping("/api/me")
    @ResponseBody
    public User me() {
		logger.info("me begin:");
		User ret = ModelUtils.convert(SpringUtils.getUser(), new User());
		logger.info("me end: ret[{}]", ret);
		
		return ret;
    }
	
	/**
      * Heart beat
      * 
      * @return 
    */
    @GetMapping("/hb")
    @ResponseBody
    public String hb() {
        return LocalDateTime.now().toString();
    }
}
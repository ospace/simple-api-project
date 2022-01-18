package com.tistory.ospace.api.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tistory.ospace.api.exception.DataIntegrityException;
import com.tistory.ospace.api.exception.DuplicateException;
import com.tistory.ospace.api.repository.UserRepository;
import com.tistory.ospace.api.repository.dto.SearchDto;
import com.tistory.ospace.api.repository.dto.UserDto;
import com.tistory.ospace.api.util.BaseUtils;
import com.tistory.ospace.common.DataUtils;
import com.tistory.ospace.common.StrUtils;


@Service
public class UserService{
	
	@Autowired
	private UserRepository userRepo;
		
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * 회원 조회
	 * @param usrSrl
	 * @return
	 */
	public UserDto getById(Integer id){
		return  userRepo.findById(id);
	}
	
	/**
	 * 회원 삭제
	 * @param user
	 */
	@Transactional
	public void delete(UserDto user) {
		userRepo.deletetById(user.getId());
	}

	public int count(SearchDto search) {
		return userRepo.countBy(search);
	}

	public List<UserDto> search(SearchDto search) {
		List<UserDto> ret = userRepo.findAllBy(search);
		return ret;
	}
	
	public List<UserDto> searchIn(Collection<Integer> ids) {
		if(DataUtils.isEmpty(ids)) return null;
		return userRepo.findAllIn(ids);
	}

	public void save(UserDto user) {
		if(StrUtils.isNotEmpty(user.getPassword())) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		
		if(user.getId() != null) {
			user.setModifier(BaseUtils.getUserId());
			user.setLoginId(null);
			userRepo.update(user);
		} else {
			if(null != userRepo.findByLoginId(user.getLoginId())) {
				throw new DuplicateException("아이디중복:"+user.getLoginId());
			}
			
			user.setCreator(BaseUtils.getUserId());
			userRepo.insert(user);
		}
	}

	public void deleteById(Integer id) {
		if(null == id) return;
		try {
			userRepo.deletetById(id);
		} catch(DataIntegrityViolationException ex) {
			throw new DataIntegrityException("사용자삭제: id["+id+"]", ex);
		}
		
	}

	public UserDto getByLoginId(String loginId) {
		if(StrUtils.isEmpty(loginId)) return null;
		return userRepo.findByLoginId(loginId);
	}
}

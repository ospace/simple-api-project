package com.tistory.ospace.api.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tistory.ospace.api.repository.UserRepository;
import com.tistory.ospace.api.repository.dto.AuthDto;
import com.tistory.ospace.api.repository.dto.UserDto;


@Service
public class AuthService  implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	
	@Autowired
	private UserRepository userRepo;

    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException, DataAccessException {
    	logger.debug("loadUserByUsername begin: userId[{}]", userId);
    	
		UserDto account = this.userRepo.findByLoginId(userId);
		
		logger.debug("account[{}]", account);
		
		//if(null == account || account.isUse()) account = null;
		//회원 권한
//		if(dto != null){
//			
//			List<String> roleList = new ArrayList<String>();
//			List<Account> authList = accountRepo.getUserAuthList(userId);
//			
//			for(Account lst :  authList ){
//				roleList.add( lst.getRoleCode()));
//			}
//			
//			dto.setRoleList(roleList);
//		}

		AuthDto ret = AuthDto.of(account);
		
		//logger.debug("loadUserByUsername end: ret[{}]", ret);
		
		return ret;
    }
    
    public void insertLoginLog(int accountId, String successYn, String remoteAddr, String comment) {
//        AccountHistory accountHistory = new AccountHistory();
//        accountHistory.setSuccessYn(successYn);
//        accountHistory.setRemoteIp(remoteAddr);
//        accountHistory.setDescription(comment);
//
//        this.accountRepo.insertAccountHistory(accountHistory);
    }
    
}
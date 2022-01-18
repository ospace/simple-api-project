package com.tistory.ospace.api.repository.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tistory.ospace.common.BaseDto;

@Alias("AuthAccount")
public class AuthDto extends BaseDto implements UserDetails {
	private static final long serialVersionUID = -7941242818336586872L;
	
	private UserDto user;

	public static AuthDto of(UserDto user) {
		return new AuthDto(user);
	}
	
	public AuthDto(UserDto user) {
		this.user = user;
	}

	public UserDto getUser() {
		return user;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		//authority code: ROLEXXXX
		authorities.add( new SimpleGrantedAuthority( "ROLE_" + user.getRoleCode().substring(4)) );

		return authorities;
	}

	@Override
	public String getUsername() {
		return user == null ? "" : user.getLoginId();
	}
	 
	@Override
	public String getPassword() {
		return user == null ? "" : user.getPassword();
	}
	
	public String getAuthority() {
		return null == user ? "" : user.getRoleCode();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return null == user ? false : user.isUse();
	}
	 
	@Override
	 public int hashCode() {
		return user.getLoginId().hashCode();
	 }
	 
	@Override
    public boolean equals(Object rhs) {
        if (rhs instanceof AuthDto) {
            return user.getLoginId().equals(((AuthDto) rhs).user.getLoginId());
        }
        return false;
    }

}

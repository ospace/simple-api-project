package com.tistory.ospace.api.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tistory.ospace.api.controller.model.ModelUtils;
import com.tistory.ospace.api.exception.DataIntegrityException;
import com.tistory.ospace.api.repository.CodeRepository;
import com.tistory.ospace.api.repository.dto.CodeDto;
import com.tistory.ospace.api.repository.dto.SearchDto;
import com.tistory.ospace.api.util.SpringUtils;
import com.tistory.ospace.common.DataUtils;
import com.tistory.ospace.common.StrUtils;

@Service
@Transactional
public class CodeService {
	private static final Logger logger = LoggerFactory.getLogger(CodeService.class);
	private static final String CATE = "1001";
	
	@Autowired
	private CodeRepository codeRepo;
	
	public List<CodeDto> searchRole() {
		return codeRepo.findAllCodeBy(null, CodeDto.ofGroup("ROLE", null));
	}
	
	public Map<String,CodeDto> searchRoleMap() {
		return ModelUtils.convertMap(searchRole());
	}
	
	public List<CodeDto> searchCategory(SearchDto search) {
		CodeDto code = CodeDto.ofGroup(CATE, null);
		return searchCode(search, code);
	}
	
	public int countCategory(SearchDto search) {
		CodeDto code = CodeDto.ofGroup(CATE, null);
		return codeRepo.countCodeBy(search, code);
	}
	
	public List<CodeDto> searchCode(SearchDto search, CodeDto code) {
		List<CodeDto> ret = codeRepo.findAllCodeBy(search, code);
		return ret;
	}
	
	public CodeDto getCodeById(String id) {
		if(StrUtils.isEmpty(id)) return null;
		return codeRepo.findCodeById(id);
	}
	
	@Transactional
	public void saveCategories(List<CodeDto> data) {
		DataUtils.iterate(data, it->saveCategory(it));
	}
	
	public void saveCategory(CodeDto code) {
		if(null == code) return;
		code.setGroupCode(CATE);
		saveCode(code);
	}
	
	public void saveCode(CodeDto code) {
		if(StrUtils.isEmpty(code.getCode())) {
			insertCode(code);
		} else {
			updateCode(code);
		}
	}
	
	protected void insertCode(CodeDto code) {
		logger.debug("insertCode begin: code[{}]", code);
		long runtime = System.currentTimeMillis();
		
		if(StrUtils.isEmpty(code.getGroupCode()) || 4 < code.getGroupCode().length()) {
			throw new RuntimeException("saveCode failed: invalid group code");
		}
		
		String nextCode = generateNextCode(code.getGroupCode());
		
		if(null == code.getOrder() || 0 > code.getOrder()) {
			Integer nextOrder = 1;
			if(StrUtils.isNotEmpty(nextCode)) {
				nextOrder = codeRepo.findMaxOrderByGroup(code.getGroupCode())+1;
			}
			code.setOrder(nextOrder);
		} else {
			CodeDto existCode = getSameOrder(code);
			if(null != existCode) {
				increaseOrder(code);
			}
		}
		
		code.setCode(nextCode);
		SpringUtils.applyUser(code);
		codeRepo.insertCode(code);
		
		logger.debug("insertCode end: code[{}] runtime[{} msec]", code, System.currentTimeMillis()-runtime);
	}
	
	protected String generateNextCode(String groupCode) {
		String lastCode = codeRepo.findLastCodeByGroup(groupCode);
		
		String ret = null;
		if(StrUtils.isEmpty(lastCode)) {
			ret = groupCode+"0001";
		} else {
			int codeNum = Integer.parseInt(lastCode)+1;
			ret = String.format("%08d", codeNum);
			logger.debug("generateNextCode: from[{}] to[{}]", codeNum, ret);
		}
		
		return ret;
	}
	
	protected void updateCode(CodeDto code) {
		CodeDto targetCode = exchangeDuplicateOrder(code);
		if(null != targetCode) {
			switchOrder(code, targetCode);
		} else {
			SpringUtils.applyUser(code);
			codeRepo.updateCode(code);
		}
	}
	
	/*
	 * 중복된 순서가 있다면 해당 엔티티와 순서값을 교환
	 */
	protected CodeDto exchangeDuplicateOrder(CodeDto code) {
		if(null == code.getOrder()) return null;
		
		CodeDto orgCode = getCodeById(code.getCode());
		if(code.getOrder() == orgCode.getOrder()) return null;
		
		CodeDto ret = getSameOrder(code);
		if(null == ret) return null;
		
		ret.setOrder(orgCode.getOrder());
		
		return ret;
	}
	
	protected CodeDto getSameOrder(CodeDto code) {
		return getSameOrder(code.getGroupCode(), code.getOrder());
	}
	
	protected CodeDto getSameOrder(String groupCode, int order) {
		return codeRepo.findCodeBy(CodeDto.ofGroup(groupCode, order));
	}
	
	public void deleteCategory(String code) {
		CodeDto codeObj = getCodeById(code);
		if(null == codeObj) return;
		
		try {
			codeRepo.deleteCodeBy(codeObj);
		} catch(DataIntegrityViolationException ex) {
			throw new DataIntegrityException("카테고리삭제: code["+code+"]", ex);
		}
		
		SpringUtils.applyUser(codeObj);
		decreaseOrder(codeObj);
	}
	
	public void categoryOrderUp(String code) {
		if(StrUtils.isEmpty(code)) return;
		
		CodeDto codeSearch = new CodeDto();
		codeSearch.setCode(code);
		codeSearch.setGroupCode(CATE);
		
		CodeDto aCode = codeRepo.findFirstOrderBy(codeSearch, true);
		if(null == aCode) return;
		
		switchOrder(getCodeById(code), aCode);
	}

	public void categoryOrderDown(String code) {
		if(StrUtils.isEmpty(code)) return;
		
		CodeDto codeSearch = new CodeDto();
		codeSearch.setCode(code);
		codeSearch.setGroupCode(CATE);
		
		CodeDto aCode = codeRepo.findFirstOrderBy(codeSearch, false);
		if(null == aCode) return;
		
		switchOrder(getCodeById(code), aCode);
	}
	
	protected void switchOrder(CodeDto l, CodeDto r) {
		int order = l.getOrder();
		l.setOrder(r.getOrder());
		r.setOrder(order);
		
		SpringUtils.applyUser(l);
		SpringUtils.applyUser(r);
		
		codeRepo.updateCode(l);
		codeRepo.updateCode(r);
	}
	
	protected void increaseOrder(CodeDto code) {
		codeRepo.rearrange(code, 1);
	}
	
	protected void decreaseOrder(CodeDto code) {
		codeRepo.rearrange(code, -1);
	}

}

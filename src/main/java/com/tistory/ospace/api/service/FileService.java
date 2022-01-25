package com.tistory.ospace.api.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tistory.ospace.api.exception.DataIntegrityException;
import com.tistory.ospace.api.repository.FileRepository;
import com.tistory.ospace.api.repository.dto.FileDto;
import com.tistory.ospace.api.util.SpringUtils;
import com.tistory.ospace.common.CmmUtils;
import com.tistory.ospace.common.DateUtils;
import com.tistory.ospace.common.FileUtils;
import com.tistory.ospace.common.StrUtils;


@Service
public class FileService {
	private static final Logger logger = LoggerFactory.getLogger(FileService.class);
	private static final char SEPERATOR = File.separatorChar;
	
	@Value("${simple-api.file.store-path:/tmp/simpleapi}") 
	private String STORE_PATH;
	
	@Autowired
	private FileRepository fileRepo;
	
	public FileDto upload(MultipartFile mf) {
		return upload(mf, SpringUtils.getUserId());
	}
	
	@Transactional
	public FileDto upload(MultipartFile mf,Integer userId) {
		if (null == mf || mf.isEmpty()) return null;
		
		FileDto file = newFileDto(mf);
		
		try {
			saveFile(mf.getInputStream(), file);
		} catch(Exception e) {
			throw new RuntimeException(file.getOrgFilename() + " upload failed: ", e);
		}
		
		SpringUtils.applyUser(file, userId);
		fileRepo.insert(file);
		
		return file;
	}
	
	public FileDto readFile(String id, HttpServletResponse res) throws IOException {
		if(null == id) return null;
		
		FileDto file = fileRepo.findById(id);
		if(null == file) return null;
		
		File f =  new File(getStorePath(file));
		
		res.setContentType(file.getType());
		res.setContentLength((int)f.length());
		
		FileUtils.copy(f, res.getOutputStream());
		
		if(f.length() != file.getSize()) {
			logger.warn("file size is different: id[{}] original[{}] saved[{}]", file.getId(), file.getSize(), f.length());
		}
		
		return file;
	}
	
	public void readFile(String filepath, String filename, HttpServletResponse res) throws IOException {
		File f = new File(getStorePath(filepath, filename));
		
		// https://www.baeldung.com/java-file-mime-type
		//String contentType = f.toURI().toURL().openConnection().getContentType();
		String contentType = Files.probeContentType(f.toPath());
		res.setContentType(contentType);
		res.setContentLength((int)f.length());
		FileUtils.copy(f, res.getOutputStream());
	}
	
	public void delete(String filepath, String filename) throws IOException {
		FileDto file = getFileFromFilename(filename);
		if (!filepath.equals(file.getFilepath())) {
			throw new FileNotFoundException("invalid filepath");
		}
		
		try {
			fileRepo.deleteById(file.getId());
		} catch(DataIntegrityViolationException ex) {
			throw new DataIntegrityException("파일삭제: id["+file.getId()+"]", ex);
		}

		String storePath = getStorePath(file);
		FileUtils.deleteFile(storePath);
	}
	
	public void download(String filepath, String filename, HttpServletRequest req, HttpServletResponse res) throws IOException {
		FileDto file = getFileFromFilename(filename);
		String origFilename =  file.getOrgFilename();
		File f =  new File(getStorePath(file));
		
		String downloadFilename = encodeFilename(getBrowserType(req), origFilename);
        res.setContentType("application/octet-stream");
        //res.setContentType("application/x-msdownload");
        res.setHeader("Content-Disposition", "attachment; filename=" +  downloadFilename);
        res.setHeader("Content-Transfer-Encoding", "binary");
        res.setHeader("Content-Length", "" + f.length() );
        res.setHeader("Pragma", "no-cache");
        res.setHeader("Expires", "0");
        
		FileUtils.copy(f, res.getOutputStream());
	}
	
	private FileDto getFileFromFilename(String filename) throws FileNotFoundException {
		if (StrUtils.isEmpty(filename)) {
			throw new FileNotFoundException("filename is empty");
		}
		
		String id = filename.substring(0, filename.lastIndexOf('.'));
		if (32 > id.length()) {
			throw new FileNotFoundException("id is invalid");
		}
		
		FileDto file = fileRepo.findById(id);
		
		if(null == file) {
			throw new FileNotFoundException("not found");
		}
		
		return file;
	}
	
	private FileDto newFileDto(MultipartFile mf) {
		FileDto ret = new FileDto();
		
		ret.setId(CmmUtils.newId());
		
		String filepath = DateUtils.toStringDateShort2(DateUtils.now());
		ret.setFilepath(filepath);
		
		String filename = mf.getOriginalFilename();
		ret.setOrgFilename(filename);
		
		String fileExt = FileUtils.getExtension(filename);
		if("jsp".equals(fileExt)) fileExt = "txt";
		ret.setExtension(fileExt);
		
		ret.setSize(mf.getSize());
		ret.setType(mf.getContentType());
		
		return ret;
	}
	
	private String getStorePath(FileDto file) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(STORE_PATH)
		  .append(SEPERATOR).append(file.getFilepath())
		  .append(SEPERATOR).append(file.getId())
		  .append(".").append(file.getExtension());
		
		return sb.toString();
	}
	
	private String getStorePath(String filepath, String filename) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(STORE_PATH)
		  .append(SEPERATOR).append(filepath)
		  .append(SEPERATOR).append(filename); 
		
		return sb.toString();
	}
	
	private void saveFile(InputStream in, FileDto file) throws Exception {
		String filepath = getStorePath(file);
		
		Path path = Paths.get(filepath);
		FileUtils.mkdirs(path.getParent().toString());
		
		try {
			FileUtils.copy(in, path);
		} catch (Exception e) {
			FileUtils.deleteFile(filepath);
			throw e;
		}
	}
	
	private static String encodeFilename(String browser, String filename) {
		try {
			switch(browser) {
			case "MSIE":
				return URLEncoder.encode(filename, "UTF-8").replace("+", "%20");
			case "Firefox": case "Opera":
				return "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
			case "Chrome": {
				StringBuffer sb = new StringBuffer();
	            char c = 0;
	            int len = filename.length();
	            for (int i = 0; i < len; i++) {
	                c = filename.charAt(i);
	                if (c > '~') {
	                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
	                } else {
	                    sb.append(c);
	                }
	            }
	            return sb.toString();
				}
			default:
				break;
			}
		} catch(UnsupportedEncodingException e) {
			throw new RuntimeException("encoding failed: " + filename);
		}
		
		throw new RuntimeException("Not supported browser: " + browser);
	}
	
	//ref: http://mkil.tistory.com/223
	private static String getBrowserType(HttpServletRequest req) {
        String header = req.getHeader("User-Agent");
        if (header.indexOf("MSIE") > -1) {
            return "MSIE";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        } else if (header.indexOf("Trident/") > -1){ //IE8~
            //IE 버전 별 체크 >> Trident/6.0(IE 10) , Trident/5.0(IE 9) , Trident/4.0(IE 8)
            return "MSIE";
        }
        return "Firefox";
    }
}

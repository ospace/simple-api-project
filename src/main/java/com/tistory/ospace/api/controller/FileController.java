package com.tistory.ospace.api.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tistory.ospace.annotation.timelog.TimeLog;
import com.tistory.ospace.api.controller.model.FileRS;
import com.tistory.ospace.api.controller.model.ListRS;
import com.tistory.ospace.api.controller.model.ModelUtils;
import com.tistory.ospace.api.repository.dto.FileDto;
import com.tistory.ospace.api.service.FileService;
import com.tistory.ospace.common.DataUtils;

@TimeLog
@RestController
@RequestMapping("/file")
public class FileController{
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    
    @Autowired
    private FileService fileService;
    
    /**
     * 파일 획득
     * @param code
     * @return
     * @throws IOException 
     * @throws FileNotFoundException   
     */
    @GetMapping(value="/{filepath}/{filename}")
    public void get(@PathVariable("filepath") String filepath, @PathVariable("filename") String filename, HttpServletResponse res) throws IOException {
        logger.debug("get begin: filepath[{}] filename[{}]", filepath, filename);
        long runtime = System.currentTimeMillis();
        
        fileService.readFile(filepath, filename, res);
        
        logger.debug("get end: runtime[{} msec]", System.currentTimeMillis()-runtime);
    }
    
//    @GetMapping(value="/**")
//    public void get(HttpServletRequest req, HttpServletResponse res) throws IOException {
//    	String uri = req.getRequestURI();
//        logger.debug("get begin: uri[{}]", uri);
//        long runtime = System.currentTimeMillis();
//        logger.debug("get end: runtime[{} msec]", System.currentTimeMillis()-runtime);
//    }

    /**
     * 파일 다운로드
     * @param code
     * @return
     * @throws IOException  
     */
    @GetMapping(value="/download/{filepath}/{filename}")
    public void download(@PathVariable("filepath") String filepath, @PathVariable("filename") String filename, HttpServletRequest req, HttpServletResponse res) throws IOException {
        logger.debug("download begin: filepath[{}] filename[{}] filenames[{}]", filepath, filename);
        long runtime = System.currentTimeMillis();
        
        fileService.download(filepath, filename, req, res);
        
        logger.debug("download end: runtime[{} msec]", System.currentTimeMillis()-runtime);
    }
    
    /**
     * 파일 등록
     * @param file
     * @return
     * @throws  
     */
    @PostMapping
    @Transactional
    public ListRS<?> upload(@RequestParam("file") List<MultipartFile> files) throws ParseException {
        logger.debug("upload begin: size[{} ea]", files.size());
        long runtime = System.currentTimeMillis();
        
        List<FileDto> res = DataUtils.map(files, it->fileService.upload(it));
        ListRS<?> ret = ListRS.of(res, it->ModelUtils.convert(it, new FileRS()));
        
        logger.debug("upload end: size[{} ea] runtime[{} msec]", res.size(), System.currentTimeMillis()-runtime);
        
        return ret;
    }
    
    /**
     * 파일 삭제
     * @param code
     * @return
     * @throws IOException 
     * @throws ParseException 
     */
    @DeleteMapping(value="/{filepath}/{filename}")
    public void delete(@PathVariable("filepath") String filepath, @PathVariable("filename") String filename) throws IOException {
        logger.debug("delete begin: filepath[{}] filename[{}]", filepath, filename);
        long runtime = System.currentTimeMillis();
        
        fileService.delete(filepath, filename);
        
        logger.debug("delete end: runtime[{} msec]", System.currentTimeMillis()-runtime);
    }
}
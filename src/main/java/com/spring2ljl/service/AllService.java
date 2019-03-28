package com.spring2ljl.service;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface AllService {
    void upload(MultipartFile file, HttpServletRequest request) throws IOException;

    void deleteByIds(List<Integer> ids);

    void query(Model model);
}

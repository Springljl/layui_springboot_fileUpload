package com.spring2ljl.service.impl;

import com.spring2ljl.mapper.AllMapper;
import com.spring2ljl.pojo.All;
import com.spring2ljl.service.AllService;
import com.spring2ljl.util.DateUtil;
import com.spring2ljl.util.IpUtil;
import com.spring2ljl.util.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class AllServiceImpl implements AllService {
    @Autowired
    private AllMapper allMapper;

    @Override
    public void upload(MultipartFile file, HttpServletRequest request) throws IOException {
        String path = ResourceUtils.getURL("classpath:").getPath() + "/static/images";
        String ipAddr = IpUtil.getIpAddr(request);
        int i = file.getOriginalFilename().lastIndexOf(".");
        Long time = System.currentTimeMillis();
        String name = time + file.getOriginalFilename().substring(i);
        File file2 = new File(path);
        if (!file2.exists()) {
            file2.mkdir();
        }
        File file1 = new File(path + "/" + name);
        file.transferTo(file1);
        QiniuUtil.upload(file1.getPath(), name);
        All all = new All();
        all.setIp(ipAddr);
        all.setName(name);
        all.setSize(file1.length() / 1024);
        all.setTime(time);
        file1.delete();
        allMapper.insert(all);
    }

    @Override
    public void deleteByIds(List<Integer> ids) {
        Example example = new Example(All.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", ids);
        allMapper.deleteByExample(example);
    }

    @Override
    public void query(Model model) {
        List<All> list = allMapper.selectAll();
        for (All all : list) {
            all.setVTime(DateUtil.format(all.getTime()));
        }
        Collections.sort(list, new Comparator<All>() {
            @Override
            public int compare(All o1, All o2) {
                return (int) (o2.getTime() - o1.getTime());
            }
        });
        model.addAttribute("images", list);
    }
}

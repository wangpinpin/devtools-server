package com.wpp.devtools.service;

import com.wpp.devtools.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private ActivityRepository activityRepository;

    /**
     * 查询活动列表
     * @return
     */
    public Object findActivityList() {
        return activityRepository.findAllByOrderBySort();
    }
}

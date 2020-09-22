package com.wpp.devtools.service;

import com.alibaba.fastjson.JSON;
import com.wpp.devtools.domain.bo.AddSubscribeBo;
import com.wpp.devtools.domain.entity.Subscribe;
import com.wpp.devtools.domain.entity.SubscribeRecord;
import com.wpp.devtools.enums.ExceptionCodeEnums;
import com.wpp.devtools.exception.CustomException;
import com.wpp.devtools.repository.ActivityRepository;
import com.wpp.devtools.repository.SubscribeRecordRepository;
import com.wpp.devtools.repository.SubscribeRepository;
import com.wpp.devtools.util.CommonUtils;
import com.wpp.devtools.util.EmailUtil;
import com.wpp.devtools.util.JpaUpdateUtil;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private SubscribeRepository subscribeRepository;

    @Autowired
    private SubscribeRecordRepository subscribeRecordRepository;

    @Autowired
    private UnAuthService unAuthService;

    @Autowired
    private EmailUtil emailUtil;

    /**
     * 查询活动列表
     *
     * @return
     */
    public Object findActivityList() {
        return activityRepository.findAllByOrderBySort();
    }

    /**
     * 添加订阅
     *
     * @param a
     * @param userId
     * @return
     */
    public void addSubscribe(AddSubscribeBo a,
            String userId) {

        Subscribe s = subscribeRepository.findCancelByEmail(a.getEmail());
        if (null != s) {
            //判断是否已经存在
            if(StringUtils.isBlank(a.getId())) {
                throw new CustomException(ExceptionCodeEnums.GOD_EMAIL_EXISTS);
            }
            //判断是否被取消订阅
            if(s.isCancel()) {
                throw new CustomException(ExceptionCodeEnums.GOD_EMAIL_CANCEL);
            }
        }

        Subscribe subscribe = new Subscribe();
        if (StringUtils.isNoneBlank(a.getId())) {
            subscribe = subscribeRepository.findById(a.getId()).orElse(new Subscribe());
        }
        Subscribe newSubscribe = JSON.parseObject(JSON.toJSONString(a), Subscribe.class);
        JpaUpdateUtil.copyNullProperties(subscribe, newSubscribe);
        newSubscribe.setActivityName(String.join(",", a.getActivityName()));
        newSubscribe.setUserId(userId);
        subscribeRepository.save(newSubscribe);
    }

    /**
     * 查询订阅
     *
     * @param userId
     * @return
     */
    public Object findSubscribe(String userId) {
        List<Map<String, Object>> list = subscribeRepository.findList(userId);
        list = CommonUtils.toCamelCase(list);
        list.forEach(e -> {
            e.put("activityName", e.get("activityName").toString().split(","));
        });
        return list;
    }

    /**
     * 发送订阅
     */
    public int sendSubscribe() {
        List<Subscribe> list = subscribeRepository.findAllSubscribe(CommonUtils.getThisTimeHour());
        list.forEach(e -> {
            if (e.getActivityName().contains("日记")) {
                String content = unAuthService
                        .getDoglickingDiary("0c97d296-e5b1-11ea-9d4b-00163e1e93a5");
                emailUtil.sendMail(e.getEmail(), "小破站 | 日记",
                        emailUtil.sendDiaryHtml(content, e.getGodNickName(), e.getNickName(), e.getId()));
                SubscribeRecord s = SubscribeRecord.builder().subscribeId(e.getId()).build();
                subscribeRecordRepository.save(s);
            }
        });
        return list.size();
    }


    /**
     * 取消订阅
     *
     * @param id
     * @param cancel
     */
    public void cancelSubscribe(String id, Boolean cancel) {
        Subscribe subscribe = subscribeRepository.findById(id).orElse(null);
        if (null == subscribe) {
            throw new CustomException(ExceptionCodeEnums.PARAM_ERROR);
        }
        subscribe.setCancel(cancel);
        subscribeRepository.save(subscribe);
    }

    /**
     * 启用禁用订阅
     * @param id
     * @param enabled
     */
    public void enabledSubscribe(String id, Boolean enabled) {
        Subscribe subscribe = subscribeRepository.findById(id).orElse(null);
        if (null == subscribe) {
            throw new CustomException(ExceptionCodeEnums.PARAM_ERROR);
        }
        subscribe.setEnabled(enabled);
        subscribeRepository.save(subscribe);
    }

    /**
     * 删除订阅
     * @param id
     */
    @Transactional
    public void delSubscribe(String id) {
        subscribeRepository.deleteById(id);
    }


}

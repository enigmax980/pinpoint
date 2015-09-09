/*
 * Copyright 2014 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.navercorp.pinpoint.web.dao.memory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import com.navercorp.pinpoint.web.alarm.vo.Rule;
import com.navercorp.pinpoint.web.dao.AlarmDao;

/**
 * @author minwoo.jung
 */
@Repository
public class MemoryAlarmDao implements AlarmDao {
    
    private final Map<String, Rule> alarmRule = new ConcurrentHashMap<String, Rule>();
    private final AtomicInteger ruleIdGenerator  = new AtomicInteger(); 
    
    @Override
    public String insertRule(Rule rule) {
        String ruleId = String.valueOf(ruleIdGenerator.getAndIncrement());
        rule.setRuleId(ruleId);
        alarmRule.put(ruleId, rule);
        return rule.getRuleId();
    }

    @Override
    public void deleteRule(Rule rule) {
        alarmRule.remove(rule.getRuleId());
    }

    @Override
    public void deleteRuleByUserGroupId(String userGroupId) {
        for (Entry<String, Rule> entry : alarmRule.entrySet()) {
            if (entry.getValue().getUserGroupId().equals(userGroupId)) {
                alarmRule.remove(entry.getKey());
            }
        }
    }

    @Override
    public List<Rule> selectRuleByUserGroupId(String userGroupId) {
        List<Rule> ruleList = new LinkedList<>();

        for (Entry<String, Rule> entry : alarmRule.entrySet()) {
            if (entry.getValue().getUserGroupId().equals(userGroupId)) {
                ruleList.add(entry.getValue());
            }
        }
        
        return ruleList;
    }
    
    @Override
    public List<Rule> selectRuleByApplicationId(String applicationId) {
        List<Rule> ruleList = new LinkedList<>();
        
        for (Entry<String, Rule> entry : alarmRule.entrySet()) {
            if (entry.getValue().getApplicationId().equals(applicationId)) {
                alarmRule.remove(entry.getKey());
            }
        }
        
        return ruleList;
    }

    @Override
    public void updateRule(Rule rule) {
        alarmRule.put(rule.getRuleId(), rule);
    }
}
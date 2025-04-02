package com.seasugar.netty.beans;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.seasugar.netty.dao.GroupMapper;
import com.seasugar.netty.entity.Group;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Slf4j
public class AppStaticConfig {
    private static final String USER_ID_SPLIT_REGEX = ",";

    @Autowired
    private GroupMapper groupMapper;

    // 构造器注入（推荐方式）
//    public AppStaticConfig(GroupMapper groupMapper) {
//        this.groupMapper = groupMapper;
//    }

    @Bean("GROUP_MAP")
    public Map<Long, List<String>> initGroupMap() {
        ConcurrentHashMap<Long, List<String>> map = new ConcurrentHashMap<>();
        getGroupMap(map);
        return map;
    }

    @Bean("ID_GROUP")
    public Map<Long, Group> initIdGroup() {
        ConcurrentHashMap<Long, Group> map = new ConcurrentHashMap<>();
        getIdGroupMap(map);
        return map;
    }

    /**
     * 加载群组数据并处理异常
     */
    private void getGroupMap(Map<Long, List<String>> targetMap) {
        try {
            List<Group> groups = queryGroupsFromDB();
            populateMapData(targetMap, groups);
            log.info("Group map initialized with {} entries", groups.size());
        } catch (DataAccessException e) {
            log.error("Failed to initialize group map", e);
            throw new IllegalStateException("Group data initialization failed", e);
        }
    }

    private void getIdGroupMap(Map<Long, Group> targetMap) {
        try {
            List<Group> groups = queryGroupsFromDB();
            groups.forEach(group -> {
                targetMap.put(group.getId(), group);
            });
        } catch (DataAccessException e) {
            log.error("Failed to initialize group map", e);
            throw new IllegalStateException("Group data initialization failed", e);
        }
    }

    /**
     * 数据库查询封装
     */
    private List<Group> queryGroupsFromDB() {
        return Optional.ofNullable(groupMapper.selectList(Wrappers.emptyWrapper()))
                .orElseGet(Collections::emptyList);
    }


    /**
     * 数据转换处理
     */
    private void populateMapData(Map<Long, List<String>> map, List<Group> groups) {
        groups.stream()
                .filter(Objects::nonNull)
                .forEach(group -> {
                    List<String> userIds = new ArrayList<>(splitUserIdsSafely(group.getUserIds()));
                    if (!userIds.isEmpty()) {
                        map.put(group.getId(), userIds);
                    }
                });
    }

    /**
     * 安全的字符串分割方法
     */
    private List<String> splitUserIdsSafely(String userIds) {
        return Optional.ofNullable(userIds)
                .filter(s -> !s.isEmpty())
                .map(s -> Arrays.asList(s.split(USER_ID_SPLIT_REGEX)))
                .orElse(Collections.emptyList());
    }
}

package com.seasugar.netty.beans;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.seasugar.netty.dao.GroupMapper;
import com.seasugar.netty.entity.tGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Slf4j
public class AppStaticConfig {
    private static final String USER_ID_SPLIT_REGEX = ",";
    private final GroupMapper groupMapper;

    // 构造器注入（推荐方式）
    public AppStaticConfig(GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }

    @Bean("GROUP_MAP")
    public Map<Long, List<String>> initGroupMap() {
        ConcurrentHashMap<Long, List<String>> map = new ConcurrentHashMap<>();
        loadGroupData(map);
        return map;
    }

    /**
     * 加载群组数据并处理异常
     */
    private void loadGroupData(Map<Long, List<String>> targetMap) {
        try {
            List<tGroup> groups = queryGroupsFromDB();
            populateMapData(targetMap, groups);
            log.info("Group map initialized with {} entries", groups.size());
        } catch (DataAccessException e) {
            log.error("Failed to initialize group map", e);
            throw new IllegalStateException("Group data initialization failed", e);
        }
    }

    /**
     * 数据库查询封装
     */
    private List<tGroup> queryGroupsFromDB() {
        return Optional.ofNullable(groupMapper.selectList(Wrappers.emptyWrapper()))
                .orElseGet(Collections::emptyList);
    }

    /**
     * 数据转换处理
     */
    private void populateMapData(Map<Long, List<String>> map, List<tGroup> groups) {
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

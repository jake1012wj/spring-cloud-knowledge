package com.jeesite.modules.search.mapper;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * ElasticSearch高亮配置
 */
@Slf4j
@Component
public class HighlightResultHelper {

    public <T> List<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
        List<T> results = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            if (hit != null) {
                T result = null;
                if (hit.getSourceAsString() != null && !hit.getSourceAsString().isEmpty()) {
                    result = JSONObject.parseObject(hit.getSourceAsString(), clazz);
                }
                // 高亮查询
                for (HighlightField field : hit.getHighlightFields().values()) {
                    try {
                        String highlightedValue = concat(field.getFragments());
                        PropertyUtils.setProperty(result, field.getName(), highlightedValue);
                    } catch (Exception e) {
                        log.error("设置高亮字段异常：{}", e.getMessage(), e);
                    }
                }
                results.add(result);
            }
        }
        return results;
    }

    public <T> T mapSearchHit(SearchHit searchHit, Class<T> clazz) {
        T result = null;
        if (searchHit.getSourceAsString() != null && !searchHit.getSourceAsString().isEmpty()) {
            result = JSONObject.parseObject(searchHit.getSourceAsString(), clazz);
        }
        for (HighlightField field : searchHit.getHighlightFields().values()) {
            try {
                String highlightedValue = concat(field.getFragments());
                PropertyUtils.setProperty(result, field.getName(), highlightedValue);
            } catch (Exception e) {
                log.error("设置高亮字段异常：{}", e.getMessage(), e);
            }
        }
        return result;
    }

    private String concat(Text[] texts) {
        StringBuilder sb = new StringBuilder();
        for (Text text : texts) {
            sb.append(text.string());
        }
        return sb.toString();
    }
}
//@Slf4j
//@Component
//public class HighlightResultHelper implements SearchResultMapper {
//    @Override
//    public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
//        List<T> results = new ArrayList<>();
//        for (SearchHit hit : response.getHits()) {
//            if (hit != null) {
//                T result = null;
//                if (StringUtils.hasText(hit.getSourceAsString())) {
//                    result = JSONObject.parseObject(hit.getSourceAsString(), clazz);
//                }
//                // 高亮查询
//                for (HighlightField field : hit.getHighlightFields().values()) {
//                    try {
//                        PropertyUtils.setProperty(result, field.getName(), concat(field.fragments()));
//                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//                        log.error("设置高亮字段异常：{}", e.getMessage(), e);
//                    }
//                }
//                results.add(result);
//            }
//        }
//        return new AggregatedPageImpl<T>(results, pageable, response.getHits().getTotalHits(), response
//            .getAggregations(), response.getScrollId());
//    }
//
//
//    @Override
//    public <T> T mapSearchHit(SearchHit searchHit, Class<T> clazz) {
//        List<T> results = new ArrayList<>();
//        for (HighlightField field : searchHit.getHighlightFields().values()) {
//            T result = null;
//            if (StringUtils.hasText(searchHit.getSourceAsString())) {
//                result = JSONObject.parseObject(searchHit.getSourceAsString(), clazz);
//            }
//            try {
//                PropertyUtils.setProperty(result, field.getName(), concat(field.fragments()));
//            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//                log.error("设置高亮字段异常：{}", e.getMessage(), e);
//            }
//            results.add(result);
//        }
//        return null;
//    }
//
//    private String concat(Text[] texts) {
//        StringBuilder sb = new StringBuilder();
//        for (Text text : texts) {
//            sb.append(text.toString());
//        }
//        return sb.toString();
//    }
//}


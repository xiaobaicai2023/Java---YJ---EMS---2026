package com.yunpower.system.api.factory;

import com.yunpower.system.api.RemoteDictDataService;
import com.yunpower.system.api.domain.SysCommonDictData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * 数据字典服务降级处理
 *
 * @author XIAOTONG.CAO
 */
public class RemoteDictDataFallbackFactory implements FallbackFactory<RemoteDictDataService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteDictDataFallbackFactory.class);

    @Override
    public RemoteDictDataService create(Throwable throwable) {
        log.error("数据字典服务调用失败:{}", throwable.getMessage());
        return new RemoteDictDataService() {
            @Override
            public String getDictLabel(String dictType, String dictValue, String source) {
                return null;
            }

            @Override
            public String getDictValue(String dictType, String dictValue, String source) {
                return null;
            }

            @Override
            public List<SysCommonDictData> getListByDictType(String dictType, String source) {
                return null;
            }
        };
    }
}

package com.yunpower.common.security.utils;

import java.util.Collection;
import java.util.List;
import com.alibaba.fastjson2.JSONArray;
import com.yunpower.common.core.constant.CacheConstants;
import com.yunpower.common.core.utils.SpringUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.redis.service.RedisService;
import com.yunpower.system.api.domain.SysCommonDictData;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 字典工具类
 * 
 * @author yunpower
 */
public class DictUtils
{
    /**
     * 设置字典缓存
     * 
     * @param key 参数键
     * @param dictDatas 字典数据列表
     */
    public static void setDictCache(String key, List<SysCommonDictData> dictDatas)
    {
        SpringUtils.getBean(RedisService.class).setCacheObject(getCacheKey(key), dictDatas);
    }

    /**
     * 获取字典缓存
     * 
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static List<SysCommonDictData> getDictCache(String key)
    {
        JSONArray arrayCache = SpringUtils.getBean(RedisService.class).getCacheObject(getCacheKey(key));
        if (StringUtils.isNotNull(arrayCache))
        {
            return arrayCache.toList(SysCommonDictData.class);
        }
        return null;
    }

    /**
     * 获取字典缓存
     *
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static SysCommonDictData getDictDataCache(String key,String dictLabel)
    {
        List<SysCommonDictData> list = getDictCache(key);
        if(StringUtils.isNotEmpty(list)){
            for (SysCommonDictData dict : list) {
                if (dict.getDictLabel().equals(dictLabel)) {
                    return dict;
                }
            }
        }
        return null;
    }

    /**
     * 删除指定字典缓存
     * 
     * @param key 字典键
     */
    public static void removeDictCache(String key)
    {
        SpringUtils.getBean(RedisService.class).deleteObject(getCacheKey(key));
    }

    /**
     * 清空字典缓存
     */
    public static void clearDictCache()
    {
        Collection<String> keys = SpringUtils.getBean(RedisService.class).keys(CacheConstants.SYS_DICT_KEY + "*");
        SpringUtils.getBean(RedisService.class).deleteObject(keys);
    }

    /**
     * 设置cache key
     * 
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String configKey)
    {
        return CacheConstants.SYS_DICT_KEY + configKey;
    }
}

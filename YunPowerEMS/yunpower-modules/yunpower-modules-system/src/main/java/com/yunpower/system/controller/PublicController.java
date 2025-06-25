package com.yunpower.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.yunpower.common.core.utils.GenRandomUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.security.annotation.InnerAuth;
import com.yunpower.system.api.domain.SysDept;
import com.yunpower.system.api.domain.SysStation;
import com.yunpower.system.service.IPublicService;
import com.yunpower.system.service.ISysDeptService;
import com.yunpower.system.service.ISysStationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @title: 公共方法
 * @Author: Jiajiaglam
 * @date: 2023-09-27 15:29
 * @description:
 */
@Api(tags = "G 公共方法")
@RestController
@RequestMapping("/public")
public class PublicController extends BaseController {

    @Autowired
    IPublicService publicService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysStationService stationService;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 获取用户的站点列表
     */
    @ApiOperation("获取用户的站点列表")
    @GetMapping("/getStationlist")
    public AjaxResult list(SysDept dept) {
        List<SysDept> deptList = deptService.selectDeptList(dept);

        for (SysDept item : deptList) {
            if (item.getIsCanSelect() == null || item.getIsCanSelect() == 0) {
                continue;
            }

            SysStation station = stationService.selectSysStationByDeptId(item.getId());
            if (station != null) {
                item.setStationType(station.getStationType());
            }
        }

        deptList = deptService.buildDeptTree(deptList);
        return success(deptList);
    }

    /**
     * 保存用户选择的站点（部门）
     */
    @ApiOperation("保存用户选择的站点")
    @PostMapping("/setStation")
    public AjaxResult setCurrentStation(Long deptId) {
        boolean isNext = publicService.setCurrentStation(deptId);
        return toAjax(isNext);
    }

    /**
     * 保存用户权限范围内的第一个站点，并返回结果
     * 说明：@innerAuth+AOP 实现 yunpower-api 远程接口
     */
    @ApiOperation("保存用户权限范围内的第一个站点")
    @InnerAuth
    @GetMapping("/setUserFirstStation")
    public AjaxResult setCurrentStation() {
        //这里调用getCurrentStation，如果已经有保存了就不设置了
        return success(publicService.getCurrentStation());
    }

    /**
     * 远程内部调用-获取当前用户站点信息
     */
    @ApiOperation("获取用户当前站点（部门）信息")
    @InnerAuth
    @GetMapping("/getCurrentStationInfo/inner")
    public SysStation getCurrentStationInfo() {
        return publicService.getCurrentStationInfo();
    }

    /**
     * 获取用户当前站点（部门）
     */
    @ApiOperation("获取用户当前站点")
    @GetMapping("/getStation")
    public AjaxResult getCurrentStation() {
        return success(publicService.getCurrentStation());
    }

    /**
     * 获取用户当前企业
     */
    @ApiOperation("获取用户当前企业")
    @GetMapping("/getEnterprise")
    public AjaxResult getCurrentEnterprise() {
        return success(publicService.getCurrentEnterprise());
    }

    /**
     * 生成随机字符串
     * 如果加上 @RequestParam("") 就是必填项，不填后台报错
     *
     * @param genType 生成类型：1纯字母 2纯数字 3混合（必填）
     * @param len     长度（必填）
     * @return 结果
     */
    @ApiOperation("生成随机字符串")
    @GetMapping("/genRandomKey")
    public AjaxResult genRandomKey(Integer genType, Integer len) {
        if (StringUtils.isNull(genType)) {
            return error("请输入生成类型：1纯字母/2纯数字/3混合");
        }
        if (StringUtils.isNull(len)) {
            return error("请输入要生成随机数的长度");
        }
        if (genType < 1 || genType > 3) {
            genType = 3;
        }
        if (len < 1 || len > 64) {
            return error("随机数的长度不能小于1且不能大于64位");
        }

        String code = "";
        switch (genType) {
            case 1:
                code = GenRandomUtils.GenRandomLetter(len);
                break;
            case 2:
                code = GenRandomUtils.GenRandomNumber(len);
                break;
            default:
                code = GenRandomUtils.GenRandom(len);
        }

        return success(code);
    }

    /**
     * 获取天气数据-通过当前站点位置获取
     * */
    @GetMapping("/getWeather")
    public AjaxResult getWeather(){
        SysStation station = this.getCurrentStationInfo();
        String url = StringUtils.format("https://devapi.qweather.com/v7/weather/now?location={}&key=ede0270be04c4adf9834d7b92cb312c2",StringUtils.isNotEmpty(station.getCoordinate())?station.getCoordinate():"122.13,37.42");
        ResponseEntity<JSONObject> response =restTemplate.getForEntity(url, JSONObject.class);
        if(response.getStatusCodeValue() != 200){
            return error("获取天气信息失败");
        }

        return success(response.getBody());
    }
}

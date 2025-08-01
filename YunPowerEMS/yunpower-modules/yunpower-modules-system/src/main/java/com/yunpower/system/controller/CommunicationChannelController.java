package com.yunpower.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.yunpower.common.core.utils.GenRandomUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.system.api.domain.SysDept;
import com.yunpower.system.api.domain.SysStation;
import com.yunpower.system.service.IPublicService;
import com.yunpower.system.service.ISysDeptService;
import com.yunpower.system.service.ISysStationService;
import com.yunpower.common.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yunpower.system.domain.CommunicationChannel;
import com.yunpower.system.service.ICommunicationChannelService;

/**
 * 通讯通道
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "T 通讯通道表")
@RestController
@RequestMapping("/channel")
public class CommunicationChannelController extends BaseController {
    @Autowired
    private ICommunicationChannelService communicationChannelService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysStationService stationService;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询通讯通道列表（不分页）
     */
    @ApiOperation("查询通讯通道列表（不分页）")
    @RequiresPermissions("system:channel:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(CommunicationChannel communicationChannel) {
        List<CommunicationChannel> list = communicationChannelService.selectCommunicationChannelList(communicationChannel);
        return success(list);
    }

    /**
     * 获取通讯通道详细信息
     */
    @ApiOperation("获取通讯通道详细信息")
    @RequiresPermissions("system:channel:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {

        CommunicationChannel channel = communicationChannelService.selectCommunicationChannelById(id);
        if(channel == null){
            return AjaxResult.error("未查询到通讯通道");
        }
        SysDept dept = deptService.selectDeptById(channel.getDeptId());
        if (dept != null) {
            channel.setStationName(dept.getDeptName());
        }
        return success(channel);
    }
}

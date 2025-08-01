package com.yunpower.system.controller;

import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.system.domain.SysCommonAddress;
import com.yunpower.system.service.ISysCommonAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 全国四级行政区
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "G 全国四级行政区表")
@RestController
@RequestMapping("/address")
public class SysCommonAddressController extends BaseController {
    @Autowired
    private ISysCommonAddressService sysCommonAddressService;

    /**
     * 查询全国四级行政区列表
     */
    @ApiOperation("查询全国四级行政区列表")
    @GetMapping("/list")
    public TableDataInfo list(SysCommonAddress sysCommonAddress) {
        startPage();
        List<SysCommonAddress> list = sysCommonAddressService.selectSysCommonAddressList(sysCommonAddress);
        return getDataTable(list);
    }

    /**
     * 查询全国四级行政区列表（不分页）
     */
    @ApiOperation("查询全国四级行政区列表（不分页）")
    @GetMapping("/listAll")
    public AjaxResult listAll(SysCommonAddress sysCommonAddress) {
        if (sysCommonAddress.getParentId() == null) {
            return error("请输入ParentId");
        }

        List<SysCommonAddress> list = sysCommonAddressService.selectSysCommonAddressList(sysCommonAddress);
        return success(list);
    }

    /**
     * 导出全国四级行政区列表
     */
    @ApiOperation("导出全国四级行政区列表")
    @RequiresPermissions("system:address:export")
    @Log(title = "全国四级行政区", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysCommonAddress sysCommonAddress) {
        List<SysCommonAddress> list = sysCommonAddressService.selectSysCommonAddressList(sysCommonAddress);
        ExcelUtil<SysCommonAddress> util = new ExcelUtil<SysCommonAddress>(SysCommonAddress.class);
        util.exportExcel(response, list, "全国四级行政区数据");
    }

    /**
     * 获取全国四级行政区详细信息
     */
    @ApiOperation("获取全国四级行政区详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(sysCommonAddressService.selectSysCommonAddressById(id));
    }

    /**
     * 获取全国四级行政区名称（批量）
     */
    @ApiOperation("获取全国四级行政区名称（批量）")
    @GetMapping(value = "/getNames/{ids}")
    public AjaxResult getNames(@PathVariable("ids") Long[] ids) {
        List<String> builder = new ArrayList<>();
        for (Long id : ids) {
            SysCommonAddress address = sysCommonAddressService.selectSysCommonAddressById(id);
            if (address != null) {
                builder.add(address.getName());
            }
        }
        return success(String.join("/", builder));
    }

    /**
     * 新增全国四级行政区
     */
    @ApiOperation("新增全国四级行政区")
    @RequiresPermissions("system:address:add")
    @Log(title = "全国四级行政区", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysCommonAddress sysCommonAddress) {
        return toAjax(sysCommonAddressService.insertSysCommonAddress(sysCommonAddress));
    }

    /**
     * 修改全国四级行政区
     */
    @ApiOperation("修改全国四级行政区")
    @RequiresPermissions("system:address:edit")
    @Log(title = "全国四级行政区", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysCommonAddress sysCommonAddress) {
        return toAjax(sysCommonAddressService.updateSysCommonAddress(sysCommonAddress));
    }

    /**
     * 删除全国四级行政区
     */
    @ApiOperation("删除全国四级行政区")
    @RequiresPermissions("system:address:remove")
    @Log(title = "全国四级行政区", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysCommonAddressService.deleteSysCommonAddressByIds(ids));
    }

    /**
     * 根据给定的省、市、县ID获取对应的地址名称。
     *
     * @param provinceId 省份ID
     * @param cityId 城市ID
     * @param countyId 县/区ID
     * @return 返回地址名称，以字符串形式
     */
    @GetMapping("/getName/{provinceId}/{cityId}/{countyId}")
    public String getAddressName(@PathVariable("provinceId") Long provinceId, @PathVariable("cityId") Long cityId, @PathVariable("countyId") Long countyId){
        return sysCommonAddressService.getAddressName(provinceId, cityId, countyId);
    }
}

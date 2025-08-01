package com.yunpower.system.controller;

import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.InnerAuth;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.api.domain.SysCommonDictData;
import com.yunpower.system.service.ISysCommonDictDataService;
import com.yunpower.system.service.ISysCommonDictService;
import com.yunpower.common.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典信息
 *
 * @author yunpower
 */
@Api(tags = "S 数据字典信息表")
@RestController
@RequestMapping("/dict/data")
public class SysCommonDictDataController extends BaseController {
    @Autowired
    private ISysCommonDictDataService dictDataService;

    @Autowired
    private ISysCommonDictService dictTypeService;

    @ApiOperation("获取字典信息列表")
    @RequiresPermissions("system:dict:list")
    @GetMapping("/list")
    public TableDataInfo list(SysCommonDictData dictData) {
        startPage();
        List<SysCommonDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    @ApiOperation("导出字典信息")
    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:dict:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysCommonDictData dictData) {
        List<SysCommonDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysCommonDictData> util = new ExcelUtil<SysCommonDictData>(SysCommonDictData.class);
        util.exportExcel(response, list, "字典数据");
    }

    /**
     * 查询字典数据详细
     */
    @ApiOperation("根据字典编码获取详细信息")
    @RequiresPermissions("system:dict:query")
    @GetMapping(value = "/{dictDataId}")
    public AjaxResult getInfo(@PathVariable Long dictDataId) {
        return success(dictDataService.selectDictDataById(dictDataId));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @ApiOperation("根据字典类型查询字典数据信息")
    @GetMapping(value = "/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType) {
        List<SysCommonDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (StringUtils.isNull(data)) {
            data = new ArrayList<SysCommonDictData>();
        }
        return success(data);
    }

    /**
     * 根据字典类型、字典值查询信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 结果
     */
    @ApiOperation("根据字典类型、字典值查询多级信息")
    @GetMapping(value = "/dictDataMultiLevel")
    public AjaxResult dictDataBy(String dictType, String dictValue) {
        SysCommonDictData dictData = dictDataService.selectDictDataBy(dictType, dictValue);
        return success(dictData == null ? 0 : dictData.getIsMultiLevel());
    }

    /**
     * 新增字典类型
     */
    @ApiOperation("新增字典类型")
    @RequiresPermissions("system:dict:add")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysCommonDictData dict) {
        dict.setCreateBy(SecurityUtils.getUsername());
        return toAjax(dictDataService.insertDictData(dict));
    }

    /**
     * 修改保存字典类型
     */
    @ApiOperation("修改字典类型")
    @RequiresPermissions("system:dict:edit")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysCommonDictData dict) {
        dict.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(dictDataService.updateDictData(dict));
    }

    /**
     * 修改通用字典-数据状态
     */
    @ApiOperation("修改通用字典-数据状态")
    @RequiresPermissions("system:data:state")
    @Log(title = "通用字典-数据", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(dictDataService.updateSysCommonDictDataState(id, state));
    }

    /**
     * 删除字典类型
     */
    @ApiOperation("删除字典类型")
    @RequiresPermissions("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public AjaxResult remove(@PathVariable Long[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
        return success();
    }

    /**
     * 远程内部调用-根据字典类型和字典键值查询字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @return 字典标签
     */
    @InnerAuth
    @GetMapping("/dictLabel/{dictType}/{dictValue}")
    public String getDictLabel(@PathVariable("dictType") String dictType, @PathVariable("dictValue") String dictValue) {
        return dictDataService.selectDictLabel(dictType, dictValue);
    }

    /**
     * 远程内部调用-根据字典类型和字典标签查询字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @return 字典值
     */
    @InnerAuth
    @GetMapping("/dictValue/{dictType}/{dictLabel}")
    public String getDictValue(@PathVariable("dictType") String dictType, @PathVariable("dictLabel") String dictLabel) {
        return dictDataService.selectDictValue(dictType, dictLabel);
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @InnerAuth
    @GetMapping("/list/{dictType}")
    public List<SysCommonDictData> selectDictDataByType(@PathVariable("dictType") String dictType) {
        return dictTypeService.selectDictDataByType(dictType);
    }
}

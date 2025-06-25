package com.yunpower.system.controller;

import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.InnerAuth;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.system.api.domain.SysDept;
import com.yunpower.system.api.domain.SysStation;
import com.yunpower.system.domain.DashboardConfig;
import com.yunpower.system.domain.SysCommonEnterprise;
import com.yunpower.system.service.IDashboardConfigService;
import com.yunpower.system.service.ISysCommonEnterpriseService;
import com.yunpower.system.service.ISysDeptService;
import com.yunpower.system.service.ISysStationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 仪表盘配置
 *
 * @author yunpower
 * @date 2024-05-30
 */
@Api(tags = "D 仪表盘配置表")
@RestController
@RequestMapping("/dashboard/config")
public class DashboardConfigController extends BaseController {
	@Autowired
	private IDashboardConfigService dashboardConfigService;

	@Autowired
	private ISysCommonEnterpriseService sysCommonEnterpriseService;

	@Autowired
	private ISysDeptService deptService;

	@Autowired
	private ISysStationService stationService;

	/**
	 * 查询仪表盘配置列表
	 */
	@ApiOperation("查询仪表盘配置列表")
	@RequiresPermissions("system:dashboard-config:list")
	@GetMapping("/list")
	public TableDataInfo list(DashboardConfig dashboardConfig) {
		startPage();
		List<DashboardConfig> list = dashboardConfigService.selectDashboardConfigList(dashboardConfig);
		//大屏列表页 需要获取公司、分组 站点名称
		if (dashboardConfig.getPageType() == 1) {
			if (StringUtils.isNotEmpty(list)) {
				list.forEach(this::handleDashboardConfig);
			}
		}
		return getDataTable(list);
	}

	/**
	 * 导出仪表盘配置列表
	 */
	@ApiOperation("导出仪表盘配置列表")
	@RequiresPermissions("system:dashboard-config:export")
	@Log(title = "仪表盘配置", businessType = BusinessType.EXPORT)
	@PostMapping("/export")
	public void export(HttpServletResponse response, DashboardConfig dashboardConfig) {
		List<DashboardConfig> list = dashboardConfigService.selectDashboardConfigList(dashboardConfig);
		ExcelUtil<DashboardConfig> util = new ExcelUtil<DashboardConfig>(DashboardConfig.class);
		util.exportExcel(response, list, "仪表盘配置数据");
	}

	/**
	 * 获取仪表盘配置详细信息-id
	 */
	@ApiOperation("获取仪表盘配置详细信息")
	@RequiresPermissions("system:dashboard-config:query")
	@GetMapping(value = "/getInfo/{id}")
	public AjaxResult getInfo(@PathVariable("id") Long pageKey) {
		DashboardConfig config = dashboardConfigService.selectDashboardConfigById(pageKey);
		if (config != null) {
			handleDashboardConfig(config);
		}
		return success(config);
	}

	private void handleDashboardConfig(DashboardConfig config) {
		//企业信息
		SysCommonEnterprise enterprise = sysCommonEnterpriseService.selectSysCommonEnterpriseById(config.getEntId());
		if (enterprise != null) {
			config.setEntName(enterprise.getEntName());
		}
		//站点
		SysDept dept = deptService.selectDeptById(config.getDeptId());
		if (dept != null) {
			SysStation station = stationService.selectSysStationByDeptId(dept.getId());
			if (station != null) {
				config.setStationType(station.getStationType());
			}
			config.setStationName(dept.getDeptName());
			//分组
			SysDept group = deptService.selectDeptById(dept.getParentId());
			if (group != null) {
				config.setGroupName(group.getDeptName());
			}
		}
		if (config.getPageType() == 1 && StringUtils.isNotEmpty(config.getPageKey())) {
			String[] split = config.getPageKey().split("-");
			if (split.length == 2) {
				config.setPageTemplate(split[1]);
			}
		}
	}

	/**
	 * 获取仪表盘配置详细信息-pageKey
	 */
	@ApiOperation("获取仪表盘配置详细信息")
	@RequiresPermissions("system:dashboard-config:query")
	@GetMapping(value = "/{pageKey}")
	public AjaxResult getInfo(@PathVariable("pageKey") String pageKey) {
		return success(dashboardConfigService.selectDashboardConfigByPageKey(pageKey));
	}

	/**
	 * 新增仪表盘配置
	 */
	@ApiOperation("新增仪表盘配置")
	@RequiresPermissions("system:dashboard-config:add")
	@Log(title = "仪表盘配置", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody DashboardConfig dashboardConfig) {
		return toAjax(dashboardConfigService.insertDashboardConfig(dashboardConfig));
	}

	/**
	 * 修改仪表盘配置
	 */
	@ApiOperation("修改仪表盘配置")
	@RequiresPermissions("system:dashboard-config:edit")
	@Log(title = "仪表盘配置", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody DashboardConfig dashboardConfig) {
		return toAjax(dashboardConfigService.updateDashboardConfig(dashboardConfig));
	}

	/**
	 * 修改仪表盘配置状态
	 */
	@ApiOperation("修改仪表盘配置状态")
	@RequiresPermissions("system:dashboard-config:state")
	@Log(title = "仪表盘配置", businessType = BusinessType.UPDATE)
	@PutMapping("/changeStatus/{id}/{state}")
	public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
		return toAjax(dashboardConfigService.updateDashboardConfigState(id, state));
	}

	/**
	 * 删除仪表盘配置
	 */
	@ApiOperation("删除仪表盘配置")
	@RequiresPermissions("system:dashboard-config:remove")
	@Log(title = "仪表盘配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
	public AjaxResult remove(@PathVariable Long[] ids) {
		return toAjax(dashboardConfigService.deleteDashboardConfigByIds(ids));
	}

	/**
	 * datav项目调用-获取大屏配置信息
	 * 说明：@innerAuth+AOP 实现 api 远程接口
	 */
	@ApiOperation("datav项目调用-获取大屏配置信息")
	@InnerAuth
	@GetMapping(value = "/station/inner/{stationId}/{isPre}")
	public Map<String, Object> getInfoByStationId(@PathVariable("stationId") Long stationId, @PathVariable("isPre") Integer isPre) {
		return dashboardConfigService.selectDashboardConfigByStationId(stationId, isPre);
	}
}

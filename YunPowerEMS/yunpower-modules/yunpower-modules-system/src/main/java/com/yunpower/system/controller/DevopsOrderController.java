package com.yunpower.system.controller;

import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.system.api.domain.SysDept;
import com.yunpower.system.domain.*;
import com.yunpower.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 工单管理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "G 工单管理表")
@RestController
@RequestMapping("/order")
public class DevopsOrderController extends BaseController {
	@Autowired
	private IDevopsOrderService devopsOrderService;

	@Autowired
	private IMonitorDeviceService monitorDeviceService;

	@Autowired
	private ISysDeptService deptService;

	@Autowired
	private IMonitorDeviceVarService monitorDeviceVarService;

	/**
	 * 查询工单管理列表
	 */
	@ApiOperation("查询工单管理列表")
	@RequiresPermissions("system:order:list")
	@GetMapping("/list")
	public TableDataInfo list(DevopsOrder devopsOrder) {
		startPage();
		List<DevopsOrder> list = devopsOrderService.selectDevopsOrderList(devopsOrder);
		for (DevopsOrder item : list) {
			packageInfo(item);
		}
		return getDataTable(list);
	}

	private void packageInfo(DevopsOrder item) {
		//站点（部门）&设备
		SysDept dept = deptService.selectDeptById(item.getStationId());
		if (dept != null) {
			item.setStationName("[" + dept.getDeptName() + "]");
		}

		//站点&设备
		MonitorDevice device = monitorDeviceService.selectMonitorDeviceById(item.getDeviceId());
		if (device == null) {
			return;
		}
		item.setStationName(item.getStationName() + device.getDeviceName());

		//变量名称
		MonitorDeviceVar deviceVar = monitorDeviceVarService.selectMonitorDeviceVarById(1000l);
		if (deviceVar == null) {
			return;
		}

		item.setAlarmName("[" + device.getDeviceName() + "，" + deviceVar.getVarName() + "]");
		item.setAlarmContent("报警事件描述");
	}

	/**
	 * 导出工单管理列表
	 */
	@ApiOperation("导出工单管理列表")
	@RequiresPermissions("system:order:export")
	@Log(title = "工单管理", businessType = BusinessType.EXPORT)
	@PostMapping("/export")
	public void export(HttpServletResponse response, DevopsOrder devopsOrder) {
		List<DevopsOrder> list = devopsOrderService.selectDevopsOrderList(devopsOrder);
		ExcelUtil<DevopsOrder> util = new ExcelUtil<DevopsOrder>(DevopsOrder.class);
		util.exportExcel(response, list, "工单管理数据");
	}

	/**
	 * 获取工单管理详细信息
	 */
	@ApiOperation("获取工单管理详细信息")
	@RequiresPermissions("system:order:query")
	@GetMapping(value = "/{id}")
	public AjaxResult getInfo(@PathVariable("id") Long id) {
		DevopsOrder order = devopsOrderService.selectDevopsOrderById(id);
		packageInfo(order);
		return success(order);
	}

	/**
	 * 新增工单管理
	 */
	@ApiOperation("新增工单管理")
	@RequiresPermissions("system:order:add")
	@Log(title = "工单管理", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@RequestBody DevopsOrder devopsOrder) {
		if (StringUtils.longIsBlack0(devopsOrder.getStationId())) {
			return error("请选择站点");
		}
		if (StringUtils.longIsBlack0(devopsOrder.getDeviceId())) {
			return error("请选择设备");
		}
		if (devopsOrder.getAlarmId() == null) {
			devopsOrder.setAlarmId(0L);
		}
		devopsOrder.setUseMinutes(0);
		devopsOrder.setTimeoutStatus(0);
		devopsOrder.setStatus(0);
		return toAjax(devopsOrderService.insertDevopsOrder(devopsOrder));
	}

	/**
	 * 修改工单管理
	 */
	@ApiOperation("修改工单管理")
	@RequiresPermissions("system:order:edit")
	@Log(title = "工单管理", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@RequestBody DevopsOrder devopsOrder) {
		if (StringUtils.longIsBlack0(devopsOrder.getStationId())) {
			return error("请选择站点");
		}
		if (StringUtils.longIsBlack0(devopsOrder.getDeviceId())) {
			return error("请选择设备");
		}
		return toAjax(devopsOrderService.updateDevopsOrder(devopsOrder));
	}

	/**
	 * 修改工单管理状态
	 */
	@ApiOperation("修改工单管理状态")
	@RequiresPermissions("system:order:state")
	@Log(title = "工单管理", businessType = BusinessType.UPDATE)
	@PutMapping("/changeStatus/{id}/{state}")
	public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
		return toAjax(devopsOrderService.updateDevopsOrderState(new DevopsOrder(), id, state));
	}

	/**
	 * 删除工单管理
	 */
	@ApiOperation("删除工单管理")
	@RequiresPermissions("system:order:remove")
	@Log(title = "工单管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
	public AjaxResult remove(@PathVariable Long[] ids) {
		return toAjax(devopsOrderService.deleteDevopsOrderByIds(new DevopsOrder(), ids));
	}
}

package com.yunpower.system.controller;

import com.alibaba.fastjson2.JSON;
import com.yunpower.common.core.DoubleUtils;
import com.yunpower.common.core.constant.CacheConstants;
import com.yunpower.common.core.enums.vo.EnumSOTVO;
import com.yunpower.common.core.utils.FormatUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.utils.bean.BeanUtils;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.redis.service.RedisService;
import com.yunpower.common.security.annotation.InnerAuth;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.api.domain.FeignMonitorDeviceVar;
import com.yunpower.system.api.domain.SysDept;
import com.yunpower.system.api.domain.SysStation;
import com.yunpower.system.domain.CommunicationChannel;
import com.yunpower.system.domain.MonitorDevice;
import com.yunpower.system.domain.MonitorDeviceVar;
import com.yunpower.system.domain.MonitorDeviceVarMap;
import com.yunpower.system.domain.vo.PVVo;
import com.yunpower.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 监控设备变量
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "J 监控设备变量表")
@RestController
@RequestMapping("/device-var")
public class MonitorDeviceVarController extends BaseController {
    @Autowired
    private ICommunicationChannelService communicationChannelService;

    @Autowired
    private IMonitorDeviceVarService monitorDeviceVarService;

    @Autowired
    private IMonitorDeviceVarMapService deviceVarMapService;

    @Autowired
    private IMonitorDeviceService monitorDeviceService;

    @Autowired
    private ISysCommonConfigService configService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ISysStationService stationService;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询监控设备变量列表
     */
    @ApiOperation("查询监控设备变量列表")
    @RequiresPermissions("system:device-var:list")
    @GetMapping("/list")
    public TableDataInfo list(MonitorDeviceVar monitorDeviceVar) {
        startPage();
        List<MonitorDeviceVar> list = monitorDeviceVarService.selectMonitorDeviceVarList(monitorDeviceVar);
        packageList(list);
        return getDataTable(list);
    }

    /**
     * 查询监控设备变量列表（不分页）
     */
    @ApiOperation("查询监控设备变量列表（不分页）")
    @RequiresPermissions("system:device-var:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(MonitorDeviceVar monitorDeviceVar) {
        List<MonitorDeviceVar> list = monitorDeviceVarService.selectMonitorDeviceVarList(monitorDeviceVar);
        packageList(list);
        return success(list);
    }

    //region 公共：封装设备具体信息
    private void packageList(List<MonitorDeviceVar> list) {
        list.forEach(this::packageInfo);
    }

    private void packageInfo(MonitorDeviceVar item) {
        //通道名称
        CommunicationChannel channel = communicationChannelService.selectCommunicationChannelById(item.getChannelId());
        item.setChannelName(channel == null ? "" : channel.getChannelName());

        //设备名称
        MonitorDevice device = monitorDeviceService.selectMonitorDeviceById(item.getDeviceId());
        item.setDeviceName(device == null ? "" : device.getDeviceName());

        //变量属性
        if (StringUtils.isNotEmpty(item.getVarMapSn())) {
            MonitorDeviceVarMap varMap = deviceVarMapService.selectMonitorDeviceVarMapByIndexSn(item.getVarMapSn());
            item.setVarMapName(varMap == null ? "" : varMap.getIndexName());
        }
    }
    //endregion

    //region 获取第一部分显示变量

    /**
     * 获取第一部分显示变量
     */
    @ApiOperation("获取第一部分显示变量")
    @RequiresPermissions("system:device-var:list")
    @GetMapping("/configRunTimeList")
    public AjaxResult getConfigRunTimeList(String deviceSn, Integer stationType) {
        if (StringUtils.isEmpty(deviceSn)) {
            return error("请选择设备");
        }

        // 获取配置，决定显示那些变量
        String configValue = configService.selectConfigByKey("sys.runtime.monitor_var." + stationType);
        if (StringUtils.isEmpty(configValue)) {
            return success();
        }

        // 获取所有的变量
        MonitorDeviceVar deviceVar = new MonitorDeviceVar();
        deviceVar.setDeviceSn(deviceSn);
        deviceVar.setStopFlag(0);
        List<MonitorDeviceVar> varList = monitorDeviceVarService.selectMonitorDeviceVarList(deviceVar);
        if (varList.isEmpty()) {
            return success();
        }

        String[] configs = configValue.split(",");
        List<String> configList = Arrays.asList(configs);
        List<EnumSOTVO> result = new ArrayList<>();

        for (MonitorDeviceVar item : varList) {
            if (configList.contains(item.getVarMapSn())) {
                MonitorDeviceVarMap varMap = deviceVarMapService.selectMonitorDeviceVarMapByIndexSn(item.getVarMapSn());
                if (StringUtils.isNull(varMap)) {
                    continue;
                }

                EnumSOTVO vo = null;
                Object jsono = redisService.getCacheObject(getVariableCacheKey(item.getVarSn()));
                String json=null;
                if(!Objects.isNull(jsono)) {
                	json=jsono.toString();                	
                }                               
                if (!StringUtils.isEmpty(json)) {
                    vo = JSON.parseObject(json, EnumSOTVO.class);
                }
                if (vo == null) {
                    vo = new EnumSOTVO();
                    vo.setValue("--");
                    vo.setTime("--");
                } else {
                    vo.setValue(FormatUtils.fmt2point(Float.parseFloat(vo.getValue().toString())));
                }
                vo.setName(item.getVarName());
                vo.setUnit(item.getUnit());
                result.add(vo);
            }
        }

        return success(result);
    }
    //endregion

    //region 获取第二部分显示变量（实时数据）

    /**
     * 获取第二部分显示变量（实时数据）
     */
    @ApiOperation("获取第二部分显示变量（实时数据）")
    @RequiresPermissions("system:device-var:list")
    @GetMapping("/runTimeList")
    public AjaxResult getrunTimeList(String deviceSn, Integer stationType) {
        if (StringUtils.isEmpty(deviceSn)) {
            return error("请选择设备");
        }
        // 获取所有的变量
        MonitorDeviceVar deviceVar = new MonitorDeviceVar();
        deviceVar.setDeviceSn(deviceSn);
        deviceVar.setStationType(stationType);
        deviceVar.setStopFlag(0);
        List<MonitorDeviceVar> varList = monitorDeviceVarService.selectMonitorDeviceVarList(deviceVar);
        if (varList.isEmpty()) {
            return success();
        }

        // 保存结果
        List<EnumSOTVO> result = new ArrayList<>();

        for (MonitorDeviceVar item : varList) {
            EnumSOTVO vo = null;
            Object jsono = redisService.getCacheObject(getVariableCacheKey(item.getVarSn()));
            String json=null;
            if(!Objects.isNull(jsono)) {
            	json=jsono.toString();                	
            }
            if (!StringUtils.isEmpty(json)) {
                vo = JSON.parseObject(json, EnumSOTVO.class);
            }
            if (vo == null) {
                vo = new EnumSOTVO();
                vo.setValue("--");
                vo.setTime("--");
            } else {
                double _value = DoubleUtils.stringToDouble(vo.getValue().toString());
                if (item.getVarType() == 1) {
                    //模拟量
                    vo.setValue(_value);
                } else {
                    //状态量
                    vo.setValue(_value == 0 ? "投入" : "投出");
                }
                vo.setVarType(item.getVarType().toString());
            }
            vo.setName(item.getVarName());
            vo.setUnit(item.getUnit());
            result.add(vo);
        }

        return success(result);
    }
    //endregion

    //region 获取【组串】电流电压功率（光伏）

    /**
     * 获取【组串】电流电压功率（光伏）
     */
    @ApiOperation("获取【组串】电流电压功率（光伏）")
    @RequiresPermissions("system:device-var:query")
    @GetMapping(value = "/getPVPowerInfo/{deviceId}")
    public AjaxResult getPVPowerInfo(@PathVariable("deviceId") Long deviceId) {
        //取出组串电流(pv_i_)
        MonitorDeviceVar deviceVar = new MonitorDeviceVar();
        deviceVar.setDeviceId(deviceId);
        deviceVar.setStopFlag(0);

        Map<String, Object> map = new HashMap<>();
        map.put("varMapSn", "pv_i_");
        deviceVar.setParams(map);
        List<MonitorDeviceVar> pviList = monitorDeviceVarService.selectMonitorDeviceVarList(deviceVar);

        //取出组串电压(pv_u_)
        map.put("varMapSn", "pv_u_");
        deviceVar.setParams(map);
        List<MonitorDeviceVar> pvuList = monitorDeviceVarService.selectMonitorDeviceVarList(deviceVar);

        //取出组串功率(pv_p_)
        map.put("varMapSn", "pv_p_");
        deviceVar.setParams(map);
        List<MonitorDeviceVar> pvpList = monitorDeviceVarService.selectMonitorDeviceVarList(deviceVar);

        List<PVVo> result = new ArrayList<>();
        for (int i = 1; i <= pviList.size(); i++) {
            String sufix = StringUtils.padLeft(i, 2);

            PVVo vo = new PVVo();
            vo.setName("组串" + sufix);

            //获取【电流】变量编码
            List<MonitorDeviceVar> filter = pviList.stream().filter(s -> s.getVarMapSn().equals("pv_i_" + sufix)).collect(Collectors.toList());
            if (filter.size() > 0) {
                String varsn = filter.get(0).getVarSn();
                vo.setI(""); //从缓存中获取
            } else {
                vo.setI("-1");
            }

            //获取【电压】变量编码
            filter = pvuList.stream().filter(s -> s.getVarMapSn().equals("pv_u_" + sufix)).collect(Collectors.toList());
            if (filter.size() > 0) {
                String varsn = filter.get(0).getVarSn();
                vo.setU("");
            } else {
                vo.setU("-1");
            }

            //获取【功率】变量编码
            filter = pvpList.stream().filter(s -> s.getVarMapSn().equals("pv_p_" + sufix)).collect(Collectors.toList());
            if (filter.size() > 0) {
                String varsn = filter.get(0).getVarSn();
                vo.setW("");
                vo.setTime("");
            } else {
                vo.setW("-1");
            }

            result.add(vo);
        }
        return success(result);
    }
    //endregion

    //region 获取【交流】电流电压频率（光伏）

    /**
     * 获取【交流】电流电压频率（光伏）
     */
    @ApiOperation("获取【交流】电流电压频率（光伏）")
    @RequiresPermissions("system:device-var:query")
    @GetMapping(value = "/getPVElectricInfo/{deviceId}")
    public AjaxResult getPVElectricInfo(@PathVariable("deviceId") Long deviceId) {
        MonitorDevice device = monitorDeviceService.selectMonitorDeviceById(deviceId);
        if (StringUtils.isNull(device)) {
            return success();
        }

        //另一种方法，拼接变量编码：var_sn = device_sn + var_map_sn
        List<PVVo> result = new ArrayList<>();

        //A相
        PVVo voA = new PVVo();
        voA.setName("A");

        String varSn = device.getDeviceSn() + "_IA";
        voA.setI("");

        varSn = device.getDeviceSn() + "_UA";
        voA.setU("");

        result.add(voA);

        //B相
        PVVo voB = new PVVo();
        voB.setName("B");

        varSn = device.getDeviceSn() + "_IB";
        voB.setI("");

        varSn = device.getDeviceSn() + "_UB";
        voB.setU("");

        result.add(voB);

        //C相
        PVVo voC = new PVVo();
        voC.setName("C");

        varSn = device.getDeviceSn() + "_IC";
        voC.setI("");

        varSn = device.getDeviceSn() + "_UC";
        voC.setU("");

        result.add(voC);

        return success(result);
    }
    //endregion

    //region 获取【温度】数据

    /**
     * 获取【温度】数据
     */
    @ApiOperation("获取【温度】数据")
    @RequiresPermissions("system:device-var:query")
    @GetMapping(value = "/getTemperatureInfo/{deviceId}/{type}")
    public AjaxResult getTemperatureInfo(@PathVariable("deviceId") Long deviceId, @PathVariable("type") Integer type) {
        MonitorDeviceVar deviceVar = new MonitorDeviceVar();
        deviceVar.setDeviceId(deviceId);
        deviceVar.setUnit("℃"); //温度单位
        deviceVar.setStopFlag(0);
        List<MonitorDeviceVar> tempList = monitorDeviceVarService.selectMonitorDeviceVarList(deviceVar);

        List<EnumSOTVO> result = new ArrayList<>();
        for (MonitorDeviceVar item : tempList) {
            EnumSOTVO vo = new EnumSOTVO();
            vo.setKey(item.getVarName());
            vo.setValue(""); //从缓存中获取
            vo.setTime("");
            result.add(vo);
        }

        return success(result);
    }
    //endregion

    /**
     * 【导出】监控设备变量列表
     * 注意：变量编码不可修改，不可新增，但可以删除
     */
    @ApiOperation("【导出】监控设备变量列表")
    @RequiresPermissions("system:device-var:export")
    @Log(title = "监控设备变量", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MonitorDeviceVar monitorDeviceVar) {
        List<MonitorDeviceVar> list = monitorDeviceVarService.selectMonitorDeviceVarList(monitorDeviceVar);
        packageList(list);
        ExcelUtil<MonitorDeviceVar> util = new ExcelUtil<MonitorDeviceVar>(MonitorDeviceVar.class);
        util.exportExcel(response, list, "设备变量数据");
    }

    /**
     * 【导入】监控设备变量列表
     */
    @ApiOperation("【导入】监控设备变量列表")
    @Log(title = "监控设备变量", businessType = BusinessType.IMPORT)
    @RequiresPermissions("system:device-var:import")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<MonitorDeviceVar> util = new ExcelUtil<MonitorDeviceVar>(MonitorDeviceVar.class);
        List<MonitorDeviceVar> deviceVarList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = monitorDeviceVarService.importDevice(deviceVarList, updateSupport, operName);
        return success(message);
    }

    /**
     * 获取监控设备变量详细信息
     */
    @ApiOperation("获取监控设备变量详细信息")
    @RequiresPermissions("system:device-var:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        MonitorDeviceVar info = monitorDeviceVarService.selectMonitorDeviceVarById(id);
        if (info != null) {
            //获取站点名称
            SysDept dept = deptService.selectDeptById(info.getDeptId());
            info.setStationName(dept == null ? "" : dept.getDeptName());
            packageInfo(info);
        }
        return success(info);
    }

    /**
     * 新增监控设备变量
     */
    @ApiOperation("新增监控设备变量")
    @RequiresPermissions("system:device-var:add")
    @Log(title = "监控设备变量", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MonitorDeviceVar monitorDeviceVar) {

        //region 测点数量是否超上限
        Long deptId = publicService.getCurrentStation();
        SysStation station = stationService.selectSysStationByDeptId(deptId);
        if (StringUtils.isNull(station)) {
            return error("当前站点异常");
        }
        if (station.getVarLimit() > 0) {
            int count = monitorDeviceVarService.selectDeviceVarCountByDeptId(deptId);
            if (count >= station.getVarLimit()) {
                return error("当前站点测点数量已超上限");
            }
        }
        //endregion

        if (StringUtils.longIsBlack0(monitorDeviceVar.getChannelId())) {
            return error("请选择通道");
        }

        if (StringUtils.longIsBlack0(monitorDeviceVar.getDeviceId())) {
            return error("请选择监控设备");
        }

        //if (StringUtils.isEmpty(monitorDeviceVar.getVarMapSn())) {
        //    return error("请选择变量属性");
        //}
        //if (StringUtils.isEmpty(monitorDeviceVar.getUnit())) {
        //    return error("请填写变量单位");
        //}

        if (StringUtils.intIsBlack0(monitorDeviceVar.getDataType())) {
            return error("请选择数据类型");
        }

        if (StringUtils.isNull(monitorDeviceVar.getSaveCycle())) {
            return error("请选择存盘周期");
        }

        MonitorDevice monitorDevice = monitorDeviceService.selectMonitorDeviceById(monitorDeviceVar.getDeviceId());
        if (monitorDevice == null || monitorDevice.getStopFlag() == 1) {
            return error("监控设备无效，请先配置通道");
        }
        monitorDeviceVar.setStationType(monitorDevice.getStationType());
        monitorDeviceVar.setComDeviceId(monitorDevice.getComDeviceId());
        monitorDeviceVar.setComDeviceSn(monitorDevice.getComDeviceSn());

        //通道编码
        CommunicationChannel channel = communicationChannelService.selectCommunicationChannelById(monitorDeviceVar.getChannelId());
        if (StringUtils.isNull(channel)) {
            return error("请先配置通道");
        }
        monitorDeviceVar.setChannelSn(channel.getChannelSn());

        //region 同一管理机上，IO型变量信息体地址唯一
        if (monitorDeviceVar.getOrigin() == 1 && monitorDeviceVar.getMessageAddress() > 0) {
            if (!monitorDeviceVarService.checkMessageAddressOnly(monitorDeviceVar)) {
                return error("信息体地址重复");
            }
        }
        //endregion

        //前端选择-系统生成
        if ("sys".equals(monitorDeviceVar.getVarSn())) {
            //获取最后一条记录
            int maxSuffix = 0;
            monitorDeviceVar.setDeptId(publicService.getCurrentStation());
            MonitorDeviceVar last = monitorDeviceVarService.selectLastMonitorDeviceVar(monitorDeviceVar);
            if (last != null) {
                maxSuffix = StringUtils.getMaxSuffix(last.getVarSn(), maxSuffix);
            }
            //规则：设备编号+索引编码+2位随机
            monitorDeviceVar.setVarSn(StringUtils.generateVarSn(monitorDeviceVar.getVarMapSn(), monitorDeviceVar.getDeviceSn(), ++maxSuffix, false));
        } else {
            //场景：大屏配置的哪些固定变量就需要这样设置
            monitorDeviceVar.setVarSn(station.getStationSn() + "_" + monitorDeviceVar.getVarSn());
        }

        //region //默认值
        if (StringUtils.isNull(monitorDeviceVar.getRegisterIndex())) {
            monitorDeviceVar.setRegisterIndex(0);
        }
        if (StringUtils.isNull(monitorDeviceVar.getRegisterAddress())) {
            monitorDeviceVar.setRegisterAddress(0);
        }
        if (StringUtils.isNull(monitorDeviceVar.getMessageAddress())) {
            monitorDeviceVar.setMessageAddress(0);
        }
        //内存型变量 清空-信息地地址
        if (monitorDeviceVar.getOrigin() == 2) {
            monitorDeviceVar.setMessageAddress(0);
        }
        //endregion

        return toAjax(monitorDeviceVarService.insertMonitorDeviceVar(monitorDeviceVar));
    }

    /**
     * 修改监控设备变量
     */
    @ApiOperation("修改监控设备变量")
    @RequiresPermissions("system:device-var:edit")
    @Log(title = "监控设备变量", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MonitorDeviceVar monitorDeviceVar) {
        if (StringUtils.longIsBlack0(monitorDeviceVar.getDeviceId())) {
            return error("请选择监控设备");
        }

        MonitorDevice monitorDevice = monitorDeviceService.selectMonitorDeviceById(monitorDeviceVar.getDeviceId());
        if (monitorDevice == null || monitorDevice.getStopFlag() == 1) {
            return error("监控设备无效，请先配置通道");
        }
        monitorDeviceVar.setStationType(monitorDevice.getStationType());

        //region 同一管理机上，IO型变量信息体地址唯一
        if (monitorDeviceVar.getOrigin() == 1 && monitorDeviceVar.getMessageAddress() > 0) {
            if (!monitorDeviceVarService.checkMessageAddressOnly(monitorDeviceVar)) {
                return error("信息体地址重复");
            }
        }
        //endregion

        //内存型变量 清空-信息地地址
        if (monitorDeviceVar.getOrigin() == 2) {
            monitorDeviceVar.setMessageAddress(0);
        }
        return toAjax(monitorDeviceVarService.updateMonitorDeviceVar(monitorDeviceVar));
    }

    /**
     * 修改监控设备变量状态
     */
    @ApiOperation("修改监控设备变量状态")
    @RequiresPermissions("system:device-var:state")
    @Log(title = "监控设备变量", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(monitorDeviceVarService.updateMonitorDeviceVarState(new MonitorDeviceVar(), id, state));
    }

    /**
     * 修改监控设备变量【监控状态】
     */
    @ApiOperation("修改监控设备变量【监控状态】")
    @RequiresPermissions("system:device-var:state")
    @Log(title = "监控设备变量", businessType = BusinessType.UPDATE)
    @PutMapping("/changeMonitorStatus/{id}/{state}")
    public AjaxResult changeMonitorStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(monitorDeviceVarService.updateDeviceVarMonitorState(new MonitorDeviceVar(), id, state));
    }

    /**
     * 删除监控设备变量
     */
    @ApiOperation("删除监控设备变量")
    @RequiresPermissions("system:device-var:remove")
    @Log(title = "监控设备变量", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(monitorDeviceVarService.deleteMonitorDeviceVarByIds(ids));
    }

    //region // 变量数据存储KEY
    private String getVariableCacheKey(String varSn) {
        return CacheConstants.MONITOR_DEVICE_VARSN_KEY + varSn;
    }
    //endregion


    //region 远程内部调用

    /**
     * 远程内部调用-根据条件查询
     *
     * @param request 查询条件
     * @return 配置
     */
    @InnerAuth
    @PostMapping("/list/inner")
    public List<FeignMonitorDeviceVar> getListInnerAuth(@RequestBody FeignMonitorDeviceVar request) {

        // 创建一个新的MonitorDeviceVar对象，并从请求中复制属性
        MonitorDeviceVar monitorDeviceVar = new MonitorDeviceVar();
        BeanUtils.copyProperties(request, monitorDeviceVar);

        // 获取符合条件的MonitorDeviceVar列表
        List<MonitorDeviceVar> list = monitorDeviceVarService.selectMonitorDeviceVarList(monitorDeviceVar);
        if (Objects.isNull(list)) {
            return new ArrayList<>();
        }
        // 将MonitorDeviceVar列表转换为FeignMonitorDeviceVar列表
        List<FeignMonitorDeviceVar> result = new ArrayList<>();
        // 遍历源列表
        for (MonitorDeviceVar source : list) {
            // 创建一个新的 FeignMonitorDeviceVar 对象
            FeignMonitorDeviceVar feignMonitorDeviceVar = new FeignMonitorDeviceVar();
            // 使用 BeanUtils.copyProperties() 方法将源对象的属性复制到目标对象
            BeanUtils.copyProperties(source, feignMonitorDeviceVar);
            // 将目标对象添加到结果列表中
            result.add(feignMonitorDeviceVar);
        }
        return result;
    }

    /**
     * 远程内部调用-根据变量编码查询
     *
     * @param varSn 变量编码
     * @return 变量数据
     */
    @InnerAuth
    @GetMapping("/getInfo/inner/{varSn}")
    public FeignMonitorDeviceVar getInfoInnerAuth(@PathVariable("varSn") String varSn) {
        // 根据变量编码获取变量信息
        MonitorDeviceVar monitorDeviceVar = monitorDeviceVarService.selectMonitorDeviceVarByVarSn(varSn);
        if (Objects.isNull(monitorDeviceVar)) {
            return null;
        }
        // 将MonitorDeviceVar转换为FeignMonitorDeviceVar
        FeignMonitorDeviceVar feignMonitorDeviceVar = new FeignMonitorDeviceVar();
        BeanUtils.copyProperties(monitorDeviceVar, feignMonitorDeviceVar);
        return feignMonitorDeviceVar;
    }
    //endregion
    
    
    public static void main(String args[]) {
    	EnumSOTVO vo = null;
    	String json = "{\n"
    			+ "	\"key\": \"htycy_D1__Ia\",\n"
    			+ "	\"value\": \"387.98\",\n"
    			+ "	\"time\": \"2024-11-29 09:21:30\",\n"
    			+ "	\"name\": \"Ia\",\n"
    			+ "	\"unit\": \"\",\n"
    			+ "	\"varType\": \"1\"\n"
    			+ "}";
        if (!StringUtils.isEmpty(json)) {
            vo = JSON.parseObject(json, EnumSOTVO.class);
            System.out.println(vo.getKey());
        }
    }
    
    
}

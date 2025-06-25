package com.yunpower.datav.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 图表配置实体
 *
 * @author: XIAOPING.CAO
 */
@Data
public class ChartConfigDto implements Serializable {
	/**
	 * 数据类型（1单值 2连续值）
	 */
	private Integer valueType;

	/**
	 * 接口类型（1实时[WebSocket + 缓存] 2数据库 3自定义接口）
	 */
	private Integer apiType;
	/**
	 * 电价标准（1跟随日期  2指定电价）
	 */
	private Integer electricityPriceType;

	/**
	 * 指定电价
	 */
	private Long schemeId;

	/**
	 * 列表类型（1报警  2工单）
	 */
	private Integer tableType;

	/**
	 * 表格宽度（1长表头  2短表头）
	 */
	private Integer headType;

	/**
	 * 统计类型（1按类型 2按级别）（1按类型 2按日期）
	 */
	private Integer staticType;

	/**
	 * 指定变量-数据区
	 */
	private List<Var> varList;

	/**
	 * 是否显示辅助线
	 * 默认值为 false
	 */
	private Boolean isShowLine = false;

	/**
	 * 辅助线
	 */
	private List<LineChild> lineChildren;

	/**
	 * 图例位置（1上 2下 3左 4右）
	 * 默认值为 2
	 */
	private Integer legendPosition = 2;

	/**
	 * 右数据区
	 */
	private List<Var> children;

	/**
	 * 设备变量
	 */
	@Data
	public static class Var implements Serializable {

		/**
		 * 通用变量（用电量 发电量 电流 电压 有功功率 无功功率 视在功率 功率因素 温度 湿度 负载率 状态）
		 * 默认值为 sys_config_variable_type 字典值
		 */
		private Integer comVariable;

		/**
		 * 变量编码
		 */
		private String varSn;

		/**
		 * 变量名称
		 */
		private String varName;

		/**
		 * 图例名称
		 */
		private String legend;

		/**
		 * 图例颜色（默认无，即跟随组件）
		 */
		private String color;

		/**
		 * 指标单位（默认是变量中的单位，允许用户修改）
		 */
		private String unit;

		/**
		 * 存储类型（1变化值 2累计值 3 峰平谷）
		 */
		private Integer storageType;

		/**
		 * 数值类型（1实时/平均值 2最小值 3最大值）
		 */
		private Integer changeType;

		/**
		 * 辅助指标-环比
		 */
		private Boolean chain = false;

		/**
		 * 辅助指标-同比
		 */
		private Boolean yoy = false;

		/**
		 * 辅助指标-显示电费
		 */
		private Boolean showCharge = false;

		/**
		 * 显示类型（1数值 2百分比 3比值）
		 */
		private Integer percentage = 1;

		/**
		 * 基准值
		 */
		private Double baseValue;

		/**
		 * 最小值
		 */
		private Double minValue;

		/**
		 * 最大值
		 */
		private Double maxValue;

		/**
		 * 背景颜色
		 * 默认值为 #00000000
		 */
		private String backgroundColor;

		/**
		 * 图标
		 */
		private String icon;

		/**
		 * 图标颜色
		 */
		private String iconColor;

		/**
		 * 设备类型（1通讯相关  2设备相关）
		 */
		private Integer equipType = 1;

		/**
		 * 状态类型（1通讯状态 2设备状态 3市电状态）
		 */
		private Integer stateType;

		/**
		 * 图表-数据转换类型
		 */
		private String conversion;

		/**
		 * 变量-数据系数
		 * */
		private Double coefficient;

		/**
		 * 单位换算 自动换算
		 */
		private Boolean unitConvert = true;

		/**
		 * 字体颜色
		 */
		private String fontColor;

		/**
		 * 子数据区
		 */
		private List<Var> children;
	}

	/**
	 * 辅助线
	 */
	@Data
	public static class LineChild {
		/**
		 * 辅助线方向（0无 1横线 2竖线）
		 * 默认值为 1
		 */
		private Integer lineDirection;

		/**
		 * 辅助线名称
		 */
		private String lineName;

		/**
		 * 辅助线值
		 */
		private String lineValue;
	}
}


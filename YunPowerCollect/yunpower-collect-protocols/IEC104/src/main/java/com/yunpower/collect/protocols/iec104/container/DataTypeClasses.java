package com.yunpower.collect.protocols.iec104.container;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

/**
 * 使用了AsduType的注解类的存储容器为适配asdu类型服务
 * 将类  解析类型（方法），组装数据（方法）放入其中，读取后放入内存
 * 避免重复读取
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataTypeClasses {

	private Class typeClass;

	private int typeId;

	private Method load;

}

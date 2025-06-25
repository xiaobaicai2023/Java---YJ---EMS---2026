package com.yunpower.common.core.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表达式引擎工具类
 * */
@Slf4j
public class AviatorUtils {
	/**
	 * 解析公式 - 返回对应的变量集合
	 */
	public static List<String> extractVariables(String expression) {
		// 改进的正则表达式，确保匹配的变量包含至少一个字母
		Pattern pattern = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
		Matcher matcher = pattern.matcher(expression);

		// 用于存储变量名的集合
		Set<String> variables = new LinkedHashSet<>();

		// 提取匹配的变量名
		while (matcher.find()) {
			String variable = matcher.group();
			variables.add(variable);
		}

		if (variables.isEmpty()) {
			return null;
		}

		return new ArrayList<>(variables);
	}

	/**
	 * 执行表达式
	 * */
	public static Double execute(String expression, Map<String, Object> env) {
		Double value = 0D;
		try{
			//校验表达式
			if(StrUtil.isEmpty(expression)){
				return value;
			}
			//校验
			if(ObjectUtil.isEmpty(env)){
				return value;
			}

			// 解析并执行表达式
			Object result = AviatorEvaluator.execute(expression, env);

			//结果转换
			value = (result instanceof Number) ? ((Number) result).doubleValue() : null;

		}catch (Exception ex){
			log.error("表达式执行异常 execute 表达式：{} 参数：{}",expression,env);
			log.error("异常信息",ex);
		}
		return value;
    }

	/**
	 * 自定义执行表达式
	 * */
	public static boolean customExecute(String expressionStr , Map<String, Object> env){
		boolean result = false;
		try{
			//定义规则表达式
			Expression expression = AviatorEvaluator.compile(expressionStr);
			// 计算结果
			result = (Boolean) expression.execute(env);
		}catch (Exception ex){
			log.error("表达式执行异常 customExecute 表达式：{} 参数：{}",expressionStr,env);
			log.error("异常信息",ex);
		}
		return result;
	}
}

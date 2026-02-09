package com.amituofo.common.kit.parser;

import java.util.ArrayList;
import java.util.List;

import com.amituofo.common.ex.ParseException;
import com.amituofo.common.util.StringUtils;

public class FunctionParser {

	  /**
     * 提取函数表达式
     * @param expression 函数表达式字符串，如 "myfunc('aaa','bbb')" 或 "myfunc(aaa, bbb)"
     * @return Func对象，包含函数名和参数数组
	 * @throws ParseException 
     */
    public static FunctionDesc parse(String expression) throws ParseException {
        expression = expression.trim();
        
        // 查找函数名和参数部分的分界点（左括号）
        int leftParen = expression.indexOf('(');
		if (leftParen == -1 || !expression.endsWith(")")) {
			throw new ParseException("Invalid function expression: " + expression);
		}

		// 提取函数名
		String funcName = expression.substring(0, leftParen).trim();
		if (StringUtils.isEmpty(funcName)) {
			throw new ParseException("Invalid function expression: " + expression + ", Function name not found! ");
		}

		// 提取参数部分（去掉括号）
		String paramsStr = expression.substring(leftParen + 1, expression.length() - 1).trim();

		List<String> params = new ArrayList<>();
        
        if (!paramsStr.isEmpty()) {
            // 逐个解析参数
            int start = 0;
            boolean inQuote = false;
            
            for (int i = 0; i < paramsStr.length(); i++) {
                char c = paramsStr.charAt(i);
                
                if (c == '\'') {
                    inQuote = !inQuote; // 切换引号状态
                } else if (c == ',' && !inQuote) {
                    // 遇到逗号且不在引号内，提取参数
                    String param = paramsStr.substring(start, i).trim();
                    params.add(cleanParam(param));
                    start = i + 1;
                }
            }
            
            // 添加最后一个参数
            String lastParam = paramsStr.substring(start).trim();
            params.add(cleanParam(lastParam));
        }
        
        return new FunctionDesc(funcName, params.toArray(new String[params.size()]));
    }

    /**
     * 清理参数（去除可能的引号）
     */
    private static String cleanParam(String param) {
        if (param.startsWith("'") && param.endsWith("'") && param.length() > 1) {
            return param.substring(1, param.length() - 1);
        }
        return param;
    }

    public static void main(String[] args) {
        // 测试用例
        String[] testCases = {
            "myfunc('aaa','bbb')",
            "myfunc(aaa, bbb)",
            "myfunc('aaa', bbb)",
            "myfunc()",
            "myfunc( 'test', 'with spaces', '00',   123 )",
            "myfunc(123, 456)",
            "anotherFunc('single param')",
            "complexFunc('a,b,c', 'd\\'e')"
        };
        
        for (String testCase : testCases) {
            System.out.println("Testing: " + testCase);
            try {
            	FunctionDesc result = parse(testCase);
                System.out.println("Function name: " + result.name);
                System.out.println("Parameters: ");
                for (String param : result.params) {
                    System.out.println("  " + param + "");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
    }
}

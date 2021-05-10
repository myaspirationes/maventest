package org.DB;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Administrator wangyongzhi
 * @Create 2018/2/22
 */
public class ParamHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private Map<String, String[]> params = new HashMap<String, String[]>();

    public ParamHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.params.putAll(request.getParameterMap());
    }

    // 重载一个构造方法
    public ParamHttpServletRequestWrapper(HttpServletRequest request, Map<String, Object> extendParams) {
        this(request);
        addAllParameters(extendParams);// 这里将扩展参数写入参数表
    }

    @Override
    public String getParameter(String name) {// 重写getParameter，代表参数从当前类中的map获取
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    public void addAllParameters(Map<String, Object> otherParams) {// 增加多个参数
        for (Map.Entry<String, Object> entry : otherParams.entrySet()) {
            addParameter(entry.getKey(), entry.getValue());
        }
    }

    public void addParameter(String name, Object value) {// 增加参数
        if (value != null) {
            if (value instanceof String[]) {
                params.put(name, (String[])value);
            } else if (value instanceof String) {
                params.put(name, new String[] {(String)value});
            } else {
                params.put(name, new String[] {String.valueOf(value)});
            }
        }
    }

    /**
     * 重写获取参数方法
     *
     * @return
     */
    @Override
    public Map<String, String[]> getParameterMap() {

        this.params.putAll(super.getParameterMap());
        params.remove("p");
        // params.remove("q");
        return Collections.unmodifiableMap(params);
    }


    @Override
    public String[] getParameterValues(String name) {
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values;
        //return super.getParameterValues(name);
    }
}

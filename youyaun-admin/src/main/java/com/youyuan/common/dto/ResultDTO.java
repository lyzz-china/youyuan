package com.youyuan.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 结果处理
* @author yizhong.liao
* @createTime 2021/8/30 17:15
*/
public class ResultDTO implements Serializable {

    private static final String correctValue = "0";

    private static final long serialVersionUID = 1L;
    private String resultCode;
    private String resultMsg;
    private Map<String, Object> resultData;

    private ResultDTO(){}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public Map<String, Object> getResultData() {
        return resultData;
    }

    public void setResultData(Map<String, Object> resultData) {
        this.resultData = resultData;
    }

    public void addDataEntry(String key, Object value){
        if (resultData == null) {
            resultData = new HashMap<String, Object>();
        }
        resultData.put(key, value);
    }

    public Object findDataEntry(String key){
        if (resultData == null) {
            return null;
        }
        return resultData.get(key);
    }

    public void removeDataEntry(String key){
        if (resultData == null) {
            return;
        }
        resultData.remove(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T findDataEntry(String key, Class<T> type) {
        if (resultData == null) {
            return null;
        }
        Object value = resultData.get(key);
        if (null != value && type.isInstance(value)) {
            return (T) value;
        } else {
            return null;
        }
    }

    public <T> List<T> findDataEntryList(String key, Class<T> clazz) {
        if (resultData == null) {
            return null;
        }

        List<T> result = new ArrayList<T>();
        Object value = resultData.get(key);
        if(null != value && value instanceof List<?>)
        {
            for (Object o : (List<?>) value)
            {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    public static ResultDTO instance() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setResultCode(correctValue);
        return resultDTO;
    }

    public static ResultDTO instance(String code, String msg) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setResultCode(code);
        resultDTO.setResultMsg(msg);
        return resultDTO;
    }

    public boolean isSuccess(){
        return null != resultCode && resultCode.equals("0");
    }

    public boolean isFailure(){
        return !isSuccess();
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
                "resultCode='" + resultCode + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                ", resultData=" + resultData +
                '}';
    }
}

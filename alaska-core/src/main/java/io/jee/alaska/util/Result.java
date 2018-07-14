package io.jee.alaska.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Result<T> implements Serializable {

	public static final Integer CODE_SUCCESS = 0;
	public static final Integer CODE_ERROR = -1;
	private static final long serialVersionUID = -6298623269351615426L;
	private Boolean success;
	private Integer code;
	private String message;
	private T data;
	private Map<String, String> errorFields;
	
	public static Result<Map<Object, Object>> successMapData(Object dataKey, Object dataValue){
		Result<Map<Object, Object>> result = new Result<>();
		result.setSuccess(true);
		result.setData(new HashMap<Object, Object>());
		result.getData().put(dataKey, dataValue);
		return result;
	}
	
	public static <T> Result<T> success(){
		return Result.success(null);
	}
	
	public static <T> Result<T> success(T data){
		return Result.success(null, data);
	}
	
	public static <T> Result<T> success(String message, T data){
		return Result.success(CODE_SUCCESS, message, data);
	}
	
	public static <T> Result<T> success(Integer code, String message, T data){
		return new Result<T>(true, code, message, data, null);
	}
	
	public static <T> Result<T> error(String message){
		return Result.error(CODE_ERROR, message, null, null);
	}
	
	public static <T> Result<T> error(int code, String message){
		return Result.error(code, message, null, null);
	}
	
	public static <T> Result<T> error(Map<String, String> errorFields){
		return Result.error(CODE_ERROR, null, null, errorFields);
	}
	
	public static <T> Result<T> error(String errorField, String errorFieldMessage){
		return Result.error(CODE_ERROR, errorField, errorFieldMessage);
	}
	
	public static <T> Result<T> error(Integer code, String errorField, String errorFieldMessage){
		Map<String, String> errorFields = new HashMap<>();
		errorFields.put(errorField, errorFieldMessage);
		return Result.error(code, errorFieldMessage, null, errorFields);
	}
	
	public static <T> Result<T> error(Integer code, String message, T data, Map<String, String> errorFields){
		return new Result<T>(false, code, message, data, errorFields);
	}
	
	public static <T> Result<T> code(Integer code, String message){
		return Result.code(code, message, null);
	}
	
	public static <T> Result<T> code(Integer code, String message, T data){
		return new Result<T>(null, code, message, data, null);
	}
	
	public static <T> Result<T> result(boolean success, String message){
		return new Result<T>(success, success ? CODE_SUCCESS : CODE_ERROR, message, null, null);
	}
	
	public static <T> Result<T> result(Result<?> source){
		Result<T> target = new Result<T>(source.isSuccess(), source.getCode(), source.getMessage(), null, source.getErrorFields());
		return target;
	}
	
	public Result() { }

	public Result(Boolean success, Integer code, String message, T data, Map<String, String> errorFields) {
		this.success = success;
		this.code = code;
		this.message = message;
		this.data = data;
		this.errorFields = errorFields;
	}

	public Boolean isSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Map<String, String> getErrorFields() {
		return errorFields;
	}

	public void setErrorFields(Map<String, String> errorFields) {
		this.errorFields = errorFields;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}

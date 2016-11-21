package io.jee.alaska.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Result<T> implements Serializable {

	private static final long serialVersionUID = -6298623269351615426L;
	private Boolean success;
	private Integer code;
	private String message;
	private T data;
	private Map<String, String> errorFields;
	
	
	public static <T> Result<T> success(){
		return Result.success(null);
	}
	
	public static <T> Result<T> success(T data){
		return Result.success(null, data);
	}
	
	public static <T> Result<T> success(String message, T data){
		return Result.success(null, message, data);
	}
	
	public static <T> Result<T> success(Integer code, String message, T data){
		return new Result<T>(true, code, message, data, null);
	}
	
	public static <T> Result<T> error(String message){
		return Result.error(null, message, null, null);
	}
	
	public static <T> Result<T> error(Map<String, String> errorFields){
		return Result.error(null, null, null, errorFields);
	}
	
	public static <T> Result<T> error(String errorField, String errorFieldMessage){
		return Result.error(null, errorField, errorFieldMessage);
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
		return new Result<T>(success, null, message, null, null);
	}
	
	public static <T> Result<T> result(Result<?> source){
		Result<T> target = new Result<T>(source.isSuccess(), source.getCode(), source.getMessage(), null, source.getErrorFields());
		return target;
	}

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
		return "Result [success=" + success + ", code=" + code + ", message=" + message + ", data=" + data
				+ ", errorFields=" + errorFields + "]";
	}
	
}

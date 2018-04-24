package io.jee.alaska.exception;

/**
 * <P>Title: 标记事务可回滚的业务异常,配合声明式事务使用
 * <p>Descriptor: 业务系统可以根据业务需求，继承该类定义具体业务相关的业务。如：NoFoundException, ParameterInvaidException 等。
 * <P>Copyright (c) CAISAN 2018
 * @author XieXiaoXu on 2018年4月24日
 *
 */
public class BusinessException extends AbstractI18NMessageException {

	/** UID */
	private static final long serialVersionUID = -9018571104185955115L;

	public BusinessException() {
		super();
	}

	public BusinessException(BusinessExceptionMsg msg) {
		super(msg.getCode(), msg.getDesc());
	}

	public BusinessException(BusinessExceptionMsg msg, Throwable cause) {
		super(msg.getCode(), msg.getDesc(), cause);
	}

	public BusinessException(int errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

	public BusinessException(int errorCode, String errorMessage, Throwable cause) {
		super(errorCode, errorMessage, cause);
	}

	public BusinessException(int errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	public BusinessException(String errorMessage, Throwable cause) {
		super(errorMessage, cause);
	}

	public BusinessException(String errorMessage) {
		super(errorMessage);
	}


}

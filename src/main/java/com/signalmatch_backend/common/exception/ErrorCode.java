package com.signalmatch_backend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

	//Internal Server Error
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 생겼습니다."),

	// Client Error
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "적절하지 않은 HTTP 메소드입니다."),
	INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "요청 값의 타입이 잘못되었습니다."),
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "적절하지 않은 값입니다."),
	NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리소스를 찾을 수 없습니다."),
	MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다."),

	//Bookmark Error
	BOOKMARK_ONLY_ONE_TARGET_ALLOWED(HttpStatus.BAD_REQUEST,"investorId 또는 startupId 중 하나만 값이 있어야 합니다."),
	BOOKMARK_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 북마크가 존재합니다."),
	//Auth
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

	// User Error
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자의 정보를 찾을 수 없습니다."),
	LOGINID_ALREADY_USED(HttpStatus.BAD_REQUEST,"이미 존재하는 아이디입니다."),
	PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST,"비밀번호가 일치하지 않습니다"),
	PASSWORD_ALREADY_USED(HttpStatus.BAD_REQUEST,"사용중인 비밀번호 입니다."),
	PROFILE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 프로필이 존재합니다."),

	//Startup Error
	STARTUP_NOT_FOUND(HttpStatus.NOT_FOUND, "스타트업의 정보를 찾을 수 없습니다." ),

	//Investor Error
	INVESTOR_NOT_FOUND(HttpStatus.NOT_FOUND,"투자자의 정보를 찾을 수 없습니다."),

	//Matching Error
	MATCH_ALREADY_USED(HttpStatus.BAD_REQUEST,"이미 진행 중인 매칭입니다."),
	MATCH_NOT_FOUND(HttpStatus.NOT_FOUND, "매칭의 정보를 찾을 수 없습니다."),

	//File Error
	INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "허용되지 않은 파일 형식입니다."),
	INVALID_MIME_TYPE(HttpStatus.BAD_REQUEST, "허용되지 않은 MIME 타입입니다."),
	IMAGE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "이미지 파일 크기가 허용 범위를 초과했습니다."),
	IR_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "IR 파일 크기가 허용 범위를 초과했습니다."),
	PDF_ONLY(HttpStatus.BAD_REQUEST, "IR 파일은 PDF만 가능합니다."),
	FILE_EXTENSION_REQUIRED(HttpStatus.BAD_REQUEST, "파일 확장자가 필요합니다.");


    private final HttpStatus status;
	private final String message;



	ErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}

package com.signalmatch_backend.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    @Value("${aws.s3.region.static:}")
    private String region;

    @Value("${aws.s3.credentials.accessKey:}")
    private String accessKey;

    @Value("${aws.s3.credentials.secretKey:}")
    private String secretKey;


    private AwsCredentialsProvider provider() {
        if (StringUtils.hasText(accessKey) && StringUtils.hasText(secretKey)) {
            return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
        }
        // 로컬 프로파일/EC2 IAM Role 등 기본 체인 사용
        return DefaultCredentialsProvider.create();}

    private Region resolveRegion() {
        if (StringUtils.hasText(region)) {
            return Region.of(region);}
        // 환경변수/프로파일/인스턴스 메타데이터 등에서 지역 자동 해석
        return DefaultAwsRegionProviderChain.builder().build().getRegion();
 }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(resolveRegion())
                .credentialsProvider(provider())
                .build();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(resolveRegion())
                .credentialsProvider(provider())
                .build();
    }
}
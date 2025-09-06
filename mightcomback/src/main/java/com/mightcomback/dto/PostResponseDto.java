// 역활: 게시물 조회 API의 응답 형식을 정의하는 데이터 전송 객체
package com.mightcomback.dto;

import com.mightcomback.domain.Post;
import com.mightcomback.domain.Tag;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 게시물 조회 결과를 클라이언트에게 전달하기 위한 데이터 전송 객체(DTO)입니다.
 * Entity와 달리, 이 객체는 API 응답 스펙에 맞게 필요한 데이터만 포함합니다.
 * Entity의 변경이 API 응답에 직접적인 영향을 주지 않도록 격리하는 역할을 합니다.
 */

@Getter
public class PostResponseDto {
    
    private final Long id;
    private final String storeName;
    private final String body;
    private final Short rating;
    private final Set<String> tags; // 태그는 Tag 객체가 아닌, 이름(String)의 Set으로 변환하여 전달
    private final LocalDateTime createdAt;


    /**
     * DTO의 생성자.
     * Post 엔티티를 받아서 DTO 필드를 초기화합니다.
     * @param post 원본 Post 엔티티
     */
    private PostResponseDto(Post post) {
        this.id = post.getId();
        this.storeName = post.getStoreName();
        this.body = post.getBody();
        this.rating = post.getRating();
        // Post 엔티티에 있는 Set<Tag>를 Set<String>으로 변환합니다.
        this.tags = post.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());
        this.createdAt = post.getCreatedAt();
    }

    /**
     * Post 엔티티를 PostResponseDto로 변환하는 정적 팩토리 메소드입니다.
     * new PostResponseDto(post) 대신 PostResponseDto.fromEntity(post) 형태로 사용하여
     * 코드의 가독성과 객체 생성 방식의 일관성을 높입니다.
     * @param post 변환할 Post 엔티티
     * @return 변환된 PostResponseDto 객체
     */
    public static PostResponseDto fromEntity(Post post) {
        return new PostResponseDto((post));
    }
}

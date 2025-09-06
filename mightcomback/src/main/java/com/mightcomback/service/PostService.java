package com.mightcomback.service;

import com.mightcomback.domain.Post;
import com.mightcomback.dto.PostResponseDto;
import com.mightcomback.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Post 관련 비즈니스 로직을 처리하는 서비스 클래스 입니다.
 * @Service: 이 클래스가 비즈니스 로직을 담당하는 서비스 계층의 컴포넌트임을 Spring에게 알려줍니다.
 * @RequiredArgsConstructor: final 필드에 대한 생성자를 자동으로 생성해주는 Lombok 어노테이션입니다.
 * 이를 통해 PostRepository를 생성자 주입 방식으로 안전하게 의존성 주입(DI)받을 수 있습니다.
 */


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 클래스 전에에 읽기 전용 트랜잭션을 적용하여 성능을 최적화합니다.
public class PostService {

    private final PostRepository postRepository;    // 생성자 주입

    /**
     * 태그를 기반으로 게시물을 검색하는 핵심 비즈니스 로직입니다.
     * 검색 조건('AND' 또는 "OR")에 따라 적절한 리포지토리 메소드를 호출합니다.
     * 
     * @param tagNames 검색할 태그 이름 목록
     * @param searchCondition 검색 조건 (AND 또는 OR)
     * @return 검색된 게시물 정보를 담은 DTO 리스트
     */
    

     public List<PostResponseDto> searchPostsByTags(List<String> tagNames, String searchCondition) {
        // 검색할 태그가 없거나 비어있으면 빈 리스트를 반환합니다.
        if(tagNames == null || tagNames.isEmpty()) {
            return Collections.emptyList();
        }

        List<Post> posts;

        // 대소문자 구분 없이 'AND' 조건으로 검색합니다.
        if ("AND".equalsIgnoreCase(searchCondition)) {
            posts = postRepository.findByAllTags(tagNames, (long) tagNames.size());
        }
        // 그 외의 모든 경우 (예: "OR", null, 오타 등) 'OR' 조건으로 기본 검색을 수행합니다.
        else {
            posts = postRepository.findByAnyTags(tagNames);
        }

        // 조화된 Post 엔티티 리스트를 PostResponseDto 리스트로 변환하여 반환합니다.
        // Entity를 직접 Controller로 반환하는 것은 좋지 않은 습관이므로, DTO로 변환하는 과정을 거칩니다.
        return posts.stream()
            .map(PostResponseDto::fromEntity)
            .collect(Collectors.toList());
     }

     // ... 여기에서 게시물 생성, 수정, 삭제 등의 비즈니스 로직 추가...
}


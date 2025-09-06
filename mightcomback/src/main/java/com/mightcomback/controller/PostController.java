package com.mightcomback.controller;

import com.mightcomback.dto.PostResponseDto;
import com.mightcomback.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Post 관련 API 요청을 처리하는 컨트롤러 클래스입니다.
 * @RestController: 이 클래스가 RESTful API의 엔드포인트를 담당하는 컨트롤러임을 나타냅니다.
 * 이 어노테이션이 붙은 클래스의 메소드는 기본적으로 JSON 형태의 데이터를 반환합니다.
 * @RequestMapping("/api/posts"): 이 컨트롤러의 모든 메소드에 대한 공통 URL 경로를 '/api/posts'로 설정합니다.
 * @RequiredArgsConstructor: final 필드에 대한 생성자를 자동으로 생성하여 PostService를 주입받습니다.
 */
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor

public class PostController {

    private final PostService postService;
    
    /**
     * 태그를 기반으로 게시물을 검색하는 API 엔드포인트 입니다.
     * HTTP GET 요청을 통해 접근할 수 있습니다.
     * 예시 URL: /api/posts/search?tags=데이트, 강남역&condition=AND
     * 
     * @GetMapping("/search"): '/api/posts' 뒤에 '/search' 경로가 추가된 GET 요청을 이 메소드와 메핑합니다
     * @RequestParam: URL의 쿼리 파라미터(?, &로 연결된 값들)를 메소드의 파라미터와 바인딩합니다.
     * - 'List<String> tags' : 'tags' 파라미터를 받습니다. (예: "데이트, 강남역" -> ["데이트", "강남역"]리스트로 자동 변환)
     * - 'String condition': 'condition' 파라미터를 받습니다. 'defaultValue = "OR'를 통해 값이 없을 경우 기본으로 "OR"을 사용합니다.
     * @return 검색 결과를 담은 DTO 리스트를 ResponseEntity 객체로 감싸서 변환합니다.
     */
    @GetMapping("/search")
    public ResponseEntity<List<PostResponseDto>> searchPosts(
            @RequestParam List<String> tags,
            @RequestParam(defaultValue = "OR") String condition) {
        
        // 주입받은 PostService의 검색 메소드를 호출하여 비즈니스 로직을 수행합니다.
        List<PostResponseDto> results = PostService.searchPostsByTags(tags. condition);

        // 서비스로부터 받은 결과(DTO 리스트)를 HTTP 200 OK 상태 코드와 함께 응답 본문에 담아 반환합니다
        return ResponseEntity.ok(results);

    }
}

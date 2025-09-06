package com.mightcomback.domain;


import org.springframework.data.jpa.Repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.Set;



/*
================================================================================
 Part 3: Tag 리포지토리 (TagRepository.java)
--------------------------------------------------------------------------------
 - 'tags' 테이블에 대한 데이터베이스 접근(CRUD)을 담당하는 인터페이스입니다.
 - Spring Data JPA가 이 인터페이스의 구현체를 자동으로 생성해 줍니다.
*/
@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    /**
     * 태그 이름으로 태그를 조회합니다.
     * 새로운 태그를 저장하기 전에, 이 메소드를 통해 DB에 이미 존재하는 태그인지
     * 확인하는 데 사용됩니다. (태그 중복 방지)
     * @param name 태그 이름 (예: "#데이트")
     * @return Optional<Tag>
     */
    Optional<Tag> findByName(String name);

    /**
     * 여러 개의 태그 이름으로 해당하는 태그 목록을 한 번에 조회합니다.
     * 게시물에 포함된 여러 태그들을 한 번의 쿼리로 효율적으로 가져올 때 유용합니다.
     * @param names 태그 이름 목록
     * @return List<Tag>
     */
    List<Tag> findByNameIn(Set<String> names);

}

// 역활: Post 엔티티에 대한 데이터베이스 접근 및 커스텀 쿼리 담당
// 위치: com.mightcomback.repository 패키지
package com.mightcomback.repository;

import com.mightcomback.domain.Post;    // Post 엔티티의 새로운 이름 import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Query.param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // ====================================================================
    // == OR 조건 검색 (findByAnyTags) 쿼리 구현 ==
    // ====================================================================
    /**
     * 주어진 태그 목록 중 하나라도 포함된 게시물을 검색합니다. (OR 조건)
     *
     * JPQL 설명:
     * - `SELECT DISTINCT p`: 중복된 Post가 결과에 포함되지 않도록 DISTINCT를 사용합니다.
     * (예: '#데이트', '#강남'으로 검색 시, 두 태그를 모두 가진 게시물이 결과에 두 번 나타나는 것을 방지)
     * - `FROM Post p JOIN p.tags t`: Post(p) 엔티티와 그와 연관된 tags(t) 컬렉션을 조인합니다.
     * - `WHERE t.name IN :tagNames`: Tag의 이름(t.name)이 파라미터로 받은 태그 이름 목록(:tagNames)에 포함되는 경우를 찾습니다.
     *
     * @param tagNames 검색할 태그 이름의 리스트
     * @return 조건에 맞는 게시물 리스트
     */
    @Query("SELECT DISTINCT p FROM Post p JOIN p.tags t WHERE t.name IN :tagNames")
    List<Post> findByAnyTags(@Param("tagNames") List<String> tagNames);

    // ====================================================================
    // == AND 조건 검색 (findByAllTags) 쿼리 구현 ==
    // ====================================================================
    /**
     * 주어진 태그 목록을 '모두' 포함하는 게시물만 검색합니다. (AND 조건)
     * 이 쿼리는 태그 검색 기능의 핵심 로직입니다.
     *
     * JPQL 설명:
     * 1. `FROM Post p JOIN p.tags t`: Post와 Tag를 조인하여 모든 '게시물-태그' 연결 관계를 가져옵니다.
     * 2. `WHERE t.name IN :tagNames`: 사용자가 검색한 태그들('#데이트', '#강남' 등)에 해당하는 연결 관계만 필터링합니다.
     * 3. `GROUP BY p.id`: 필터링된 결과를 게시물 ID(p.id)로 그룹화합니다.
     * 이제 각 그룹은 하나의 게시물과 그 게시물이 가진 (사용자가 검색한) 태그들의 묶음이 됩니다.
     * 4. `HAVING COUNT(DISTINCT t.id) = :tagCount`: 그룹화된 결과에 대한 조건을 설정합니다.
     * - `COUNT(DISTINCT t.id)`: 각 게시물 그룹(p.id)에 포함된 고유한 태그의 개수를 셉니다.
     * - `= :tagCount`: 그 개수가 파라미터로 받은 '검색할 태그의 총 개수'(:tagCount)와 같은 그룹만 최종 결과로 선택합니다.
     * - 예: '#데이트', '#강남' (tagCount=2)으로 검색했다면, 태그 개수가 정확히 2개인 게시물 그룹만 살아남습니다.
     *
     * @param tagNames 검색할 태그 이름의 리스트
     * @param tagCount 검색할 태그의 총 개수 (tagNames.size())
     * @return 조건에 맞는 게시물 리스트
     */
    @Query("SELECT p FROM post p JOIN p.tags t WHERE t.name IN : tagNames GROUP BY p.id HAVING COUNT(DISTINCT t.id) = :tagCount")
    List<Post> findByAllTags(@Param("tagNames") List<String> tagNames, @Param("tagCount") Long tagCount);

    
}

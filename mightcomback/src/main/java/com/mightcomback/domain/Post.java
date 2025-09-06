package com.mightcomback.domain;

import com.mightcomback.domain.Tag;    // Tag 엔티티 패키지 경로
import jakarta.persistence.*;
import Lombok.Getter;
import Lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/*
================================================================================
 Part 2: Post 엔티티 (Post.java - 태그 관계 부분만 발췌)
--------------------------------------------------------------------------------
 - 기존 Post 엔티티에 Tag와의 관계를 설정하는 코드입니다.
 - 이 클래스가 관계의 주인(Owner) 역할을 합니다.
*/

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor //Lombok: 기본 생성자 자동 생성
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ... (storeName, body 등 다른 필드들) ...
    // User와의 관계 (한 명의 유저는 여러 개시물을 작성할 수 있다.)
    // 실제 User 엔티티가 필요하지만, 여기서는 주석으로 표시합니다.
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id")
    // private User user;

    @Column(name = "store_name", nullable = false, length = 100)
    private String storeName;


    @Lob // 본문 내용은 길 수 있으므로 TEXT 타입으로 매핑
    @Column (name = "body")
    private String body;

    @Column (name = "rating")
    private Short rating;

    @Column(name = "address", length = 255)
    private String address;

    // PostGIS 위치 정보 컬럼 (실제 구현 시 의존성 추가 필요)
    // @Column(name = "location", columnDefinition = "geography(Point, 4326)")
    // private Point location;

    @CreationTimestamp  // JPA: 엔티티 생성 시 자동으로 현재 시간 기록
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp    // JPA: 엔티티 업데이트 시 자동으로 현재 시간 기록
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Post와 Tag의 다대다(N:M) 관계를 정의합니다.
    /* @ManyToMany: 다대다 관계임을 JPA에게 알려줍니다.
     * cascade: 이 엔티티의 상태 변화가 연관된 엔티티에 어떻게 전파될지 설정합니다.
     *  - PERSIST: 이 Post를 저장할 때, 연관된 tags중에 아직 저장되지 않은(새로운) Tag가 있다면 함께 저장됩니다.
     *  - MERGE: 분리된(detached) 상태의 Postㄹ르 다시 영속화할 때, 연관된 tags의 변경사항도 함께 병합(반영)됩니다.
     */

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    /*
     * @JoinTable: 다대다 관계를 매핑하기 위한 중간 테이플(Junction Table)의 정보를 설정합니다.
     * - name: 사용할 중간 테이블의 이름을 'post_tags'로 명시합니다.
     * - joinColumns: 현재 엔티티(Post)를 참조하는 중간 테이블의 외래키 컬럼을 설정합니다. (컬럼명: Post_id)
     * - inverseJoinColumns: 반대쪽 엔티티(Tag)를 참조하는 중간 테이블의 외래키 컬럼을 설정합니다. (컬럼명: tag_id)
     */
    @JoinTable(
        name = "post_tags", // 자동으로 생성될 중간 테이블의 이름
        joinColumns = @JoinColumn(name = "post_id"), // 중간 테이블에서 Post를 참조하는 외래키
        inverseJoinColumns = @JoinColumn(name = "tag_id") // 중간 테이블에서 Tag를 참조하는 외래키
    )
    private Set<Tag> tags = new HashSet<>();    // 중복을 허용하지 않는 Set 컬렉션 사용

    // ... (다른 필드 및 메소드) ...

 // ====================================================================
 // == 연관관계 편의 메소드 ==
 // ====================================================================
 /*
  * Post에 Tag를 추가하는 편의 메소드입니다.
  * Post의 tags 컬렉션에 Tag를 추가하고,
  * Tag의 posts 컬렉션에도 이 Posts를 추가하고 양방향 관계의 일관성을 유지합니다.
  * @param tag 추가할 태그
  */
    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getPosts().add(this);       // Tag 엔티티에도 posts 컬렉션이 있다면
    }

    /*
     * post에서 Tag를 제거하는 편의 메소드입니다.
     *  @param tag 제거할 태그
     */
    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getPosts().remove(this);
    }
}


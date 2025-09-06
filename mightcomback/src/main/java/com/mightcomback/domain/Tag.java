package com.mightcomback.domain;

import jakarta.persistence.*;
import Lombok.Getter;
import Lombok.NoArgsConstructor;
import Lombok.Setter;
import java.util.HashSet;
import java.util.Set;

/*
==================================================
Tag 엔티티 (Tag.jva)
--------------------------------------------------
 - 하나의 태그 정보를 나타내는 객체 입니다. (예 : #데이트)
 - DB의 'tags' xpdlqmfrhk 1:1로 매핑됩니다.
 */

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor // Lombok: 기본 생성자 자동 생성
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 태그 이름은 중복되면 안 되므로 unique = true 제약 조건을 추가합니다.
    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;

    // Post와의 관계 설정
    // 이 Tag가 여러 Post에 의해 사용될 수 있음을 의미합니다.
    // 'mappedBy = "tags"'는 이 관계의 주인이 Post 엔티티의 'tags' 필드임을 나타냅니다.
    // 즉, 관계에 대한 변경(추가/삭제)은 Post 엔티티를 통해서 이루어져야 합니다.
    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts = new HashSet<>();

    // 생성자, Getter, Setter 등은 Lombok 어노테이션(@Getter, @Setter, @NoArgsConstructor)으로 대체 가능
}


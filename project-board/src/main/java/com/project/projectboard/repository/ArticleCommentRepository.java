package com.project.projectboard.repository;

import com.project.projectboard.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource // 스프링 부트 데이터 레스트에서 지원하는 어노테이션, 별도의 컨트롤러와 서비스 영역 없이 미리 내부적으로 정의되어 있는 로직을 따라 처리됨, 그 로직은 해당 도메인의 정보를 매핑하여 REST API를 제공하는 역할을 함
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
}

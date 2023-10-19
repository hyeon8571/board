package com.project.projectboard.repository;

import com.project.projectboard.domain.Article;
import com.project.projectboard.domain.ArticleComment;
import com.project.projectboard.domain.QArticle;
import com.project.projectboard.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource // 스프링 부트 데이터 레스트에서 지원하는 어노테이션, 별도의 컨트롤러와 서비스 영역 없이 미리 내부적으로 정의되어 있는 로직을 따라 처리됨, 그 로직은 해당 도메인의 정보를 매핑하여 REST API를 제공하는 역할을 함
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>, // 엔티티에 있는 모든 필드에 대한 기본 검색 기능을 제공(부분검색, 대소문자 등은 안됨)
        QuerydslBinderCustomizer<QArticleComment> {

    List<ArticleComment> findByArticle_Id(Long articleId);  // 연관관계로 인해 _ 사용(comment 앤티티에서 articleId 사용)

    void deleteByIdAndUserAccount_UserId(Long articleCommentId, String userId);

    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {
        bindings.excludeUnlistedProperties(true); // 추가하지 않은 필드는 검색 조건에서 제외
        bindings.including(root.content, root.createdAt, root.createdBy); // 검색 조건 추가
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    };
}

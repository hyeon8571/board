package com.project.projectboard.controller;

import com.project.projectboard.config.SecurityConfig;
import com.project.projectboard.dto.ArticleWithCommentsDto;
import com.project.projectboard.dto.UserAccountDto;
import com.project.projectboard.dto.response.ArticleResponse;
import com.project.projectboard.service.ArticleService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    private final MockMvc mvc;

    @MockBean // mockito의 mock과 동일, @Autowired 불가, 필드에만 주입
    private ArticleService articleService;

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }


    @DisplayName("[VIEW][GET] 게시글 리스트 (게시판 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());

        // When & Then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk()) // 정상 호출인지
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // HTML 파일의 컨텐츠인지 (호환되는 컨텐츠 포함)
                .andExpect(view().name("articles/index")) // 뷰 이름 검사
                .andExpect(model().attributeExists("articles")); // 내부에 값이 있는지 (이름을 articles로 지정)
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
    }


    @DisplayName("[VIEW][GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        // Given
        Long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());

        // When & Then
        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk()) // 정상 호출인지
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // HTML 파일의 컨텐츠인지 (호환되는 컨텐츠 포함)
                .andExpect(view().name("articles/detail")) // 뷰 이름 검사
                .andExpect(model().attributeExists("article")) // 내부에 값이 있는지 (이름을 articles로 지정)
                .andExpect(model().attributeExists("articleComments")); // 댓글 리스트에도 값이 있어야 함
        then(articleService).should().getArticle(articleId);
    }

    // 픽스처

    private ArticleWithCommentsDto createArticleWithCommentsDto() {
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(1L,
                "uno",
                "pw",
                "uno@mail.com",
                "Uno",
                "memo",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }
}
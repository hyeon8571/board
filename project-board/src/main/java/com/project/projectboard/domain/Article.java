package com.project.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true) // 상속받은 안쪽까지 찍기 위해 사용
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
})
@Entity
public class Article extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    private UserAccount userAccount;


    @Setter
    @Column(nullable = false)
    private String title; // 제목

    @Setter
    @Column(nullable = false, length = 10000)
    private String content; // 본문

    @ToString.Exclude
    @JoinTable( // 주인관계의 엔티티에 사용해야함
            name = "article_hashtag",
            joinColumns = @JoinColumn(name = "articleId"),
            inverseJoinColumns = @JoinColumn(name = "hashtagId")
    )
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // persist = insert, merge = update
    private Set<Hashtag> hashtags = new LinkedHashSet<>();

    @ToString.Exclude // ToString을 찍기위해 필드를 돌다가 articleComment를 보고 들어감 -> articleComment 에서도 toString이 있기 때문에 또 필드를 돌다가 article로 들어감 => 순환참조, 이것을 방지하기 위해 exclude 사용
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

  
    protected Article() {}

    private Article(UserAccount userAccount, String title, String content) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
    }

    public static Article of(UserAccount userAccount, String title, String content) {
        return new Article(userAccount, title, content);
    }

    public void addHashtag(Hashtag hashtag) {
        this.getHashtags().add(hashtag);
    }

    public void addHashtags(Collection<Hashtag> hashtags) {
        this.getHashtags().addAll(hashtags);
    }

    public void clearHashtags() {
        this.getHashtags().clear();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article that)) return false;
        Article article = (Article) o;
        return this.getId() != null && this.getId().equals(that.getId()); // getId()가 아닌 바로 id로 접근할 경우 지연로딩 상황에서 해당 값이 null이 아니지만 null로 읽힐 수 있다
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}

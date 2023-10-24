package com.project.projectboard.service;

import com.project.projectboard.domain.Hashtag;
import com.project.projectboard.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    public Set<String> parseHashtagNames(String content) {
        if (content == null) {
            return Set.of();
        }

        Pattern pattern = Pattern.compile("#[\\w가-힣]+");
        Matcher matcher = pattern.matcher(content.strip()); // strip은 앞, 뒤 공백문자 자르기
        Set<String> result = new HashSet<>();

        while (matcher.find()) { // 정규식에 해당하는 단어가 발견되면
            result.add(matcher.group().replace("#", ""));
        }

        return Set.copyOf(result); // result를 불변하게 만들기 위해 Set.copyOf 사용, 그냥 result를 반환해도 결과는 같음
    }

    public Set<Hashtag> findHashtagsByNames(Set<String> hashtagNames) {
        return new HashSet<>(hashtagRepository.findByHashtagNameIn(hashtagNames));
    }

    public void deleteHashtagWithoutArticles(Long hashtagId) {
        Hashtag hashtag = hashtagRepository.getReferenceById(hashtagId);
        if (hashtag.getArticles().isEmpty()) {
            hashtagRepository.delete(hashtag);
        }
    }
}
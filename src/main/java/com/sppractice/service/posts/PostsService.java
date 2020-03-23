package com.sppractice.service.posts;

import com.sppractice.domain.posts.Posts;
import com.sppractice.domain.posts.PostsRepository;
import com.sppractice.web.dto.PostsListResponseDto;
import com.sppractice.web.dto.PostsResponseDto;
import com.sppractice.web.dto.PostsSaveRequestDto;
import com.sppractice.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    /* readOnly = true로 줄시 트랜잭션 범위는 유지하되, 조회 기능만 남겨두어 조회 속도가 개선된다.
        .map(PostsListReponseDto::new -> map(Posts -> new PostsListResponseDto(posts))와 같음.
        postsRepository 결과로 넘오온 Posts의 Stream을 map을 통해 postsListReponseDto 변환 -> lists로 반환하는 메소드
    */
    @Transactional
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream().map(PostsListResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public void delete (Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));

        postsRepository.delete(posts);
    }

    public PostsResponseDto findById (Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));

        return new PostsResponseDto(entity);
    }
}

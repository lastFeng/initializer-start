package com.welford.spring.boot.blog.initializerstart.service;

import com.welford.spring.boot.blog.initializerstart.domain.Vote;

/**
 * @author : guoweifeng
 * @date : 2021/4/25
 */
public interface VoteService {
    Vote getVoteById(Long id);

    void removeVote(Long id);
}
